import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ProfileDropdown from '../components/ProfileDropdown';
import { ordersApi, productApi, ratingsApi } from '../utils/api';
import '../styles/SellerHome.css';

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

  const sellerId = 1;

  const calculateTotalRevenue = (orders) => {
    return orders.reduce((total, order) => {
      const product = products.find(p => p.productId === order.productId);
      const isSuccessOrder = order.orderStatus?.toLowerCase() === 'success';
      if (!product || !isSuccessOrder) return total;
      return total + product.productPrice * order.orderQuantity;
    }, 0);
  };

  const renderStars = (rating) => {
    const fullStars = Math.floor(rating);
    const halfStar = rating - fullStars >= 0.5;
    const stars = [];
    for (let i = 0; i < fullStars; i++) stars.push(<i key={`full-${i}`} className="fas fa-star text-warning"></i>);
    if (halfStar) stars.push(<i key="half" className="fas fa-star-half-alt text-warning"></i>);
    while (stars.length < 5) stars.push(<i key={`empty-${stars.length}`} className="far fa-star text-warning"></i>);
    return stars;
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const productsRes = await productApi.getProducts(sellerId);
        setProducts(productsRes.data);

        const ordersRes = await ordersApi.getSellerOrders(sellerId);
        setOrders(ordersRes.data.sort((a, b) => b.orderId - a.orderId));

        const ratings = {};
        for (let product of productsRes.data) {
          try {
            const res = await ratingsApi.getProductRatings(product.productId);
            const data = Array.isArray(res.data) ? res.data : [res.data];
            const avg = data.length ? (data.reduce((sum, r) => sum + r.ratingValue, 0) / data.length).toFixed(1) : null;
            ratings[product.productId] = {
              average: avg,
              count: data.length,
              feedbacks: data.map(r => r.feedback)
            };
          } catch {
            ratings[product.productId] = null;
          }
        }
        setProductRatings(ratings);
      } catch (err) {
        setError(`Failed to load data: ${err.response?.data?.message || err.message}`);
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
    const filtered = products.filter(product =>
      product.productName.toLowerCase().includes(value.toLowerCase())
    );
    setSuggestions(filtered);
    setShowSuggestions(true);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    if (suggestions.length > 0) handleSuggestionClick(suggestions[0].productId);
  };

  const handleSuggestionClick = (id) => {
    setSearchTerm('');
    setSuggestions([]);
    setShowSuggestions(false);
    navigate(`/product/${id}`);
  };

  if (loading) return <div className="loading-container"><div className="spinner-border text-primary" role="status"><span className="visually-hidden">Loading...</span></div></div>;

  if (error) return <div className="alert alert-danger m-4" role="alert">{error}</div>;

  const totalRevenue = calculateTotalRevenue(orders);

  return (
    <div className="home-page">
      <nav className="navbar navbar-light bg-white">
        <div className="container-fluid px-4">
          <Link to="/" className="text-decoration-none">
            <div className="logo text-dark d-flex align-items-center">
              <i className="fas fa-leaf text-success me-2 logo-icon"></i>
              <span className="fw-bold">FarmToHome</span>
            </div>
          </Link>
          <div className="search-container">
            <form className="input-group" onSubmit={handleSearchSubmit}>
              <input type="text" className="form-control border-end-0" placeholder="Search products..." value={searchTerm} onChange={handleSearchChange} onBlur={() => setTimeout(() => setShowSuggestions(false), 200)} onFocus={() => { if (searchTerm.trim() !== '') setShowSuggestions(true); }} />
              <button type="submit" className="btn btn-outline-secondary border-start-0"><i className="fas fa-search"></i></button>
            </form>
            {showSuggestions && suggestions.length > 0 && (
              <div className="suggestions-dropdown">
                {suggestions.map(product => (
                  <div key={product.productId} className="suggestion-item" onClick={() => handleSuggestionClick(product.productId)} onMouseDown={e => e.preventDefault()}>
                    <div className="d-flex justify-content-between">
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

      <div className="container-fluid px-4 py-4">
        <div className="dashboard-cards">
          <div className="col-md-4">
            <Link to="/view-orders" className="text-decoration-none">
              <div className="card bg-primary text-white h-100 d-flex flex-row justify-content-between align-items-center p-4">
                <div>
                  <h6 className="mb-1">Total Orders</h6>
                  <h2 className="mb-0">{orders.length}</h2>
                </div>
                <i className="fas fa-shopping-cart fa-2x opacity-75"></i>
              </div>
            </Link>
          </div>
          <div className="col-md-4">
            <Link to="/view-products" className="text-decoration-none">
              <div className="card bg-success text-white h-100 d-flex flex-row justify-content-between align-items-center p-4">
                <div>
                  <h6 className="mb-1">Total Products</h6>
                  <h2 className="mb-0">{products.length}</h2>
                </div>
                <i className="fas fa-box fa-2x opacity-75"></i>
              </div>
            </Link>
          </div>
          <div className="col-md-4">
            <div className="card bg-warning text-white h-100 d-flex flex-row justify-content-between align-items-center p-4">
              <div>
                <h6 className="mb-1">Total Revenue</h6>
                <h2 className="mb-0">₹{totalRevenue.toLocaleString('en-IN', { maximumFractionDigits: 2 })}</h2>
              </div>
              <i className="fas fa-rupee-sign fa-2x opacity-75"></i>
            </div>
          </div>
        </div>

        <div className="section-header">
          <h4 className="mb-0">My Products</h4>
          <Link to="/view-products" className="btn btn-link text-decoration-none">
            See All Products <i className="fas fa-arrow-right ms-1"></i>
          </Link>
        </div>
        <div className="products-grid">
          {products.slice(0, 3).map(product => (
            <div key={product.productId}>
              <div className="card h-100">
                {product.imageUrl && <img src={product.imageUrl} alt={product.productName} className="card-img-top product-image" />}
                <div className="card-body">
                  <h5 className="card-title">{product.productName}</h5>
                  <p className="text-muted mb-1">Stock remaining: {product.productQuantity}</p>
                  {product.productQuantity === 0 && <span className="badge bg-danger mb-2">Out of Stock</span>}
                  <p className="mb-1"><strong>Price: ₹{product.productPrice}</strong></p>
                  {productRatings[product.productId]?.average ? (
                    <>
                      <p className="mb-1">
                        {renderStars(productRatings[product.productId].average)}
                        <small className="text-muted ms-2">
                          ({productRatings[product.productId].average} / 5, {productRatings[product.productId].count} ratings)
                        </small>
                      </p>
                      <ul className="feedback-list ps-3">
                        {productRatings[product.productId].feedbacks.slice(0, 2).map((fb, i) => (
                          <li key={i} className="text-muted small">“{fb}”</li>
                        ))}
                      </ul>
                    </>
                  ) : (
                    <p className="text-muted">No Ratings</p>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="section-header mt-5">
          <h4 className="mb-0">Recent Orders</h4>
          <Link to="/view-orders" className="btn btn-link text-decoration-none">
            See All Orders <i className="fas fa-arrow-right ms-1"></i>
          </Link>
        </div>
        <div className="orders-grid">
          {orders.slice(0, 3).map(order => (
            <div key={order.orderId}>
              <div className="card h-100">
                <div className="card-body">
                  <p className="text-muted mb-1"><small>Order ID: {order.orderId}</small></p>
                  <h5 className="card-title">{order.productName || 'Product Name N/A'}</h5>
                  <p className="mb-1"><strong>Quantity: {order.orderQuantity}</strong></p>
                  <p className="text-muted mb-1"><small>Ordered by: {order.customerName}</small></p>
                  <p className="mt-2">
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
