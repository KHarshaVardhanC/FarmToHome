import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/MyOrders.css';

function MyOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [customerId, setCustomerId] = useState(null);
  const [userName, setUserName] = useState('');

  useEffect(() => {
    // Get user info from localStorage
    const storedCustomerId = localStorage.getItem('customerId');
    const storedUserName = localStorage.getItem('userName');
    
    if (storedUserName) {
      setUserName(storedUserName);
    }
    
    if (storedCustomerId) {
      setCustomerId(storedCustomerId);
      fetchOrders(storedCustomerId);
    } else {
      setLoading(false);
      setError('Please login to view your orders');
    }
  }, []);

  const fetchOrders = async (customerId) => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:8080/order/customer/${customerId}`);
      
      // Sort orders by date (newest first)
      const sortedOrders = response.data.sort((a, b) => {
        return new Date(b.orderDate) - new Date(a.orderDate);
      });
      
      setOrders(sortedOrders);
    } catch (err) {
      console.error('Error fetching orders:', err);
      setError('Failed to load your orders. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Function to format date
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
  };

  // Calculate total price of an order
  const calculateOrderTotal = (orderItems) => {
    return orderItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  };

  // Get appropriate status class
  const getStatusClass = (status) => {
    switch (status.toLowerCase()) {
      case 'delivered':
        return 'status-delivered';
      case 'shipped':
        return 'status-shipped';
      case 'processing':
        return 'status-processing';
      case 'cancelled':
        return 'status-cancelled';
      default:
        return 'status-placed';
    }
  };

  if (loading) {
    return (
      <div className="my-orders-page">
        <Navbar userName={userName} />
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading your orders...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="my-orders-page">
      <Navbar userName={userName} />
      
      <div className="orders-container">
        <h1 className="orders-title">My Orders</h1>
        
        {error && (
          <div className="error-message">
            <p>{error}</p>
          </div>
        )}
        
        {!error && orders.length === 0 && (
          <div className="no-orders">
            <i className="fas fa-box-open fa-4x"></i>
            <h2>No Orders Yet</h2>
            <p>You haven't placed any orders yet. Start shopping to place your first order!</p>
          </div>
        )}
        
        {orders.length > 0 && (
          <div className="orders-list">
            {orders.map((order) => (
              <div key={order.orderId} className="order-card">
                <div className="order-header">
                  <div className="order-info">
                    <div className="order-id">
                      <span className="label">Order #:</span>
                      <span>{order.orderId}</span>
                    </div>
                    <div className="order-date">
                      <span className="label">Placed on:</span>
                      <span>{formatDate(order.orderDate)}</span>
                    </div>
                  </div>
                  
                  <div className={`order-status ${getStatusClass(order.status)}`}>
                    {order.status}
                  </div>
                </div>
                
                <div className="order-items">
                  {order.items.map((item, index) => (
                    <div key={`${order.orderId}-${index}`} className="order-item">
                      <div className="item-image">
                        <img src={item.imageUrl || '/images/placeholder.jpg'} alt={item.name} />
                      </div>
                      
                      <div className="item-details">
                        <h3 className="item-name">{item.name}</h3>
                        <p className="item-price">Price: ₹{item.price}/kg</p>
                        <p className="item-quantity">Quantity: {item.quantity}</p>
                      </div>
                      
                      <div className="item-subtotal">
                        <p>₹{(item.price * item.quantity).toFixed(2)}</p>
                      </div>
                    </div>
                  ))}
                </div>
                
                <div className="order-footer">
                  <div className="order-total">
                    <span className="label">Total:</span>
                    <span className="total-amount">₹{calculateOrderTotal(order.items).toFixed(2)}</span>
                  </div>
                  
                  {order.deliveryAddress && (
                    <div className="delivery-address">
                      <span className="label">Delivery Address:</span>
                      <span>{order.deliveryAddress}</span>
                    </div>
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