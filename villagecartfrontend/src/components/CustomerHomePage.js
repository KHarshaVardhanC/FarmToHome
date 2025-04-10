import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/CustomerHomePage.css';

function CustomerHomePage() {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]); // For search functionality
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedProduct, setSelectedProduct] = useState(null); // For the popup
  const [searchTerm, setSearchTerm] = useState(''); // For search input

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
      setFilteredProducts(response.data); // Initialize filtered products
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

  const handleProductClick = async (productName) => {
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

  const handleSearch = (term) => {
    setSearchTerm(term);
    const filtered = products.filter(product =>
      product.productName.toLowerCase().includes(term.toLowerCase())
    );
    setFilteredProducts(filtered);
  };

  return (
    <div className="customer-home">
      <Navbar
        cartCount={cartItems.reduce((total, item) => total + item.quantity, 0)}
        searchTerm={searchTerm}
        onSearch={handleSearch} // Pass the search handler to Navbar
      />

      <div className="content-container">
        {loading && <div className="loading">Loading products...</div>}

        {error && (
          <div className="error-message">
            <p>Error loading products: {error}</p>
            <button onClick={fetchProducts} className="retry-btn">Try Again</button>
          </div>
        )}

        {!loading && !error && filteredProducts.length === 0 && (
          <div className="no-products">No products available right now.</div>
        )}

        <div className="products-grid">
          {filteredProducts.map(product => (
            <div
              key={product.productId}
              className="product-card"
              onClick={() => handleProductClick(product.productName)} // Make the product clickable
            >
              <div className="product-image">
                <img src={product.imageUrl} alt={product.productName} />
              </div>
              <div className="product-info">
                <h3>{product.productName}</h3>
                <p>{product.productDescription}</p>
                <p>Price: ₹{product.productPrice}/kg</p>
                <button className="add-to-cart-btn" onClick={(e) => {
                  e.stopPropagation(); // Prevent triggering the product click
                  addToCart(product);
                }}>
                  Add to Cart
                </button>
              </div>
            </div>
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
            <p>Price:₹ {selectedProduct.productPrice || 'N/A'}</p>
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