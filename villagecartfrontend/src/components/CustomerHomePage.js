import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Slider from 'react-slick';
import Navbar from './CustomerNavbar';
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

  const categories = [
    {  image: '/images/vegetables.jpg' },
    {  image: '/images/fruits.jpg' },
    { image: '/images/dairy.jpg' },
    {  image: '/images/grains.jpg' },
  ];

  useEffect(() => {
    fetchProducts();

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
      setFilteredProducts(response.data);
    } catch (err) {
      console.error('Error fetching products:', err);
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchCategoryProducts = async (category) => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:8080/products/${category}`);
      setFilteredProducts(response.data);
      setSelectedCategory(category);
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
  };

  const handleSearch = (term) => {
    setSearchTerm(term);
    const filtered = products.filter((product) =>
      product.productName.toLowerCase().includes(term.toLowerCase())
    );
    setFilteredProducts(filtered);
  };

  const carouselSettings = {
    dots: true,
    infinite: true,
    speed: 1000,
    slidesToShow: 1, // Show only one category at a time
    slidesToScroll: 1,
    autoplay: true, // Enable automatic sliding
    autoplaySpeed: 1500, // Set the interval for sliding
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

      <div className="category-carousel">
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
            <div key={product.productId} className="product-card">
              <div className="product-image">
                <img src={product.imageUrl} alt={product.productName} />
              </div>
              <div className="product-info">
                <h3>{product.productName}</h3>
                <p>{product.productDescription}</p>
                <p>Price: ₹{product.productPrice}/kg</p>
                <button
                  className="add-to-cart-btn"
                  onClick={() => addToCart(product)}
                >
                  Add to Cart
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default CustomerHomePage;