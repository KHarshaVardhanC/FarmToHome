import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/MyOrders.css';

function MyOrders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const customerId = localStorage.getItem('customerId');

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    if (!customerId) {
      setError('Please login to view your orders');
      setLoading(false);
      return;
    }

    try {
      const response = await axios.get(`http://localhost:8080/order/customer/${customerId}`);
      // Sort orders by status - Placed orders first, then Processing, then Delivered
      const sortedOrders = response.data.sort((a, b) => {
        const statusOrder = { 'ordered': 1, 'Processing': 2, 'Delivered': 3 };
        return statusOrder[a.orderStatus] - statusOrder[b.orderStatus];
      });
      setOrders(sortedOrders);
    } catch (err) {
      console.error('Error fetching orders:', err);
      setError('Failed to load orders. Please try again.');
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
    navigate(`/review/${orderId}`); // Navigate to the updated route
  };

  // Function to handle buying again
  const handleBuyAgain = (order) => {
    // Store product info in localStorage for adding to cart
    const productToAdd = {
      productId: order.productId,
      productName: order.productName,
      productPrice: order.productPrice,
      imageUrl: order.imageUrl,
      orderQuantity: order.orderQuantity,
      productQuantityType: order.productQuantityType || 'kg',
      sellerId: order.sellerId
    };

    // Add to cart or navigate to product page
    try {
      // Get current cart from localStorage
      const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
      
      // Check if product already exists in cart
      const existingItemIndex = cartItems.findIndex(item => item.productId === productToAdd.productId);
      
      if (existingItemIndex !== -1) {
        // Update quantity if product already in cart
        cartItems[existingItemIndex].orderQuantity += productToAdd.orderQuantity;
      } else {
        // Add new product to cart
        cartItems.push(productToAdd);
      }
      
      // Save updated cart to localStorage
      localStorage.setItem('cartItems', JSON.stringify(cartItems));
      
      // Show success message or redirect to cart
      alert(`${productToAdd.productName} added to your cart!`);
      navigate('/cart');
    } catch (err) {
      console.error('Error adding product to cart:', err);
      alert('Failed to add product to cart. Please try again.');
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
                  
                  {/* Buy Again button only appears for delivered orders */}
                  {order.orderStatus === 'Delivered' && (
                    <button 
                      className="reorder-btn"
                      onClick={() => handleBuyAgain(order)}
                    >
                      Buy Again
                    </button>
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