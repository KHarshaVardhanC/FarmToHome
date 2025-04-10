import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/MyOrdersPage.css';

function MyOrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        const customerId = localStorage.getItem('customerId');

        // Check if customerId is valid
        if (!customerId || customerId === "null") {
          throw new Error("Customer ID is missing. Please log in again.");
        }

        const response = await axios.get(`http://localhost:8080/order/customer/${customerId}`);
        const fetchedOrders = response.data;

        // Ensure orderItems is always an array
        const formattedOrders = fetchedOrders.map(order => ({
          ...order,
          orderItems: Array.isArray(order.orderItems) ? order.orderItems : [order.orderItems]
        }));

        setOrders(formattedOrders);
      } catch (err) {
        console.error('Error fetching orders:', err);
        setError(err.response?.data?.message || err.message || 'Failed to fetch orders');
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  return (
    <div className="my-orders-page">
      <Navbar cartCount={JSON.parse(localStorage.getItem('cart'))?.length || 0} />
      
      <div className="orders-container">
        <h1>My Orders</h1>
        
        {loading ? (
          <div className="loading">Loading your orders...</div>
        ) : error ? (
          <div className="error-message">
            <p>{error}</p>
          </div>
        ) : orders.length === 0 ? (
          <div className="no-orders">
            <p>You haven't placed any orders yet.</p>
          </div>
        ) : (
          <div className="orders-list">
            {orders.map((order, index) => (
              <div key={index} className="order-card">
                <div className="order-header">
                  <div className="order-info">
                    <span className="order-date">
                      Ordered on: {new Date(order.orderDate).toLocaleDateString()}
                    </span>
                    <span className="order-status">Status: {order.orderStatus}</span>
                  </div>
                  <div className="order-total">
                    Total: ${order.orderTotal?.toFixed(2) || "N/A"}
                  </div>
                </div>
                
                <div className="order-items">
                  {order.orderItems.map((item) => (
                    <div key={item.productId} className="order-item">
                      <div className="item-image">
                        <img src={item.imageUrl} alt={item.productName} />
                      </div>
                      <div className="item-details">
                        <h4>{item.productName}</h4>
                        <p>Quantity: {item.productQuantity}</p>
                        <p>Price: ${item.productPrice?.toFixed(2) || "N/A"}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyOrdersPage;