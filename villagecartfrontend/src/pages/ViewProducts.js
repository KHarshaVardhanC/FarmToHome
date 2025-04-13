import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { productApi, ratingsApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import '../styles/ViewProducts.css';

const ViewProducts = () => {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [loading, setLoading] = useState(true);
  const [productRatings, setProductRatings] = useState({});

  const navigate = useNavigate();
  const sellerId = localStorage.getItem('sellerId');

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
    const fetchProducts = async () => {
      try {
        setLoading(true);
        const response = await productApi.getProducts(sellerId);
        const productsData = response.data || [];
        setProducts(productsData);
        setFilteredProducts(productsData);

        // Fetch ratings for each product
        const ratings = {};
        for (const product of productsData) {
          try {
            const ratingRes = await ratingsApi.getProductRatings(product.productId);
            const data = Array.isArray(ratingRes.data) ? ratingRes.data : (ratingRes.data ? [ratingRes.data] : []);

            if (data.length > 0) {
              const avg = (data.reduce((sum, r) => sum + r.ratingValue, 0) / data.length).toFixed(1);
              ratings[product.productId] = {
                average: avg,
                count: data.length,
                feedback: data.length > 0 ? data[0].feedback : null
              };
            } else {
              ratings[product.productId] = null;
            }
          } catch (err) {
            console.log(`Rating fetch error for product ${product.productId}:`, err);
            ratings[product.productId] = null;
          }
        }
        setProductRatings(ratings);
      } catch (err) {
        console.log('Products fetch error:', err);
        // Initialize empty arrays instead of showing error
        setProducts([]);
        setFilteredProducts([]);
      } finally {
        setLoading(false);
      }
    };

    if (sellerId) {
      fetchProducts();
    } else {
      setLoading(false);
      setProducts([]);
      setFilteredProducts([]);
    }
  }, [sellerId]);

  const handleSearchChange = (e) => {
    const value = e.target.value;
    setSearchTerm(value);

    const matches = products.filter(product =>
      product.productName.toLowerCase().includes(value.toLowerCase())
    );

    setSuggestions(value.trim() === '' ? [] : matches);
    setFilteredProducts(matches);
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
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="view-products-page">
      {/* Top Navigation */}
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/SellerHome" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2" style={{ fontSize: '24px' }}></i>
            <span className="fw-bold fs-4 text-success">FarmToHome</span>
          </Link>

          <form className="input-group w-50 mx-4" onSubmit={handleSearchSubmit}>
            <input
              type="text"
              className="form-control border-end-0"
              placeholder="Search products..."
              value={searchTerm}
              onChange={handleSearchChange}
              onFocus={() => {
                if (searchTerm.trim() !== '') setShowSuggestions(true);
              }}
              onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
            />
            <button className="btn btn-outline-secondary border-start-0" type="submit">
              <i className="fas fa-search"></i>
            </button>
            {showSuggestions && suggestions.length > 0 && (
              <div className="suggestions-dropdown position-absolute bg-white border rounded w-50 mt-2 shadow z-3" style={{ zIndex: 1000 }}>
                {suggestions.map((product) => (
                  <div
                    key={product.productId}
                    className="px-3 py-2 suggestion-item d-flex justify-content-between align-items-center"
                    onClick={() => handleSuggestionClick(product.productId)}
                    onMouseDown={(e) => e.preventDefault()}
                    style={{ cursor: 'pointer' }}
                  >
                    <span>{product.productName}</span>
                    <small className="text-muted">₹{product.productPrice}</small>
                  </div>
                ))}
              </div>
            )}
          </form>

          <div className="d-flex align-items-center gap-3">
            <Link to="/add-product" className="btn btn-primary px-3">
              <i className="fas fa-plus me-2"></i>Add Product
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2>My Products</h2>
        </div>

        <div className="row g-4">
          {filteredProducts.length === 0 && (
            <div className="col-12">
              <div className="alert alert-info">
                No products found. Click "Add Product" to add your first product.
              </div>
            </div>
          )}
          {filteredProducts.map((product) => (
            <div key={product.productId} className="col-md-4 col-lg-3">
              <Link to={`/product/${product.productId}`} className="text-decoration-none text-dark">
                <div className="card h-100 product-card shadow-sm">
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

                    <p className="card-text mb-1 text-muted">
                      Stock remaining: {product.productQuantity}
                    </p>
                    {product.productQuantity === 0 && (
                      <div className="mb-2">
                        <span className="badge bg-danger">Out of Stock</span>
                      </div>
                    )}

                    <p className="card-text mb-0">
                      <strong>Price: ₹{product.productPrice} {product.productQuantityType ? `per ${product.productQuantityType}` : ''}</strong>
                    </p>

                    <p className="card-text text-muted mb-0">
                      {productRatings[product.productId] ? (
                        <div>
                          {renderStars(productRatings[product.productId].average)} {productRatings[product.productId].average} / 5 ({productRatings[product.productId].count} ratings)
                        </div>
                      ) : (
                        "No Ratings"
                      )}
                    </p>
                    {/* {productRatings[product.productId]?.feedback && (
                      <p className="card-text small text-muted fst-italic mt-1">
                        "{productRatings[product.productId].feedback}"
                      </p>
                    )} */}
                  </div>
                </div>
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ViewProducts;
