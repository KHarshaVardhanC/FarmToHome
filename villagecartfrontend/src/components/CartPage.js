import React, { useEffect, useState } from 'react';
import '../styles/CartPage.css';

const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);

  // Load from localStorage on mount
  useEffect(() => {
    const storedCart = JSON.parse(localStorage.getItem("cart")) || [];
    setCartItems(storedCart);
  }, []);

  const handleQuantityChange = (id, delta) => {
    const updatedItems = cartItems.map(item =>
      item.productId === id
        ? { ...item, quantity: Math.max(1, (item.quantity || 1) + delta) }
        : item
    );
    setCartItems(updatedItems);
    localStorage.setItem("cart", JSON.stringify(updatedItems));
  };

  const handleRemoveFromCart = (id) => {
    const updatedItems = cartItems.filter(item => item.productId !== id);
    setCartItems(updatedItems);
    localStorage.setItem("cart", JSON.stringify(updatedItems));
  };

  const handleBuyNow = (id) => {
    const product = cartItems.find(item => item.productId === id);
    alert(`Buying ${product.quantity || 1} kg of ${product.name} for ₹${product.price * (product.quantity || 1)}`);
  };

  const handleBuyAll = () => {
    const total = cartItems.reduce((sum, item) => sum + item.price * (item.quantity || 1), 0);
    alert(`Buying all items for ₹${total}`);
  };

  return (
    <div className="cart-page">
      <h2>Your Cart</h2>
      {cartItems.length === 0 ? (
        <p>Your cart is empty.</p>
      ) : (
        cartItems.map(item => (
          <div className="cart-item" key={item.productId}>
            <img
              src={item.image|| `https://source.unsplash.com/100x100/?${item.productName}`}
              alt={item.name}
              className="product-img"
            />
            <div className="product-details">
            <p><strong>Product Name:</strong> {item.name}</p>
            <p><strong>Price:</strong> ₹{item.price}/kg</p>
              <div className="quantity-control">
                <strong>Quantity:</strong>
                <button onClick={() => handleQuantityChange(item.productId, -1)}>-</button>
                <span>{item.quantity || 1} kg</span>
                <button onClick={() => handleQuantityChange(item.productId, 1)}>+</button>
              </div>
            </div>
            <button className="buy-now" onClick={() => handleBuyNow(item.productId)}>Buy Now</button>
            <button className="remove-from-cart" onClick={() => handleRemoveFromCart(item.productId)}>Remove from Cart</button>
          </div>
        ))
      )}
      {cartItems.length > 0 && (
        <div className="buy-all-container">
          <button className="buy-all" onClick={handleBuyAll}>Buy All</button>
        </div>
      )}
    </div>
  );
};

export default CartPage;