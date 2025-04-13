import React, { useState, useEffect } from 'react';
import axios from 'axios';
import CustomerNavbar from './CustomerNavbar';
import '../styles/MyOrders.css';

function MyOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [customerId, setCustomerId] = useState(null);
  const [userName, setUserName] = useState('');

  useEffect(() => {
    // Get user data from localStorage
    const storedUserName = localStorage.getItem('userName');
    const storedCustomerId = localStorage.getItem('customerId');
    
    setUserName(storedUserName || '');
    setCustomerId(storedCustomerId || null);

    if (storedCustomerId) {
      fetchOrders(storedCustomerId);
    } else {
      setLoading(false);
      setError("Customer ID not found. Please login again.");
    }
  }, []);

  const fetchOrders = async (id) => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:8080/order/customer/${id}`);
      
      // Sort orders by date (most recent first)
      const sortedOrders = response.data.sort((a, b) => 
        new Date(b.orderDate) - new Date(a.orderDate)
      );
      
      setOrders(sortedOrders);
    } catch (err) {
      console.error('Error fetching orders:', err);
      setError(err.response?.data?.message || "Failed to load orders");
    } finally {
      setLoading(false);
    }
  };

  // Format date for better readability
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  return (
    <div className="my-orders-page">
      <CustomerNavbar userName={userName} />
      
      <div className="my-orders-container">
        <h1>My Orders</h1>

        {loading && <div className="loading-spinner">Loading orders...</div>}
        
        {error && (
          <div className="error-message">
            <p>{error}</p>
            {customerId && (
              <button onClick={() => fetchOrders(customerId)} className="retry-btn">
                Try Again
              </button>
            )}
          </div>
        )}

        {!loading && !error && orders.length === 0 && (
          <div className="no-orders">
            <h3>You haven't placed any orders yet</h3>
            <p>Browse our products and place your first order!</p>
            <button onClick={() => window.location.href = '/customer-home'} className="shop-now-btn">
              Shop Now
            </button>
          </div>
        )}

        {orders.length > 0 && (
          <div className="orders-list">
            {orders.map((order) => (
              <div key={order.orderId} className="order-card">
                <div className="order-header">
                  <div className="order-info">
                    <h3>Order #{order.orderId}</h3>
                    <p className="order-date">Placed on: {formatDate(order.orderDate)}</p>
                    <p className="order-status">Status: <span className={`status-${order.orderStatus.toLowerCase()}`}>{order.orderStatus}</span></p>
                  </div>
                  <div className="order-total">
                    <p>Total: ₹{order.totalAmount.toFixed(2)}</p>
                  </div>
                </div>
                
                <div className="order-items">
                  {order.orderItems.map((item, index) => (
                    <div key={index} className="order-item">
                      <div className="item-image">
                        <img src={item.product.imageUrl || '/images/placeholder.jpg'} alt={item.product.productName} />
                      </div>
                      <div className="item-details">
                        <h4>{item.product.productName}</h4>
                        <p>Quantity: {item.quantity}</p>
                        <p>Price: ₹{item.price.toFixed(2)}/kg</p>
                      </div>
                    </div>
                  ))}
                </div>
                
                <div className="order-actions">
                  {order.orderStatus === 'DELIVERED' && (
                    <button className="review-btn">Write Review</button>
                  )}
                  {order.orderStatus === 'PROCESSING' && (
                    <button className="cancel-btn">Cancel Order</button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyOrders;