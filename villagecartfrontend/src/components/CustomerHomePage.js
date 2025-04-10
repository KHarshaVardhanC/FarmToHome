import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import ProductCard from './ProductCard';
import '../styles/CustomerHomePage.css';

function CustomerHomePage() {
  const [products, setProducts] = useState([]);
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedProduct, setSelectedProduct] = useState(null); // For the popup

  useEffect(() => {
    fetchProducts();

    // Load cart from localStorage safely
    try {
      const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
      setCartItems(savedCart);
    } catch (e) {
      console.warn('Invalid cart data in localStorage:', e);
      localStorage.removeItem('cart');
      setCartItems([]);
    }
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const response = await axios.get('http://localhost:8080/allproducts');
      setProducts(response.data);
    } catch (err) {
      console.error('Error fetching products:', err);
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const addToCart = (product) => {
    const existingItemIndex = cartItems.findIndex(item => item.id === product.id);

    let updatedCart;
    if (existingItemIndex !== -1) {
      updatedCart = [...cartItems];
      updatedCart[existingItemIndex].quantity += 1;
    } else {
      updatedCart = [...cartItems, { ...product, quantity: 1 }];
    }

    setCartItems(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
  };

  const handleKnowMore = async (productName) => {
    try {
      const response = await axios.get(`http://localhost:8080/product1/${productName}`);
      if (response.data && response.data.length > 0) {
        setSelectedProduct(response.data[0]); // Use the first product in the array
      } else {
        setError('Product details not found.');
      }
    } catch (err) {
      console.error('Error fetching product details:', err);
      setError('Failed to fetch product details.');
    }
  };

  const closePopup = () => {
    setSelectedProduct(null); // Close the popup
  };

  return (
    <div className="customer-home">
      <Navbar cartCount={cartItems.reduce((total, item) => total + item.quantity, 0)} />

      <div className="content-container">
        {loading && <div className="loading">Loading products...</div>}

        {error && (
          <div className="error-message">
            <p>Error loading products: {error}</p>
            <button onClick={fetchProducts} className="retry-btn">Try Again</button>
          </div>
        )}

        {!loading && !error && products.length === 0 && (
          <div className="no-products">No products available right now.</div>
        )}

        <div className="products-grid">
          {products.map(product => (
            <ProductCard
              key={product.productId}
              product={product}
              onAddToCart={() => addToCart(product)}
              onKnowMore={() => handleKnowMore(product.productName)} // Pass the productName
            />
          ))}
        </div>
      </div>

      {selectedProduct && (
        <div className="popup">
          <div className="popup-content">
            <button className="close-btn" onClick={closePopup}>X</button>
            <h2>{selectedProduct.productName || 'N/A'}</h2>
            <img src={selectedProduct.imageUrl || ''} alt={selectedProduct.productName || 'Product'} />
            <p>{selectedProduct.productDescription || 'N/A'}</p>
            <p>Price: ${selectedProduct.productPrice || 'N/A'}</p>
            <p>Quantity: {selectedProduct.productQuantity || 'N/A'}</p>
            <h3>Seller Details</h3>
            <p>Name: {selectedProduct.sellerName || 'N/A'}</p>
            <p>Location: {selectedProduct.sellerPlace || 'N/A'}, {selectedProduct.sellerCity || 'N/A'}</p>
          </div>
        </div>
      )}
    </div>
  );
}

export default CustomerHomePage;