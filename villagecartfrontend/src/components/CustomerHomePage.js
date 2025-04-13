import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Slider from 'react-slick';
import Navbar from './CustomerNavbar';
import ProductDetailModal from './ProductDetailModal';
import '../styles/CustomerHomePage.css';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

function CustomerHomePage() {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [customerId, setCustomerId] = useState('');
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [productDetails, setProductDetails] = useState(null);

  const categories = [
    { name: 'Vegetables', image: '/images/vegetables.jpg' },
    { name: 'Fruits', image: '/images/fruits.jpg' },
    { name: 'Dairy', image: '/images/dairy.jpg' },
    { name: 'Grains', image: '/images/grains.jpg' },
  ];

  // In CustomerHomePage.js
useEffect(() => {
  // Fetch customer details after login
  const fetchCustomerDetails = async () => {
    const id = localStorage.getItem('customerId');
    console.log("Customer ID from localStorage:", id);
    
    if (id) {
      try {
        const response = await axios.get(`http://localhost:8080/customer/${id}`);
        console.log("Customer details response:", response.data);
        
        // Save customer name in localStorage for navbar
        if (response.data && response.data.name) {
          localStorage.setItem('userName', response.data.name);
          // Force a refresh of the navbar
          window.dispatchEvent(new Event('storage'));
        }
        setCustomerId(id);
      } catch (err) {
        console.error('Error fetching customer details:', err);
      }
    }
  };

  fetchCustomerDetails();
  fetchProducts();

  // Get cart items
  const id = localStorage.getItem('customerId');
  if (id) {
    fetchCustomerCart(id);
  } else {
    try {
      const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
      setCartItems(savedCart);
    } catch (e) {
      console.warn('Invalid cart data in localStorage:', e);
      localStorage.removeItem('cart');
      setCartItems([]);
    }
  }
}, []);

  // Rest of component remains the same...
  // ... (fetchProducts, fetchCustomerCart, etc.)

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const response = await axios.get('http://localhost:8080/products');
      setProducts(response.data);
      setFilteredProducts(response.data);
    } catch (err) {
      console.error('Error fetching products:', err);
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchCustomerCart = async (id) => {
    try {
      const response = await axios.get(`http://localhost:8080/order/orders/incart/${id}`);
      setCartItems(response.data);
    } catch (err) {
      console.error('Error fetching customer cart:', err);
      // If API fails, fallback to localStorage
      try {
        const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
        setCartItems(savedCart);
      } catch (e) {
        setCartItems([]);
      }
    }
  };

  const fetchCategoryProducts = async (category) => {
    try {
      setLoading(true);
      setSelectedCategory(category);
      const response = await axios.get(`http://localhost:8080/products/${category}`);
      setFilteredProducts(response.data);
    } catch (err) {
      console.error('Error fetching category products:', err);
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const addToCart = (product) => {
    const existingItemIndex = cartItems.findIndex(
      (item) => item.productId === product.productId
    );

    let updatedCart;
    if (existingItemIndex !== -1) {
      updatedCart = [...cartItems];
      updatedCart[existingItemIndex].quantity += 1;
    } else {
      const cartItem = {
        productId: product.productId,
        name: product.productName,
        price: product.productPrice,
        image: product.imageUrl,
        quantity: 1,
      };
      updatedCart = [...cartItems, cartItem];
    }

    setCartItems(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
    
    // If customer is logged in, update cart in backend too
    if (customerId) {
      // This would typically be an API call to update the cart in the backend
    }
  };

  const handleSearch = (term) => {
    setSearchTerm(term);
    const filtered = products.filter((product) =>
      product.productName.toLowerCase().includes(term.toLowerCase())
    );
    setFilteredProducts(filtered);
  };

  const handleProductClick = async (product) => {
    setSelectedProduct(product);
    
    try {
      // Fetch additional product details including seller info
      const response = await axios.get(`http://localhost:8080/product2/${product.productId}`);
      setProductDetails(response.data);
      setShowModal(true);
    } catch (error) {
      console.error('Error fetching product details:', error);
    }
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedProduct(null);
    setProductDetails(null);
  };

  const carouselSettings = {
    dots: true,
    infinite: true,
    speed: 1000,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 2000,
    responsive: [
      {
        breakpoint: 768,
        settings: {
          slidesToShow: 1,
        },
      },
    ],
  };

  return (
    <div className="customer-home">
      <Navbar
        cartCount={cartItems.reduce((total, item) => total + item.quantity, 0)}
        searchTerm={searchTerm}
        onSearch={handleSearch}
      />

      {/* Enlarged category carousel */}
      <div className="category-carousel-container">
        <Slider {...carouselSettings}>
          {categories.map((category) => (
            <div
              key={category.name}
              className="category-card"
              onClick={() => fetchCategoryProducts(category.name)}
            >
              <img src={category.image} alt={category.name} className="category-image" />
              <h3 className="category-name">{category.name}</h3>
            </div>
          ))}
        </Slider>
      </div>

      {/* Rest of component remains the same... */}
      {selectedCategory && (
        <div className="selected-category">
          <h2>{selectedCategory}</h2>
        </div>
      )}

      <div className="content-container">
        {loading && <div className="loading">Loading products...</div>}

        {error && (
          <div className="error-message">
            <p>Error loading products: {error}</p>
            <button onClick={fetchProducts} className="retry-btn">
              Try Again
            </button>
          </div>
        )}

        {!loading && !error && filteredProducts.length === 0 && (
          <div className="no-products">No products available in this category.</div>
        )}

        <div className="products-grid">
          {filteredProducts.map((product) => (
            <div 
              key={product.productId} 
              className="product-card" 
              onClick={() => handleProductClick(product)}
            >
              <div className="product-image">
                <img src={product.imageUrl} alt={product.productName} />
              </div>
              <div className="product-info">
                <h3>{product.productName}</h3>
                <p>{product.productDescription}</p>
                <p className="price">₹{product.productPrice}/kg</p>
                <button
                  className="add-to-cart-btn"
                  onClick={(e) => {
                    e.stopPropagation(); // Prevent triggering the card click
                    addToCart(product);
                  }}
                >
                  Add to Cart
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Product Detail Modal */}
      {showModal && selectedProduct && productDetails && (
        <ProductDetailModal 
          product={selectedProduct}
          productDetails={productDetails}
          onClose={closeModal}
          onAddToCart={() => {
            addToCart(selectedProduct);
            closeModal();
          }}
        />
      )}
    </div>
  );
}

export default CustomerHomePage;