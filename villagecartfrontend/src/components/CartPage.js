import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from './CustomerNavbar';
import '../styles/CartPage.css';

function CartPage() {
  const [cartItems, setCartItems] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
    setCartItems(savedCart);
  }, []);

  const updateCart = (updatedCart) => {
    setCartItems(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
  };

  const increaseQuantity = (productId) => {
    const updatedCart = cartItems.map(item => 
      item.id === productId ? {...item, quantity: item.quantity + 1} : item
    );
    updateCart(updatedCart);
  };

  const decreaseQuantity = (productId) => {
    const updatedCart = cartItems.map(item => 
      item.id === productId && item.quantity > 1 
        ? {...item, quantity: item.quantity - 1} 
        : item
    );
    updateCart(updatedCart);
  };

  const removeFromCart = (productId) => {
    const updatedCart = cartItems.filter(item => item.id !== productId);
    updateCart(updatedCart);
  };

  const buyNow = (item) => {
    // Process single item order
    console.log("Buying now:", item);
    
    // Here you would typically call your order API
    // For now, just simulating an order
    const order = {
      items: [item],
      total: item.price * item.quantity,
      date: new Date().toISOString(),
      status: 'Processing'
    };
    
    // Save to orders in localStorage
    const orders = JSON.parse(localStorage.getItem('orders')) || [];
    orders.push(order);
    localStorage.setItem('orders', JSON.stringify(orders));
    
    // Remove item from cart
    removeFromCart(item.id);
    
    // Show confirmation and redirect
    alert('Order placed successfully!');
    navigate('/my-orders');
  };

  const buyAll = () => {
    if (cartItems.length === 0) {
      alert('Your cart is empty!');
      return;
    }
    
    // Process all items as one order
    console.log("Buying all items:", cartItems);
    
    // Calculate total
    const total = cartItems.reduce(
      (sum, item) => sum + (item.price * item.quantity), 0
    );
    
    // Create order
    const order = {
      items: [...cartItems],
      total: total,
      date: new Date().toISOString(),
      status: 'Processing'
    };
    
    // Save to orders in localStorage
    const orders = JSON.parse(localStorage.getItem('orders')) || [];
    orders.push(order);
    localStorage.setItem('orders', JSON.stringify(orders));
    
    // Clear cart
    updateCart([]);
    
    // Show confirmation and redirect
    alert('All items ordered successfully!');
    navigate('/my-orders');
  };

  const calculateTotal = () => {
    return cartItems.reduce(
      (total, item) => total + (item.price * item.quantity), 
      0
    ).toFixed(2);
  };

  return (
    <div className="cart-page">
      <Navbar cartCount={cartItems.reduce((total, item) => total + item.quantity, 0)} />
      
      <div className="cart-container">
        <h1>Your Cart</h1>
        
        {cartItems.length === 0 ? (
          <div className="empty-cart">
            <p>Your cart is empty</p>
            <button onClick={() => navigate('/')}>Continue Shopping</button>
          </div>
        ) : (
          <>
            <div className="cart-items">
              {cartItems.map((item) => (
                <div key={item.id} className="cart-item">
                  <div className="item-image">
                    <img src={item.image} alt={item.name} />
                  </div>
                  
                  <div className="item-details">
                    <h3>{item.name}</h3>
                    <p>{item.description}</p>
                    
                    <div className="quantity-control">
                      <button onClick={() => decreaseQuantity(item.id)}>-</button>
                      <span>{item.quantity}</span>
                      <button onClick={() => increaseQuantity(item.id)}>+</button>
                    </div>
                    
                    <div className="item-price">${(item.price * item.quantity).toFixed(2)}</div>
                    
                    <div className="item-actions">
                      <button 
                        className="buy-now-btn"
                        onClick={() => buyNow(item)}
                      >
                        Buy Now
                      </button>
                      <button 
                        className="remove-btn"
                        onClick={() => removeFromCart(item.id)}
                      >
                        Remove
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            
            <div className="cart-summary">
              <div className="cart-total">
                <span>Total:</span>
                <span>${calculateTotal()}</span>
              </div>
              
              <button 
                className="buy-all-btn"
                onClick={buyAll}
              >
                Buy All Items
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default CartPage;