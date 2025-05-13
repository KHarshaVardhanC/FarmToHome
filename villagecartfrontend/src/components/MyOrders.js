import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/MyOrders.css';


const API_BASE_URL = process.env.REACT_APP_BACKEND_URL;

function MyOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const customerId = localStorage.getItem('customerId');

  // State for report modal
  const [showReportModal, setShowReportModal] = useState(false);
  const [reportData, setReportData] = useState({
    orderId: '',
    reportReason: '',  // Fixed property name
    orderImage: null,
    previewImage: null
  });

  // State to track reported orders
  const [reportedOrders, setReportedOrders] = useState([]);

  useEffect(() => {
    fetchOrders();
    // Load reported orders from localStorage if available
    const savedReportedOrders = localStorage.getItem('reportedOrders');
    if (savedReportedOrders) {
      setReportedOrders(JSON.parse(savedReportedOrders));
    }
  }, []);

  const fetchOrders = async () => {
    if (!customerId) {
      setError('Please login to view your orders');
      setLoading(false);
      return;
    }

    try {
      const response = await axios.get(`${API_BASE_URL}/order/customer/${customerId}`);

      if (Array.isArray(response.data)) {
        const sortedOrders = response.data.sort((a, b) => {
          const statusOrder = { 'ordered': 1, 'Processing': 2, 'Delivered': 3 };
          return statusOrder[a.orderStatus] - statusOrder[b.orderStatus];
        });
        setOrders(sortedOrders);
        setError(null);
      } else {
        setOrders([]);
        setError(null);
      }
    } catch (err) {
      console.error('Error fetching orders:', err);
      setOrders([]);
      setError(null);
    } finally {
      setLoading(false);
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'ordered':
        return 'status-ordered';
      case 'Processing':
        return 'status-processing';
      case 'Delivered':
        return 'status-delivered';
      default:
        return '';
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-IN', {
      day: 'numeric',
      month: 'short',
      year: 'numeric'
    });
  };

  const handleGetInvoice = (orderId) => {
    navigate(`/invoice/${orderId}`);
  };

  const handleWriteReview = (orderId) => {
    if (!orderId) {
      console.error('Product ID is missing');
      alert('Cannot write review: Product ID is missing');
      return;
    }
    navigate(`/review/${orderId}`);
  };

  // Report functions
  const openReportModal = (orderId) => {
    setReportData({
      orderId: orderId,
      reportReason: '',  // Fixed property name
      orderImage: null,
      previewImage: null
    });
    setShowReportModal(true);
  };

  const closeReportModal = () => {
    setShowReportModal(false);
    setReportData({
      orderId: '',
      reportReason: '',  // Fixed property name
      orderImage: null,
      previewImage: null
    });
  };

  const handleReasonChange = (e) => {
    setReportData({ ...reportData, reportReason: e.target.value });
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setReportData({
        ...reportData,
        orderImage: file,
        previewImage: URL.createObjectURL(file)
      });
    }
  };

  const handleSubmitReport = async (e) => {
    e.preventDefault();

    if (!reportData.reportReason.trim()) {
      alert('Please provide a reason for your report');
      return;
    }

    try {
      const formData = new FormData();
      formData.append('orderId', reportData.orderId);
      formData.append('reportReason', reportData.reportReason);
      if (reportData.orderImage) {  // Fixed check to match the property name
        formData.append('orderImage', reportData.orderImage);
      }
      formData.append('customerId', customerId);

      // Fixed the API endpoint URL (removed template literal inside a template literal)
      const response = await axios.post(`${API_BASE_URL}/order/report`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      if (response.status === 200 || response.status === 201) {
        // Add order ID to reported orders list
        const updatedReportedOrders = [...reportedOrders, reportData.orderId];
        setReportedOrders(updatedReportedOrders);

        // Save reported orders to localStorage for persistence
        localStorage.setItem('reportedOrders', JSON.stringify(updatedReportedOrders));

        alert('Report submitted successfully');
        closeReportModal();

        // Refresh orders and navigate back to the orders page
        fetchOrders();
        navigate('/my-orders');
      } else {
        alert('Failed to submit report. Please try again.');
      }
    } catch (err) {
      console.error('Error submitting report:', err);
      alert(`Failed to submit report: ${err.message}`);  // Added error message to alert
    }
  };

  if (loading) return (
    <div className="my-orders-page">
      <Navbar />
      <div className="loading">Loading your orders...</div>
    </div>
  );

  if (error) return (
    <div className="my-orders-page">
      <Navbar />
      <div className="error-message">{error}</div>
    </div>
  );

  return (
    <div className="my-orders-page">
      <Navbar />
      <div className="orders-container">
        <h1 className="orders-title">My Orders</h1>

        {orders.length === 0 ? (
          <div className="empty-orders">
            <p>You haven't placed any orders yet</p>
            <button className="shop-now-btn" onClick={() => navigate('/customer-home')}>
              Shop Now
            </button>
          </div>
        ) : (
          <div className="orders-list">
            {orders.map(order => (
              <div key={order.orderId} className="order-card">
                <div className="order-header">
                  <div className="order-id">Order #{order.orderId}</div>
                  <div className={`order-status ${getStatusClass(order.orderStatus)}`}>
                    {order.orderStatus}
                  </div>
                </div>

                <div className="order-content">
                  <div className="order-image">
                    <img src={order.imageUrl} alt={order.productName} />
                  </div>

                  <div className="order-details">
                    <h3 className="product-name">{order.productName}</h3>
                    <p className="order-quantity">Quantity: {order.orderQuantity.toFixed(1)} {order.productQuantityType || 'kg'}</p>
                    <p className="order-price">Price: ₹{order.productPrice}/{order.productQuantityType || 'kg'}</p>
                    <p className="order-total">Total: ₹{(order.productPrice * order.orderQuantity).toFixed(2)}</p>

                    {order.productDescription && (
                      <p className="product-description">{order.productDescription}</p>
                    )}

                    <div className="seller-info">
                      <p className="seller-name">Seller: {order.sellerName || 'N/A'}</p>
                      {order.sellerPlace && order.sellerCity && (
                        <p className="seller-location">
                          Location: {order.sellerPlace}, {order.sellerCity}, {order.sellerState} - {order.sellerPincode}
                        </p>
                      )}
                    </div>
                  </div>
                </div>

                <div className="order-actions">
                  {/* Get Invoice button appears for all orders */}
                  <button
                    className="invoice-btn"
                    onClick={() => handleGetInvoice(order.orderId)}
                  >
                    Get Invoice
                  </button>

                  {/* Report button logic */}
                  {order.orderStatus === 'Delivered' && !reportedOrders.includes(order.orderId) && (
                    <button
                      className="report-btn"
                      onClick={() => openReportModal(order.orderId)}
                    >
                      Report Issue
                    </button>
                  )}

                  {/* Reported button (disabled) */}
                  {order.orderStatus === 'Delivered' && reportedOrders.includes(order.orderId) && (
                    <button
                      className="report-btn reported-btn"
                      disabled
                    >
                      Reported
                    </button>
                  )}

                  {/* Write a Review button only appears for delivered orders that haven't been rated yet */}
                  {order.orderStatus === 'Delivered' && order.orderRatingStatus !== 'Rated' && (
                    <button
                      className="review-btn"
                      onClick={() => handleWriteReview(order.orderId)}
                    >
                      Write a Review
                    </button>
                  )}

                  {/* Show a disabled button if already rated */}
                  {order.orderStatus === 'Delivered' && order.orderRatingStatus === 'Rated' && (
                    <button
                      className="review-btn rated-btn"
                      disabled
                    >
                      Already Reviewed
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Report Modal */}
      {showReportModal && (
        <div className="report-modal-overlay">
          <div className="report-modal">
            <div className="report-modal-header">
              <h2>Report Issue with Order #{reportData.orderId}</h2>
              <button className="close-btn" onClick={closeReportModal}>&times;</button>
            </div>
            <form className="report-form" onSubmit={handleSubmitReport}>
              <div className="form-group">
                <label htmlFor="report-reason">Reason for Report:</label>
                <textarea
                  id="report-reason"
                  value={reportData.reportReason}  // Fixed property name
                  onChange={handleReasonChange}
                  placeholder="Please describe the issue with your order..."
                  required
                ></textarea>
              </div>

              <div className="form-group">
                <label htmlFor="report-image">Upload Image :</label>
                <input
                  type="file"
                  id="report-image"
                  accept="image/*"
                  onChange={handleImageChange}
                />
                {reportData.previewImage && (
                  <div className="image-preview">
                    <img src={reportData.previewImage} alt="Preview" />
                  </div>
                )}
              </div>

              <div className="form-actions">
                <button type="button" className="cancel-btn" onClick={closeReportModal}>Cancel</button>
                <button type="submit" className="submit-btn">Submit Report</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default MyOrders;