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
      // Filter out products with 0 quantity
      const availableProducts = response.data.filter(product => product.productQuantity > 0);
      setProducts(response.data); // Keep all products in state for filtering
      setFilteredProducts(availableProducts); // Only show available products by default
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchCustomerCart = async (id) => {
    try {
      const response = await axios.get(`http://localhost:8080/order/orders/incart/${id}`);
      // Store the cart items returned from the API
      setCartItems(response.data);
    } catch (err) {
      console.error('Error fetching cart:', err);
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
      // Filter out products with 0 quantity
      const availableProducts = response.data.filter(product => product.productQuantity > 0);
      setFilteredProducts(availableProducts);
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };

  const addToCart = async (product) => {
    // Check if product is in stock
    if (product.productQuantity <= 0) {
      alert("Sorry, this product is out of stock");
      return;
    }
    
    const customerId = localStorage.getItem("customerId");
    
    // First check if product already exists in cart
    const existingCartItems = [...cartItems];
    const existingItemIndex = existingCartItems.findIndex(
      item => item.productId === product.productId || 
              (item.orderId && item.orderStatus && 
               item.orderStatus.toLowerCase() === "incart" && 
               parseInt(item.productId) === parseInt(product.productId))
    );
    
    if (existingItemIndex >= 0) {
      // Product exists in cart - update quantity instead of adding new item
      try {
        // Get the existing order ID
        const orderId = existingCartItems[existingItemIndex].orderId;
        const newQuantity = existingCartItems[existingItemIndex].orderQuantity + 1;
        
        // Check if requested quantity is available
        if (newQuantity > product.productQuantity) {
          alert(`Sorry, only ${product.productQuantity} kg available in stock`);
          return;
        }
        
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
          if (!customerId) {
            localStorage.setItem("cart", JSON.stringify(updatedCartItems));
          }
        }
      } catch (error) {
        console.error("Error updating cart item quantity:", error);
      }
    } else {
      // Product doesn't exist in cart - add new item
      const orderData = {
        productId: product.productId,
        orderQuantity: 1, // Set default to 1kg
        customerId: customerId ? parseInt(customerId) : null,
        orderStatus: "IN_CART"
      };

      try {
        const response = await axios.post("http://localhost:8080/order/add", orderData);

        if (response.status === 200 || response.status === 201) {
          const result = response.data;
          const orderId = result.orderId;

          // Create a new cart item with full product details for UI
          const newCartItem = {
            orderId,
            productId: product.productId,
            productName: product.productName,
            productPrice: product.productPrice,
            imageUrl: product.imageUrl,
            orderQuantity: 1,
            orderStatus: "Incart" // Match the case from API response
          };

          // Update the state with the new item
          const updatedCartItems = [...cartItems, newCartItem];
          setCartItems(updatedCartItems);

          // Update localStorage for non-logged in users
          if (!customerId) {
            localStorage.setItem("cart", JSON.stringify(updatedCartItems));
          }

        } else {
          console.error("Failed to add to cart");
          alert("Failed to add item to cart. Please try again.");
        }
      } catch (error) {
        console.error("Error adding to cart:", error);
        alert("Error adding item to cart. Please try again.");
      }
    }
  };

  const handleSearch = (term) => {
    setSearchTerm(term);
    if (term.trim() === '') {
      // When search is cleared, show only in-stock products
      const availableProducts = products.filter(product => product.productQuantity > 0);
      setFilteredProducts(availableProducts);
    } else {
      // When searching, show all matching products regardless of stock
      const filtered = products.filter((product) =>
        product.productName.toLowerCase().includes(term.toLowerCase())
      );
      setFilteredProducts(filtered);
    }
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

  // Function to determine if a product has low stock (less than 5 kg)
  const isLowStock = (quantity) => {
    return quantity > 0 && quantity < 5;
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
              className={`product-card ${product.productQuantity === 0 ? 'out-of-stock' : ''}`}
              onClick={() => handleProductClick(product)}
            >
              <div className="product-image">
                <img src={product.imageUrl} alt={product.productName} />
                {product.productQuantity === 0 && (
                  <div className="out-of-stock-overlay">Out of Stock</div>
                )}
              </div>
              <div className="product-info">
                <h3>{product.productName}</h3>
                <p>{product.productDescription}</p>
                <p className="price">₹{product.productPrice}</p>
                
                {/* Display stock information */}
                {product.productQuantity > 0 && (
                  <p className={`product-quantity ${isLowStock(product.productQuantity) ? 'low-stock' : ''}`}>
                    {isLowStock(product.productQuantity) ? 'Only ' : ''}
                    {product.productQuantity} kg available
                  </p>
                )}
                
                <button
                  className={`add-to-cart-btn ${product.productQuantity === 0 ? 'disabled' : ''}`}
                  onClick={(e) => {
                    e.stopPropagation();
                    if (product.productQuantity > 0) {
                      addToCart(product);
                    }
                  }}
                  disabled={product.productQuantity === 0}
                >
                  {product.productQuantity > 0 ? 'Add to Cart' : 'Out of Stock'}
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
            if (selectedProduct.productQuantity > 0) {
              addToCart(selectedProduct);
              closeModal();
            }
          }}
        />
      )}
    </div>
  );
}

export default CustomerHomePage;