import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getOrderInvoice } from '../utils/api';
import '../styles/OrderInvoice.css';
import { FaBoxOpen, FaTruck, FaCheckCircle, FaArrowLeft } from 'react-icons/fa';

const OrderInvoice = () => {
    const [invoice, setInvoice] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const { orderId } = useParams();

    useEffect(() => {
        const fetchInvoice = async () => {
            try {
                setLoading(true);
                const data = await getOrderInvoice(orderId);
                setInvoice(data);
                setLoading(false);
            } catch (err) {
                setError('Failed to load invoice details. Please try again later.');
                setLoading(false);
            }
        };

        fetchInvoice();
    }, [orderId]);

    const handleBackClick = () => {
        navigate('/my-orders');
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="pulse-loader"></div>
                <p>Loading your invoice...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-container">
                <div className="error-icon">!</div>
                <h3>Something went wrong</h3>
                <p>{error}</p>
                <button onClick={() => window.location.reload()} className="retry-button">Try Again</button>
            </div>
        );
    }

    if (!invoice) return <div className="error-container"><p>Invoice not found</p></div>;

    const totalAmount = invoice.productPrice * invoice.orderQuantity;
    const grandTotal = totalAmount; // No tax calculation

    const getStatusIcon = () => {
        switch (invoice.orderStatus.toLowerCase()) {
            case 'completed':
                return <FaCheckCircle className="status-icon completed" />;
            case 'processing':
                return <FaBoxOpen className="status-icon processing" />;
            case 'shipped':
                return <FaTruck className="status-icon shipped" />;
            default:
                return null;
        }
    };

    return (
        <div className="invoice-container">
            <button className="back-button" onClick={handleBackClick}>
                <FaArrowLeft /> Back to My Orders
            </button>

            <div className="invoice-card">
                <div className="invoice-header">
                    <div className="brand-section">
                        <div className="logo-wrapper">
                            <div className="company-logo">village<span>Cart</span></div>
                        </div>
                        <p className="company-tagline">Your Village Shopping Experience</p>
                    </div>

                    <div className="invoice-title-section">
                        <h1>INVOICE</h1>
                        <div className="invoice-number">#{invoice.orderId}</div>
                    </div>
                </div>

                <div className="invoice-meta">
                    <div className="meta-item status-container">
                        <span className="meta-label">Status</span>
                        <div className="status-badge">
                            {getStatusIcon()}
                            <span className={`status-text ${invoice.orderStatus.toLowerCase()}`}>
                                {invoice.orderStatus}
                            </span>
                        </div>
                    </div>
                </div>

                <div className="parties-container">
                    <div className="party-card customer">
                        <h3>Customer</h3>
                        <div className="party-details">
                            <div className="party-name">{invoice.customerName}</div>
                            <div className="party-address">
                                <p>{invoice.customerPlace}</p>
                                <p>{invoice.customerCity}, {invoice.customerState}</p>
                                <p>{invoice.customerPincode}</p>
                            </div>
                        </div>
                    </div>

                    <div className="party-card seller">
                        <h3>Seller</h3>
                        <div className="party-details">
                            <div className="party-name">{invoice.sellerName}</div>
                            <div className="party-address">
                                <p>{invoice.sellerPlace}</p>
                                <p>{invoice.sellerCity}, {invoice.sellerState}</p>
                                <p>{invoice.sellerPincode}</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="product-section">
                    <div className="product-image-container">
                        {invoice.imageUrl ? (
                            <img src={invoice.imageUrl} alt={invoice.productName} />
                        ) : (
                            <div className="no-image">No image available</div>
                        )}
                    </div>

                    <div className="product-details">
                        <h2 className="product-name">{invoice.productName}</h2>
                        <p className="product-description">{invoice.productDescription}</p>
                        <div className="product-meta">
                            <div className="product-meta-item">
                                <span className="meta-label">Unit Price</span>
                                <span className="meta-value">₹{invoice.productPrice.toFixed(2)}</span>
                            </div>
                            <div className="product-meta-item">
                                <span className="meta-label">Quantity</span>
                                <span className="meta-value">{invoice.orderQuantity}</span>
                            </div>
                            <div className="product-meta-item">
                                <span className="meta-label">Total</span>
                                <span className="meta-value">₹{totalAmount.toFixed(2)}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="payment-summary">
                    <h3>Payment</h3>
                    <div className="summary-table">
                        <div className="summary-row">
                            <span>Subtotal</span>
                            <span>₹{totalAmount.toFixed(2)}</span>
                        </div>
                        <div className="summary-row shipping">
                            <span>Shipping</span>
                            <span>Free</span>
                        </div>
                        <div className="summary-row total">
                            <span>Total Amount</span>
                            <span>₹{grandTotal.toFixed(2)}</span>
                        </div>
                    </div>
                </div>

                <div className="invoice-footer">
                    <div className="thank-you-message">
                        <h4>Thank you for your purchase!</h4>
                        <p>If you have any questions about this invoice, please contact us at support@villagecart.com</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderInvoice;