import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { productApi } from '../utils/api';
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
  const [error, setError] = useState(null);

  const navigate = useNavigate();
  const sellerId = 1; // Replace with actual seller ID

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await productApi.getProducts(sellerId);
        setProducts(response.data);
        setFilteredProducts(response.data);
      } catch (err) {
        setError('Failed to fetch products');
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
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

        {error && <div className="alert alert-danger">{error}</div>}

        <div className="row g-4">
          {filteredProducts.length === 0 ? (
            <div className="col-12">
              <div className="alert alert-info">
                No products found. Click "Add Product" to add your first product.
              </div>
            </div>
          ) : (
            filteredProducts.map((product) => (
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
                        <strong>Price: ₹{product.productPrice}</strong>
                      </p>
                    </div>
                  </div>
                </Link>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default ViewProducts;
