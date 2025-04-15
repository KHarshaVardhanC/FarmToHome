import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { submitRating } from '../utils/api';
import '../styles/Rating.css';

const Rating = () => {
    const [rating, setRating] = useState(0);
    const [hover, setHover] = useState(0);
    const [feedback, setFeedback] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [message, setMessage] = useState('');
    const { productId } = useParams();

    // Get customer ID from localStorage
    // const customerId = localStorage.getItem('customerId');
    const customerId = 1;

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

        const ratingData = {
            customerId: parseInt(customerId),
            productId: parseInt(productId),
            ratingValue: rating,
            feedback: feedback
        };

        try {
            setIsSubmitting(true);
            await submitRating(ratingData);
            setMessage('Thank you for your feedback!');
            // Reset form
            setRating(0);
            setFeedback('');
        } catch (error) {
            setMessage('Failed to submit rating. Please try again.');
            console.error('Error submitting rating:', error);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="rating-container">
            <h2>Rate This Product</h2>
            <p>Product ID: {productId}</p>

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
                    <label htmlFor="feedback">Feedback</label>
                    <textarea
                        id="feedback"
                        value={feedback}
                        onChange={handleFeedbackChange}
                        placeholder="Share your thoughts about this product..."
                        rows="5"
                    />
                </div>

                {message && <div className={message.includes('Failed') ? 'error-message' : 'success-message'}>{message}</div>}

                <button
                    type="submit"
                    className="submit-button"
                    disabled={isSubmitting}
                >
                    {isSubmitting ? 'Submitting...' : 'Submit Rating'}
                </button>
            </form>
        </div>
    );
};

export default Rating;