import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { productApi, ordersApi, ratingsApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const Home = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  
  // Replace with actual seller ID from authentication
  const sellerId = 1;

  // Calculate total revenue from orders
  const calculateTotalRevenue = (orders) => {
    console.log('Calculating revenue for orders:', orders);
    return orders.reduce((total, order) => {
      console.log('Processing order:', order);
      // Find the corresponding product
      const product = products.find(p => p.productId === order.productId);
      console.log('Found product:', product);
      
      // Check if order status exists and is 'Success'
      const isSuccessOrder = order.orderStatus === 'Success' || order.orderStatus === 'success';
      console.log('Order status:', order.orderStatus, 'Is success:', isSuccessOrder);

      if (!product || !isSuccessOrder) {
        console.log('Skipping order - Product exists:', !!product, 'Is success:', isSuccessOrder);
        return total;
      }

      const orderRevenue = product.productPrice * order.orderQuantity;
      console.log('Order revenue:', orderRevenue);
      return total + orderRevenue;
    }, 0);
  };

  // Calculate average rating
  const calculateAverageRating = (ratings) => {
    if (!ratings.length) return 0;
    const sum = ratings.reduce((total, rating) => total + rating.ratingValue, 0);
    return (sum / ratings.length).toFixed(1);
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        // Fetch products
        console.log('Fetching products for seller:', sellerId);
        const productsResponse = await productApi.getProducts(sellerId);
        console.log('Products response:', productsResponse);
        setProducts(productsResponse.data);

        // Fetch orders
        console.log('Fetching orders for seller:', sellerId);
        const ordersResponse = await ordersApi.getSellerOrders(sellerId);
        console.log('Orders response:', ordersResponse);
        // Add debug log for order status
        console.log('Order statuses:', ordersResponse.data.map(order => order.orderStatus));
        const sortedOrders = ordersResponse.data.sort((a, b) => b.orderId - a.orderId);
        setOrders(sortedOrders);

        // Fetch ratings for all products
        console.log('Fetching ratings for seller:', sellerId);
        const ratingsResponse = await ratingsApi.getSellerRatings(sellerId);
        console.log('Ratings response:', ratingsResponse);
        setRatings(ratingsResponse.data);
      } catch (err) {
        console.error('Error details:', {
          message: err.message,
          response: err.response?.data,
          status: err.response?.status,
          endpoint: err.config?.url
        });
        setError(
          `Failed to load data: ${err.response?.data?.message || err.message}. 
           Please try again later.`
        );
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [sellerId]);

  // Handle search input change
  const handleSearchChange = (e) => {
    const value = e.target.value;
    setSearchTerm(value);
    
    if (value.trim() === '') {
      setSuggestions([]);
      setShowSuggestions(false);
      return;
    }

    // Filter products based on search term
    const filteredProducts = products.filter(product =>
      product.productName.toLowerCase().includes(value.toLowerCase())
    );
    
    setSuggestions(filteredProducts);
    setShowSuggestions(true);
  };

  // Handle suggestion click
  const handleSuggestionClick = (productId) => {
    setSearchTerm('');
    setSuggestions([]);
    setShowSuggestions(false);
    navigate(`/product/${productId}`);
  };

  // Handle search form submit
  const handleSearchSubmit = (e) => {
    e.preventDefault();
    if (suggestions.length > 0) {
      handleSuggestionClick(suggestions[0].productId);
    }
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger m-4" role="alert">
        {error}
      </div>
    );
  }

  const totalRevenue = calculateTotalRevenue(orders);
  const averageRating = calculateAverageRating(ratings);

  return (
    <div className="home-page">
      {/* Top Navigation */}
      <nav className="navbar navbar-light bg-white">
        <div className="container-fluid px-4">
          <div className="d-flex align-items-center">
            <Link to="/" className="text-decoration-none">
              <div className="logo text-dark d-flex align-items-center">
                <i className="fas fa-leaf text-success me-2" style={{ fontSize: '24px' }}></i>
                <span className="fw-bold">FarmToHome</span>
              </div>
            </Link>
          </div>

          <div className="d-flex flex-grow-1 mx-4 position-relative">
            <form className="input-group" onSubmit={handleSearchSubmit}>
              <input
                type="text"
                className="form-control border-end-0"
                placeholder="Search products..."
                value={searchTerm}
                onChange={handleSearchChange}
                onBlur={() => {
                  // Delay hiding suggestions to allow click events to fire
                  setTimeout(() => setShowSuggestions(false), 200);
                }}
                onFocus={() => {
                  if (searchTerm.trim() !== '') {
                    setShowSuggestions(true);
                  }
                }}
              />
              <button type="submit" className="btn btn-outline-secondary border-start-0">
                <i className="fas fa-search"></i>
              </button>
            </form>
            {showSuggestions && suggestions.length > 0 && (
              <div className="position-absolute top-100 start-0 end-0 mt-1 bg-white border rounded shadow-sm" style={{ zIndex: 1000 }}>
                {suggestions.map((product) => (
                  <div
                    key={product.productId}
                    className="p-2 border-bottom cursor-pointer hover-bg-light"
                    style={{ cursor: 'pointer' }}
                    onClick={() => handleSuggestionClick(product.productId)}
                    onMouseDown={(e) => e.preventDefault()} // Prevent onBlur from firing before click
                  >
                    <div className="d-flex justify-content-between align-items-center">
                      <span>{product.productName}</span>
                      <small className="text-muted">₹{product.productPrice}</small>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="d-flex align-items-center gap-3">
            <Link to="/add-product" className="btn btn-primary px-4">Add Product</Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container-fluid px-4 py-4">
        {/* Dashboard Cards */}
        <div className="row g-4 mb-4">
          <div className="col-md-3">
            <Link to="/view-orders" className="text-decoration-none">
              <div className="card bg-primary text-white h-100">
                <div className="card-body d-flex justify-content-between align-items-center">
                  <div>
                    <h6 className="card-title mb-1">Total Orders</h6>
                    <h2 className="mb-0">{orders.length}</h2>
                  </div>
                  <i className="fas fa-shopping-cart fa-2x opacity-50"></i>
                </div>
              </div>
            </Link>
          </div>
          <div className="col-md-3">
            <Link to="/view-products" className="text-decoration-none">
              <div className="card bg-success text-white h-100">
                <div className="card-body d-flex justify-content-between align-items-center">
                  <div>
                    <h6 className="card-title mb-1">Total Products</h6>
                    <h2 className="mb-0">{products.length}</h2>
                  </div>
                  <i className="fas fa-box fa-2x opacity-50"></i>
                </div>
              </div>
            </Link>
          </div>
          <div className="col-md-3">
            <div className="card bg-warning text-white h-100">
              <div className="card-body d-flex justify-content-between align-items-center">
                <div>
                  <h6 className="card-title mb-1">Total Revenue</h6>
                  <h2 className="mb-0">₹{totalRevenue.toLocaleString('en-IN', { maximumFractionDigits: 2 })}</h2>
                </div>
                <i className="fas fa-rupee-sign fa-2x opacity-50"></i>
              </div>
            </div>
          </div>
          <div className="col-md-3">
            <Link to="/view-ratings" className="text-decoration-none">
              <div className="card bg-info text-white h-100">
                <div className="card-body d-flex justify-content-between align-items-center">
                  <div>
                    <h6 className="card-title mb-1">Average Rating</h6>
                    <h2 className="mb-0">{averageRating} <small>/ 5</small></h2>
                  </div>
                  <i className="fas fa-star fa-2x opacity-50"></i>
                </div>
              </div>
            </Link>
          </div>
        </div>

        {/* My Products Section */}
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h4 className="mb-0">My Products</h4>
          <Link to="/view-products" className="btn btn-link text-decoration-none">
            See All Products <i className="fas fa-arrow-right ms-1"></i>
          </Link>
        </div>

        <div className="row g-4">
          {products.slice(0, 3).map((product) => (
            <div key={product.productId} className="col-md-4">
              <div className="card h-100">
                {product.imageUrl && (
                  <img 
                    src={product.imageUrl} 
                    className="card-img-top" 
                    alt={product.productName}
                    style={{ height: '200px', objectFit: 'cover' }}
                  />
                )}
                <div className="card-body">
                  <h5 className="card-title">{product.productName}</h5>
                  <p className="card-text mb-1">
                    <small className="text-muted">Stock remaining: {product.productQuantity}</small>
                  </p>
                  <p className="card-text mb-1">
                    <strong>Price: ₹{product.productPrice}</strong>
                  </p>
                  <p className="card-text">
                    <small className="text-muted">Location: {product.sellerPlace}, {product.sellerArea}</small>
                  </p>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* My Orders Section */}
        <div className="d-flex justify-content-between align-items-center mb-4 mt-5">
          <h4 className="mb-0">Recent Orders</h4>
          <Link to="/view-orders" className="btn btn-link text-decoration-none">
            See All Orders <i className="fas fa-arrow-right ms-1"></i>
          </Link>
        </div>

        <div className="row g-4">
          {orders.slice(0, 3).map((order) => (
            <div key={order.orderId} className="col-md-4">
              <div className="card h-100">
                <div className="card-body">
                  <p className="card-text mb-1">
                    <small className="text-muted">Order ID: {order.orderId}</small>
                  </p>
                  <h5 className="card-title">{order.productName || 'Product Name Not Available'}</h5>
                  <p className="card-text mb-1">
                    <strong>Quantity: {order.orderQuantity}</strong>
                  </p>
                  <p className="card-text mb-1">
                    <small className="text-muted">Ordered by: {order.customerName}</small>
                  </p>
                  <p className="card-text mt-2">
                    <span className={`badge ${order.orderStatus?.toLowerCase() === 'success' ? 'bg-success' : 'bg-warning'}`}>
                      Status: {order.orderStatus || 'In Cart'}
                    </span>
                  </p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Home; 