import { ordersApi, productApi, ratingsApi } from '../utils/api';
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ProfileDropdown from '../components/ProfileDropdown';

const Home = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [productRatings, setProductRatings] = useState({});


  const sellerId = 2; // Replace with actual seller ID from authentication

  const calculateTotalRevenue = (orders) => {
    return orders.reduce((total, order) => {
      const product = products.find(p => p.productId === order.productId);
      const isSuccessOrder = order.orderStatus === 'Success' || order.orderStatus === 'success';
      if (!product || !isSuccessOrder) return total;
      const orderRevenue = product.productPrice * order.orderQuantity;
      return total + orderRevenue;
    }, 0);
  };

  // useEffect(() => {
  //   const fetchData = async () => {
  //     try {
  //       setLoading(true);
  //       const productsResponse = await productApi.getProducts(sellerId);
  //       setProducts(productsResponse.data);

  //       const ordersResponse = await ordersApi.getSellerOrders(sellerId);
  //       const sortedOrders = ordersResponse.data.sort((a, b) => b.orderId - a.orderId);
  //       setOrders(sortedOrders);
  //     } catch (err) {
  //       setError(
  //         `Failed to load data: ${err.response?.data?.message || err.message}. 
  //          Please try again later.`
  //       );
  //     } finally {
  //       setLoading(false);
  //     }
  //   };

  //   fetchData();
  // }, [sellerId]);
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
  
        // 1. Get all products
        const productsRes = await productApi.getProducts(sellerId);
        const products = productsRes.data;
        setProducts(products);
  
        // 2. Get all orders
        const ordersRes = await ordersApi.getSellerOrders(sellerId);
        const sortedOrders = ordersRes.data.sort((a, b) => b.orderId - a.orderId);
        setOrders(sortedOrders);
  
        // 3. Get ratings for each product
        const ratingMap = {};
        for (let product of products) {
          try {
            const res = await ratingsApi.getProductRatings(product.productId);
            const data = Array.isArray(res.data) ? res.data : [res.data];
            const avg = data.length ? (data.reduce((sum, r) => sum + r.ratingValue, 0) / data.length).toFixed(1) : null;
            ratingMap[product.productId] = avg;
          } catch {
            ratingMap[product.productId] = null;
          }
        }
        setProductRatings(ratingMap);
  
      } catch (err) {
        setError('Failed to load products or ratings');
      } finally {
        setLoading(false);
      }
    };
  
    fetchData();
  }, [sellerId]);
  

  const handleSearchChange = (e) => {
    const value = e.target.value;
    setSearchTerm(value);

    if (value.trim() === '') {
      setSuggestions([]);
      setShowSuggestions(false);
      return;
    }

    const filteredProducts = products.filter(product =>
      product.productName.toLowerCase().includes(value.toLowerCase())
    );

    setSuggestions(filteredProducts);
    setShowSuggestions(true);
  };

  const handleSuggestionClick = (productId) => {
    setSearchTerm('');
    setSuggestions([]);
    setShowSuggestions(false);
    navigate(`/product/${productId}`);
  };

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

  const renderStars = (rating) => {
    const fullStars = Math.floor(rating);
    const halfStar = rating - fullStars >= 0.5;
    const stars = [];
  
    for (let i = 0; i < fullStars; i++) stars.push(<i key={`full-${i}`} className="fas fa-star text-warning"></i>);
    if (halfStar) stars.push(<i key="half" className="fas fa-star-half-alt text-warning"></i>);
    while (stars.length < 5) stars.push(<i key={`empty-${stars.length}`} className="far fa-star text-warning"></i>);
  
    return stars;
  };
  
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
                onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
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
                    onMouseDown={(e) => e.preventDefault()}
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
          <div className="col-md-4">
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
          <div className="col-md-4">
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
          <div className="col-md-4">
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
                  {productRatings[product.productId] ? (
  <p className="card-text mb-1">
    {renderStars(productRatings[product.productId])}
    <small className="text-muted ms-2">({productRatings[product.productId]} / 5)</small>
  </p>
) : (
  <p className="card-text text-muted">No Ratings</p>
)}

                  {/* <p className="card-text">
                    <small className="text-muted">Location: {product.sellerPlace}, {product.sellerArea}</small>
                  </p> */}
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
