import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/CartPage.css';

const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showOrderPopup, setShowOrderPopup] = useState(false);
  const [orderProducts, setOrderProducts] = useState([]);
  const [totalAmount, setTotalAmount] = useState(0);
  const [customerId, setCustomerId] = useState(null);
  const [userName, setUserName] = useState('');
  const [orderSuccess, setOrderSuccess] = useState(false);
  
  const navigate = useNavigate();

  useEffect(() => {
    const storedCustomerId = localStorage.getItem('customerId');
    const storedUserName = localStorage.getItem('userName');
    
    setCustomerId(storedCustomerId);
    setUserName(storedUserName || '');
    
    if (storedCustomerId) {
      fetchCartItems(storedCustomerId);
    } else {
      // Fallback to localStorage if customerId is not available
      try {
        const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
        setCartItems(savedCart);
        calculateTotal(savedCart);
        setLoading(false);
      } catch (e) {
        console.error('Error loading cart from localStorage', e);
        setCartItems([]);
        setLoading(false);
      }
    }
  }, []);

  const fetchCartItems = async (id) => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:8080/order/orders/incart/${id}`);
      const items = response.data || [];
      setCartItems(items);
      calculateTotal(items);
    } catch (err) {
      console.error('Error fetching cart items:', err);
      setError('Failed to load your cart. Please try again.');
      
      // Fallback to localStorage
      try {
        const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
        setCartItems(savedCart);
        calculateTotal(savedCart);
      } catch (e) {
        console.warn('Invalid cart data in localStorage:', e);
        setCartItems([]);
      }
    } finally {
      setLoading(false);
    }
  };

  const calculateTotal = (items) => {
    const total = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    setTotalAmount(total);
  };

  const updateQuantity = (index, newQuantity) => {
    if (newQuantity < 1) return;
    
    const updatedCart = [...cartItems];
    updatedCart[index].quantity = newQuantity;
    
    setCartItems(updatedCart);
    calculateTotal(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
    
    // Update cart in backend if customerId is available
    if (customerId) {
      updateCartInBackend(updatedCart);
    }
  };

  const updateCartInBackend = async (cart) => {
    try {
      await axios.post(`http://localhost:8080/order/updateCart/${customerId}`, cart);
    } catch (err) {
      console.error('Error updating cart in backend:', err);
    }
  };

  const removeFromCart = (index) => {
    const updatedCart = cartItems.filter((_, i) => i !== index);
    setCartItems(updatedCart);
    calculateTotal(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
    
    // Update cart in backend if customerId is available
    if (customerId) {
      updateCartInBackend(updatedCart);
    }
  };

  const handleBuyNow = (item, index) => {
    setOrderProducts([item]);
    setShowOrderPopup(true);
  };

  const handleBuyAll = () => {
    if (cartItems.length === 0) return;
    
    setOrderProducts([...cartItems]);
    setShowOrderPopup(true);
  };

  const handlePlaceOrder = async () => {
    if (!customerId) {
      setError('Please log in to place an order');
      return;
    }
    
    try {
      const orderData = {
        customerId: customerId,
        products: orderProducts.map(item => ({
          productId: item.productId,
          quantity: item.quantity,
          price: item.price
        })),
        totalAmount: orderProducts.reduce((sum, item) => sum + (item.price * item.quantity), 0),
        status: 'PLACED'
      };
      
      const response = await axios.post('http://localhost:8080/order/place', orderData);
      
      if (response.data) {
        // If ordering all products, clear the cart
        if (orderProducts.length === cartItems.length) {
          setCartItems([]);
          localStorage.setItem('cart', JSON.stringify([]));
          if (customerId) {
            updateCartInBackend([]);
          }
        } else {
          // Remove only the ordered products from cart
          const productIds = orderProducts.map(p => p.productId);
          const updatedCart = cartItems.filter(item => !productIds.includes(item.productId));
          setCartItems(updatedCart);
          localStorage.setItem('cart', JSON.stringify(updatedCart));
          if (customerId) {
            updateCartInBackend(updatedCart);
          }
        }
        
        setOrderSuccess(true);
        
        // Reset after 3 seconds
        setTimeout(() => {
          setShowOrderPopup(false);
          setOrderSuccess(false);
          calculateTotal([]);
        }, 3000);
      }
    } catch (err) {
      console.error('Error placing order:', err);
      setError('Failed to place your order. Please try again.');
    }
  };

  return (
    <div className="cart-page">
      <Navbar 
        cartCount={cartItems.reduce((total, item) => total + item.quantity, 0)} 
        userName={userName}
      />
      
      <div className="cart-container">
        <h1>Your Shopping Cart</h1>
        
        {loading && <div className="loading">Loading your cart...</div>}
        
        {error && <div className="error-message">{error}</div>}
        
        {!loading && cartItems.length === 0 && (
          <div className="empty-cart">
            <h2>Your cart is empty</h2>
            <p>Add some products to your cart from our collection.</p>
            <button 
              className="continue-shopping-btn"
              onClick={() => navigate('/customer-home')}
            >
              Continue Shopping
            </button>
          </div>
        )}
        
        {cartItems.length > 0 && (
          <>
            <div className="cart-items">
              {cartItems.map((item, index) => (
                <div key={index} className="cart-item">
                  <div className="item-image">
                    <img src={item.image} alt={item.name} />
                  </div>
                  
                  <div className="item-details">
                    <h3>{item.name}</h3>
                    <p className="item-price">₹{item.price}/kg</p>
                    
                    <div className="quantity-control">
                      <button 
                        onClick={() => updateQuantity(index, item.quantity - 1)}
                        disabled={item.quantity <= 1}
                      >
                        -
                      </button>
                      <span>{item.quantity}</span>
                      <button onClick={() => updateQuantity(index, item.quantity + 1)}>
                        +
                      </button>
                    </div>
                    
                    <p className="item-subtotal">
                      Subtotal: ₹{(item.price * item.quantity).toFixed(2)}
                    </p>
                    
                    <div className="item-actions">
                      <button 
                        className="remove-btn"
                        onClick={() => removeFromCart(index)}
                      >
                        Remove
                      </button>
                      <button 
                        className="buy-now-btn"
                        onClick={() => handleBuyNow(item, index)}
                      >
                        Buy Now
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            
            <div className="cart-summary">
              <div className="summary-details">
                <h3>Order Summary</h3>
                <div className="summary-row">
                  <span>Items ({cartItems.reduce((total, item) => total + item.quantity, 0)}):</span>
                  <span>₹{totalAmount.toFixed(2)}</span>
                </div>
                <div className="summary-row">
                  <span>Shipping:</span>
                  <span>Free</span>
                </div>
                <div className="summary-total">
                  <span>Total:</span>
                  <span>₹{totalAmount.toFixed(2)}</span>
                </div>
              </div>
              
              <button 
                className="buy-all-btn"
                onClick={handleBuyAll}
              >
                Buy All Items
              </button>
              
              <button 
                className="continue-shopping-btn"
                onClick={() => navigate('/customer-home')}
              >
                Continue Shopping
              </button>
            </div>
          </>
        )}
      </div>
      
      {showOrderPopup && (
        <div className="order-popup-overlay">
          <div className="order-popup">
            <button 
              className="close-popup"
              onClick={() => {
                setShowOrderPopup(false);
                setOrderSuccess(false);
              }}
            >
              ×
            </button>
            
            {orderSuccess ? (
              <div className="order-success">
                <h2>Order Placed Successfully!</h2>
                <p>Your order has been placed and will be processed soon.</p>
                <p>You can check your order status in the "My Orders" section.</p>
              </div>
            ) : (
              <>
                <h2>Confirm Your Order</h2>
                
                <div className="order-items">
                  {orderProducts.map((item, index) => (
                    <div key={index} className="order-item">
                      <h4>{item.name}</h4>
                      <p>Quantity: {item.quantity}</p>
                      <p>Price: ₹{item.price}/kg</p>
                      <p>Subtotal: ₹{(item.price * item.quantity).toFixed(2)}</p>
                    </div>
                  ))}
                </div>
                
                <div className="order-total">
                  <h3>Total Amount: ₹{orderProducts.reduce((sum, item) => sum + (item.price * item.quantity), 0).toFixed(2)}</h3>
                </div>
                
                <button 
                  className="place-order-btn"
                  onClick={handlePlaceOrder}
                >
                  Place Order
                </button>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default CartPage;