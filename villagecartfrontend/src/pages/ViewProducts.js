import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { productApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const ViewProducts = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Replace with actual seller ID from authentication
  const sellerId = 1; // This should come from your auth context

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await productApi.getProducts(sellerId);
        setProducts(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch products');
        setLoading(false);
      }
    };

    fetchProducts();
  }, [sellerId]);

  const handleDelete = async (productId) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productApi.deleteProduct(productId);
        setProducts(products.filter(product => product.productId !== productId));
      } catch (err) {
        setError('Failed to delete product');
      }
    }
  };

  if (loading) return (
    <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
      <div className="spinner-border text-primary" role="status">
        <span className="visually-hidden">Loading...</span>
      </div>
    </div>
  );

  return (
    <div className="view-products-page">
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

          <div className="d-flex flex-grow-1 mx-4">
            <div className="input-group">
              <input
                type="text"
                className="form-control border-end-0"
                placeholder="Search products..."
              />
              <button className="btn btn-outline-secondary border-start-0">
                <i className="fas fa-search"></i>
              </button>
            </div>
          </div>

          <div className="d-flex align-items-center gap-3">
            <Link to="/add-product" className="btn btn-primary px-4">
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
          {products.length === 0 ? (
            <div className="col-12">
              <div className="alert alert-info">
                No products found. Click "Add Product" to add your first product.
              </div>
            </div>
          ) : (
            products.map((product) => (
              <div key={product.productId} className="col-md-4 col-lg-3">
                <div className="card h-100">
                  {product.imageUrl && (
                    <img src={product.imageUrl} className="card-img-top" alt={product.productName} style={{ height: '200px', objectFit: 'cover' }} />
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
                    {product.productDescription && (
                      <p className="card-text">
                        <small>{product.productDescription}</small>
                      </p>
                    )}
                    <div className="d-flex gap-2 mt-3">
                      <button
                        className="btn btn-sm btn-outline-primary flex-grow-1"
                        onClick={() => navigate(`/product/${product.productId}`)}
                      >
                        Edit
                      </button>
                      <button
                        className="btn btn-sm btn-outline-danger flex-grow-1"
                        onClick={() => handleDelete(product.productId)}
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default ViewProducts; 