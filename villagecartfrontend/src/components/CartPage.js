import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/CartPage.css';

function CartPage() {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showOrderPopup, setShowOrderPopup] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  const [placingOrder, setPlacingOrder] = useState(false);
  const navigate = useNavigate();
  const customerId = localStorage.getItem('customerId');

  useEffect(() => {
    fetchCartItems();
  }, []);

  const fetchCartItems = async () => {
    if (!customerId) {
      setError('Please login to view your cart');
      setLoading(false);
      return;
    }
  
    try {
      const response = await axios.get(`http://localhost:8080/order/orders/incart/${customerId}`);
     
      if (Array.isArray(response.data)) {
        setCartItems(response.data);
        setError(null); 
      } else {
        // Handle case where response is not in expected format
        setCartItems([]);
        setError(null); // Don't show error for empty cart
      }
    } catch (err) {
      console.error('Error fetching cart items:', err);
      
      setCartItems([]);
      setError(null);
    } finally {
      setLoading(false);
    }
  };
  const handleQuantityChange = async (orderId, newQuantity) => {
    if (newQuantity < 1) return;
  
    try {
      // 1. Update backend (assumes you have an endpoint like this)
      await axios.put(`http://localhost:8080/order/update/${orderId}/${newQuantity}`);
  
      // 2. Update state
      setCartItems(prevItems =>
        prevItems.map(item =>
          item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
        )
      );
  
      // 3. Update localStorage
      const cart = JSON.parse(localStorage.getItem('cart')) || [];
      const updatedCart = cart.map(item =>
        item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
      );
      localStorage.setItem('cart', JSON.stringify(updatedCart));
    } catch (error) {
      console.error("Failed to update quantity:", error);
      alert("Failed to update quantity. Please try again.");
    }
  };
  

    // const handleQuantityChange = (orderId, newQuantity) => {
    //   if (newQuantity < 1) return;

    //   setCartItems(prevItems =>
    //     prevItems.map(item =>
    //       item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
    //     )
    //   );
    //   // console.log(prevItems);

    //   const cart = JSON.parse(localStorage.getItem('cart')) || [];
    //   const updatedCart = cart.map(item =>
    //     item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
    //   );
    //   localStorage.setItem('cart', JSON.stringify(updatedCart));
    // };

  const removeFromCart = async (orderId) => {
    try {
      await axios.delete(`http://localhost:8080/order/delete/${orderId}`);
      setCartItems(prevItems => prevItems.filter(item => item.orderId !== orderId));

      const cart = JSON.parse(localStorage.getItem('cart')) || [];
      const updatedCart = cart.filter(item => item.orderId !== orderId);
      localStorage.setItem('cart', JSON.stringify(updatedCart));
    } catch (err) {
      console.error('Error removing item from cart:', err);
      setError('Failed to remove item from cart.');
    }
  };

  const calculateTotal = (items = cartItems) => {
    return items.reduce((total, item) => total + item.productPrice * item.orderQuantity, 0).toFixed(2);
  };

  const handleBuyNow = (item) => {
    setSelectedItem(item);
    setShowOrderPopup(true);
  };

  const handleBuyAll = () => {
    setSelectedItem(null);
    setShowOrderPopup(true);
  };

  const placeOrder = async () => {
    try {
      setPlacingOrder(true);
      const itemsToOrder = selectedItem ? [selectedItem] : cartItems;
      let allOrdersSuccessful = true;
  
      for (const item of itemsToOrder) {
        // Use PUT request to update order status
        const response = await axios.put(
          `http://localhost:8080/order/order/${item.orderId}/ordered`
        );
  
        if (response.status === 200 || response.status === 201) {
          // Remove from cart items in state
          setCartItems(prev => prev.filter(cartItem => cartItem.orderId !== item.orderId));
  
          // Update local storage cart
          const cart = JSON.parse(localStorage.getItem('cart')) || [];
          const updatedCart = cart.filter(cartItem => cartItem.orderId !== item.orderId);
          localStorage.setItem('cart', JSON.stringify(updatedCart));
        } else {
          allOrdersSuccessful = false;
          console.error("Order failed with status", response.status);
        }
      }
  
      if (allOrdersSuccessful) {
        alert("Order placed successfully!");
        setShowOrderPopup(false);
        navigate("/my-orders");
      } else {
        alert("Some orders failed to place. Please check console.");
      }
    } catch (error) {
      console.error("Error placing order:", error);
      alert("An error occurred while placing the order.");
    } finally {
      setPlacingOrder(false);
    }
  };

  if (loading) return (
    <div className="cart-page">
      <Navbar />
      <div className="loading">Loading your cart...</div>
    </div>
  );

  if (error) return (
    <div className="cart-page">
      <Navbar />
      <div className="error-message">{error}</div>
    </div>
  );

  return (
    <div className="cart-page">
      <Navbar cartCount={cartItems.length} />
      <div className="cart-container">
        <h1 className="cart-title">Your Shopping Cart</h1>
        {cartItems.length === 0 ? (
          <div className="empty-cart">
            <p>Your cart is empty</p>
            <button className="continue-shopping-btn" onClick={() => navigate('/customer-home')}>
              Continue Shopping
            </button>
          </div>
        ) : (
          <>
            <div className="cart-items">
              {cartItems.map(item => (
                <div key={item.orderId} className="cart-item">
                  <div className="cart-item-image">
                    <img src={item.imageUrl} alt={item.productName} />
                  </div>
                  <div className="cart-item-details">
                    <h3>{item.productName}</h3>
                    <p className="item-price">₹{item.productPrice}/kg</p>
                    <div className="quantity-controls">
                      <button onClick={() => handleQuantityChange(item.orderId, item.orderQuantity - 1)}>-</button>
                      <span>{item.orderQuantity.toFixed(1)} kg</span>
                      <button onClick={() => handleQuantityChange(item.orderId, item.orderQuantity + 1)}>+</button>
                    </div>
                    <p className="item-total">Total: ₹{(item.productPrice * item.orderQuantity).toFixed(2)}</p>
                    <div className="item-actions">
                      <button className="buy-now-btn" onClick={() => handleBuyNow(item)}>Buy Now</button>
                      <button className="remove-btn" onClick={() => removeFromCart(item.orderId)}>Remove</button>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className="cart-summary">
              <h2>Order Summary</h2>
              <div className="summary-details">
                <div className="summary-row">
                  <span>Items ({cartItems.length}):</span>
                  <span>₹{calculateTotal()}</span>
                </div>
                <div className="summary-row">
                  <span>Delivery:</span>
                  <span>Free</span>
                </div>
                <div className="summary-row total">
                  <span>Total:</span>
                  <span>₹{calculateTotal()}</span>
                </div>
              </div>
              <button className="checkout-btn" onClick={handleBuyAll}>
                Buy All Items
              </button>
            </div>
          </>
        )}
      </div>

      {showOrderPopup && (
        <div className="order-popup-overlay">
          <div className="order-popup">
            <button className="close-popup" onClick={() => setShowOrderPopup(false)}>×</button>
            <h2>Complete Your Order</h2>
            
            <div className="order-items-summary">
              {selectedItem ? (
                <div className="order-item">
                  <img src={selectedItem.imageUrl} alt={selectedItem.productName} />
                  <div className="order-item-details">
                    <h3>{selectedItem.productName}</h3>
                    <p>Quantity: {selectedItem.orderQuantity.toFixed(1)} kg</p>
                    <p>Price: ₹{selectedItem.productPrice}/kg</p>
                    <p className="order-item-total">Item Total: ₹{(selectedItem.productPrice * selectedItem.orderQuantity).toFixed(2)}</p>
                  </div>
                </div>
              ) : (
                <>
                  <h3>Order Summary ({cartItems.length} items)</h3>
                  <div className="order-items-list">
                    {cartItems.map(item => (
                      <div key={item.orderId} className="order-summary-item">
                        <span>{item.productName} ({item.orderQuantity.toFixed(1)} kg)</span>
                        <span>₹{(item.productPrice * item.orderQuantity).toFixed(2)}</span>
                      </div>
                    ))}
                  </div>
                  <div className="order-total">
                    <strong>Total: ₹{calculateTotal()}</strong>
                  </div>
                </>
              )}
            </div>
            
            <button 
              className="place-order-btn" 
              onClick={placeOrder}
              disabled={placingOrder}
            >
              {placingOrder ? "Processing..." : "Place Order"}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default CartPage;