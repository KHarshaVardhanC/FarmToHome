import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/CartPage.css';

function CartPage() {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userName, setUserName] = useState('');
  const [customerId, setCustomerId] = useState('');
  const [showOrderPopup, setShowOrderPopup] = useState(false);
  const [orderDetails, setOrderDetails] = useState({
    products: [],
    totalPrice: 0,
    isSingleProduct: false
  });
  const [orderSuccess, setOrderSuccess] = useState(false);

  useEffect(() => {
    // Get user name and ID from localStorage
    const name = localStorage.getItem('userName');
    const id = localStorage.getItem('customerId');
    setUserName(name || 'Customer');
    setCustomerId(id);

    if (id) {
      // If we have a customerId, fetch their cart from backend
      fetchCustomerCart(id);
    } else {
      // Otherwise use local storage cart
      try {
        const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
        setCartItems(savedCart);
        setLoading(false);
      } catch (e) {
        console.warn('Invalid cart data in localStorage:', e);
        localStorage.removeItem('cart');
        setCartItems([]);
        setLoading(false);
      }
    }
  }, []);

  const fetchCustomerCart = async (id) => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:8080/order/orders/incart/${id}`);
      setCartItems(response.data);
      setError(null);
    } catch (err) {
      console.error('Error fetching customer cart:', err);
      setError('Failed to fetch your cart. Please try again later.');
      
      // Fallback to localStorage
      try {
        const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
        setCartItems(savedCart);
      } catch (e) {
        setCartItems([]);
      }
    } finally {
      setLoading(false);
    }
  };

  const updateQuantity = (productId, newQuantity) => {
    if (newQuantity < 1) return;
    
    const updatedCart = cartItems.map(item => 
      item.productId === productId ? { ...item, quantity: newQuantity } : item
    );
    
    setCartItems(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
    
    // If customer is logged in, update cart in backend
    if (customerId) {
      // This would be an API call to update the quantity in the backend
      // Implementation depends on your API design
    }
  };

  const removeItem = (productId) => {
    const updatedCart = cartItems.filter(item => item.productId !== productId);
    setCartItems(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
    
    // If customer is logged in, update cart in backend
    if (customerId) {
      // This would be an API call to remove the item from the backend cart
    }
  };

  const calculateTotal = () => {
    return cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
  };

  const handleBuyNow = (product) => {
    setOrderDetails({
      products: [product],
      totalPrice: product.price * product.quantity,
      isSingleProduct: true
    });
    setShowOrderPopup(true);
  };

  const handleBuyAll = () => {
    setOrderDetails({
      products: cartItems,
      totalPrice: calculateTotal(),
      isSingleProduct: false
    });
    setShowOrderPopup(true);
  };

  const placeOrder = async () => {
    try {
      // Here you would implement your API call to place the order
      // const response = await axios.post('http://localhost:8080/order/place', {
      //   customerId,
      //   products: orderDetails.products.map(p => ({
      //     productId: p.productId,
      //     quantity: p.quantity
      //   }))
      // });

      // For now, we'll simulate successful order placement
      setOrderSuccess(true);
      
      // Remove ordered items from cart
      let updatedCart;
      if (orderDetails.isSingleProduct) {
        // Remove just the single product
        updatedCart = cartItems.filter(
          item => item.productId !== orderDetails.products[0].productId
        );
      } else {
        // Remove all products
        updatedCart = [];
      }
      
      setCartItems(updatedCart);
      localStorage.setItem('cart', JSON.stringify(updatedCart));
      
      // Reset order popup after a delay
      setTimeout(() => {
        setShowOrderPopup(false);
        setOrderSuccess(false);
      }, 3000);
    } catch (err) {
      console.error('Error placing order:', err);
      setError('Failed to place order. Please try again.');
    }
  };

  return (
    <div className="cart-page">
      <Navbar userName={userName} cartCount={cartItems.length} />
      
      <div className="cart-container">
        <h1>Your Shopping Cart</h1>
        
        {loading && <div className="loading">Loading your cart...</div>}
        
        {error && <div className="error-message">{error}</div>}
        
        {!loading && !error && cartItems.length === 0 && (
          <div className="empty-cart">
            <h2>Your cart is empty</h2>
            <p>Add some products to your cart and they will appear here.</p>
          </div>
        )}
        
        {cartItems.length > 0 && (
          <>
            <div className="cart-items">
              {cartItems.map((item) => (
                <div key={item.productId} className="cart-item">
                  <div className="item-image">
                    <img src={item.image} alt={item.name} />
                  </div>
                  <div className="item-details">
                    <h3>{item.name}</h3>
                    <p className="item-price">₹{item.price}/kg</p>
                  </div>
                  <div className="item-quantity">
                    <button 
                      onClick={() => updateQuantity(item.productId, item.quantity - 1)}
                      className="quantity-btn"
                    >
                      -
                    </button>
                    <span>{item.quantity}</span>
                    <button 
                      onClick={() => updateQuantity(item.productId, item.quantity + 1)}
                      className="quantity-btn"
                    >
                      +
                    </button>
                  </div>
                  <div className="item-total">
                    <p>₹{(item.price * item.quantity).toFixed(2)}</p>
                    <button 
                      onClick={() => removeItem(item.productId)}
                      className="remove-btn"
                    >
                      Remove
                    </button>
                  </div>
                  <div className="item-actions">
                    <button 
                      className="buy-now-btn"
                      onClick={() => handleBuyNow(item)}
                    >
                      Buy Now
                    </button>
                  </div>
                </div>
              ))}
            </div>
            
            <div className="cart-summary">
              <div className="summary-row">
                <span>Total Items:</span>
                <span>{cartItems.reduce((total, item) => total + item.quantity, 0)}</span>
              </div>
              <div className="summary-row total">
                <span>Total Price:</span>
                <span>₹{calculateTotal().toFixed(2)}</span>
              </div>
              <button 
                className="buy-all-btn"
                onClick={handleBuyAll}
              >
                Buy All
              </button>
            </div>
          </>
        )}
      </div>
      
      {/* Order Popup */}
      {showOrderPopup && (
        <div className="popup-overlay">
          <div className="order-popup">
            {orderSuccess ? (
              <div className="order-success">
                <h2>Order Placed Successfully!</h2>
                <p>Thank you for your purchase.</p>
              </div>
            ) : (
              <>
                <h2>Order Details</h2>
                <div className="popup-items">
                  {orderDetails.products.map((item) => (
                    <div key={item.productId} className="popup-item">
                      <span>{item.name} x {item.quantity}</span>
                      <span>₹{(item.price * item.quantity).toFixed(2)}</span>
                    </div>
                  ))}
                </div>
                <div className="popup-total">
                  <span>Total:</span>
                  <span>₹{orderDetails.totalPrice.toFixed(2)}</span>
                </div>
                <div className="popup-actions">
                  <button 
                    className="cancel-btn"
                    onClick={() => setShowOrderPopup(false)}
                  >
                    Cancel
                  </button>
                  <button 
                    className="place-order-btn"
                    onClick={placeOrder}
                  >
                    Place Order
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default CartPage;