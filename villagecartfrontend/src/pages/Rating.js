import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/Rating.css';


const API_BASE_URL = process.env.REACT_APP_BACKEND_URL;

const Rating = () => {
    const [rating, setRating] = useState(0);
    const [hover, setHover] = useState(0);
    const [feedback, setFeedback] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [message, setMessage] = useState('');
    const [orderDetails, setOrderDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const { orderId } = useParams();
    const navigate = useNavigate();

    // Get customer ID from localStorage
    const customerId = localStorage.getItem('customerId');

    // Fetch order details when component mounts
    useEffect(() => {
        const fetchOrderDetails = async () => {
            if (!customerId) {
                setMessage('Please login to view order details');
                setLoading(false);
                return;
            }

            if (!orderId) {
                setMessage('Invalid order ID');
                setLoading(false);
                return;
            }

            try {
                // Fetch all orders for the customer
                const response = await axios.get(`${API_BASE_URL}/order/customer/${customerId}`);
                const orders = response.data;
                
                // Find the specific order by orderId
                const orderDetail = orders.find(order => order.orderId === parseInt(orderId));
                
                if (orderDetail) {
                    setOrderDetails(orderDetail);
                } else {
                    setMessage('Order not found');
                }
            } catch (error) {
                console.error('Error fetching order details:', error);
                setMessage('Failed to load order details');
            } finally {
                setLoading(false);
            }
        };

        fetchOrderDetails();
    }, [customerId, orderId]);

    const handleRatingChange = (value) => {
        setRating(value);
    };

    const handleFeedbackChange = (e) => {
        setFeedback(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!customerId) {
            setMessage('Please login to submit a rating');
            return;
        }

        if (rating === 0) {
            setMessage('Please select a rating');
            return;
        }

        // Create the rating data object
        const ratingData = {
            customerId: parseInt(customerId),
            orderId: parseInt(orderId),
            ratingValue: rating,
            feedback: feedback,
            productName: orderDetails?.productName
        };

        try {
            setIsSubmitting(true);
            // Submit the rating to your API endpoint
            await axios.post(`${API_BASE_URL}/rating`, ratingData);
            
            setMessage('Thank you for your feedback!');
            // Reset form
            setRating(0);
            setFeedback('');
            
            // Navigate back to orders after a brief delay
            setTimeout(() => {
                navigate('/my-orders');
            }, 2000);
        } catch (error) {
            setMessage('Failed to submit rating. Please try again.');
            console.error('Error submitting rating:', error);
        } finally {
            setIsSubmitting(false);
        }
    };

    if (loading) {
        return <div className="rating-container">Loading order details...</div>;
    }

    if (!orderDetails) {
        return (
            <div className="rating-container">
                <div className="error-message">
                    {message || 'Order details not found'}
                </div>
                <button className="back-button" onClick={() => navigate('/my-orders')}>
                    Back to My Orders
                </button>
            </div>
        );
    }

    return (
        <div className="rating-container">
            <h2>Rate This Product</h2>
            
            <div className="product-info">
                <img 
                    src={orderDetails.imageUrl} 
                    alt={orderDetails.productName} 
                    className="product-image" 
                />
                <h3>{orderDetails.productName}</h3>
                <p className="product-description">{orderDetails.productDescription || ''}</p>
                <p className="order-id">Order #{orderId}</p>
            </div>

            <form onSubmit={handleSubmit}>
                <div className="stars-container">
                    {[...Array(5)].map((_, index) => {
                        const ratingValue = index + 1;

                        return (
                            <label key={index}>
                                <input
                                    type="radio"
                                    name="rating"
                                    value={ratingValue}
                                    onClick={() => handleRatingChange(ratingValue)}
                                />
                                <span
                                    className={`star ${ratingValue <= (hover || rating) ? 'filled' : ''}`}
                                    onMouseEnter={() => setHover(ratingValue)}
                                    onMouseLeave={() => setHover(0)}
                                >
                                    ★
                                </span>
                            </label>
                        );
                    })}
                </div>

                <div className="rating-value">{rating > 0 ? `${rating} out of 5` : 'Select Rating'}</div>

                <div className="feedback-container">
                    <label htmlFor="feedback">Feedback (Optional)</label>
                    <textarea
                        id="feedback"
                        value={feedback}
                        onChange={handleFeedbackChange}
                        placeholder="Share your thoughts about this product..."
                        rows="5"
                    />
                </div>

                {message && <div className={message.includes('Failed') ? 'error-message' : 'success-message'}>{message}</div>}

                <div className="button-group">
                    <button
                        type="submit"
                        className="submit-button"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Submitting...' : 'Submit Rating'}
                    </button>
                    
                    <button 
                        type="button" 
                        className="cancel-button" 
                        onClick={() => navigate('/my-orders')}
                    >
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    );
};

export default Rating;