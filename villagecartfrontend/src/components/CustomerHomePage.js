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

  useEffect(() => {
    const fetchCustomerDetails = async () => {
      const id = localStorage.getItem('customerId');
      if (id) {
        try {
          const response = await axios.get(`http://localhost:8080/customer/${id}`);
          if (response.data && response.data.name) {
            localStorage.setItem('userName', response.data.name);
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

    const id = localStorage.getItem('customerId');
    if (id) {
      fetchCustomerCart(id);
    } else {
      try {
        const savedCart = JSON.parse(localStorage.getItem('cart')) || [];
        setCartItems(savedCart);
      } catch (e) {
        localStorage.removeItem('cart');
        setCartItems([]);
      }
    }
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const response = await axios.get('http://localhost:8080/products');
      setProducts(response.data);
      setFilteredProducts(response.data);
    } catch (err) {
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
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const addToCart = async (product) => {
    const customerId = localStorage.getItem("customerId");
    
    // First check if product already exists in cart
    const existingCartItems = [...cartItems];
    const existingItemIndex = existingCartItems.findIndex(
      item => item.productId === product.productId
    );
    
    if (existingItemIndex >= 0) {
      // Product exists in cart - update quantity instead of adding new item
      try {
        // Get the existing order ID
        const orderId = existingCartItems[existingItemIndex].orderId;
        const newQuantity = existingCartItems[existingItemIndex].orderQuantity + 1;
        
        // Update the quantity on the server
        const response = await axios.put(`http://localhost:8080/order/updateQuantity/${orderId}`, {
          orderQuantity: newQuantity
        });
        
        if (response.status === 200) {
          // Update local state
          const updatedCartItems = [...existingCartItems];
          updatedCartItems[existingItemIndex].orderQuantity = newQuantity;
          setCartItems(updatedCartItems);
          
          // Also update localStorage
          localStorage.setItem("cart", JSON.stringify(updatedCartItems));
          
          alert("Item quantity updated in cart!");
        }
      } catch (error) {
        console.error("Error updating cart item quantity:", error);
      }
    } else {
      // Product doesn't exist in cart - add new item
      const orderData = {
        productId: product.productId,
        orderQuantity: 1, // Set default to 1kg
        customerId: parseInt(customerId),
        orderStatus: "IN_CART"
      };

      try {
        const response = await fetch("http://localhost:8080/order/add", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(orderData)
        });

        if (response.ok) {
          const result = await response.json();
          const orderId = result.orderId;

          // Create a new cart item with full product details for UI
          const newCartItem = {
            orderId,
            productId: product.productId,
            productName: product.productName,
            productPrice: product.productPrice,
            imageUrl: product.imageUrl,
            orderQuantity: 1,
            orderStatus: "IN_CART"
          };

          // Update the state with the new item
          const updatedCartItems = [...cartItems, newCartItem];
          setCartItems(updatedCartItems);

          // Update localStorage
          localStorage.setItem("cart", JSON.stringify(updatedCartItems));

          alert("Item added to cart!");
        } else {
          console.error("Failed to add to cart");
        }
      } catch (error) {
        console.error("Error adding to cart:", error);
      }
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
        cartCount={cartItems.length}
        searchTerm={searchTerm}
        onSearch={handleSearch}
        userName={localStorage.getItem('userName')}
      />

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
                    e.stopPropagation();
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