import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { productApi, ratingsApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import '../styles/ProductDetails.css';

const ProductDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [product, setProduct] = useState(null);
  const [ratings, setRatings] = useState([]);
  const [averageRating, setAverageRating] = useState(null);

  const renderStars = (rating) => {
    const fullStars = Math.floor(rating);
    const halfStar = rating - fullStars >= 0.5;
    const stars = [];
    for (let i = 0; i < fullStars; i++) stars.push(<i key={`full-${i}`} className="fas fa-star text-warning"></i>);
    if (halfStar) stars.push(<i key="half" className="fas fa-star-half-alt text-warning"></i>);
    while (stars.length < 5) stars.push(<i key={`empty-${stars.length}`} className="far fa-star text-warning"></i>);
    return stars;
  };

  // Format date to be more readable
  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      day: 'numeric',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        setLoading(true);

        try {
          const res = await productApi.getProduct(id);
          setProduct(res.data);
        } catch (err) {
          console.error('Error fetching product:', err);
          setError('Failed to load product details. Please try again later.');
          setLoading(false);
          return;
        }

        // Fetch ratings separately
        try {
          const ratingRes = await ratingsApi.getProductRatings(id);
          const data = Array.isArray(ratingRes.data) ? ratingRes.data : (ratingRes.data ? [ratingRes.data] : []);
          setRatings(data);

          if (data.length > 0) {
            const avg = (data.reduce((sum, r) => sum + r.ratingValue, 0) / data.length).toFixed(1);
            setAverageRating(avg);
          }
        } catch (ratingErr) {
          console.error('Error fetching ratings:', ratingErr);
          // Don't set error for missing ratings
          setRatings([]);
          setAverageRating(null);
        }
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productApi.deleteProduct(id);
        alert('Product deleted successfully!');
        navigate('/view-products');
      } catch (err) {
        console.error('Error deleting product:', err);
        alert('Failed to delete product. Please try again.');
      }
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

  if (error) return <div className="alert alert-danger m-4">{error}</div>;
  if (!product) return <div className="alert alert-warning m-4">Product not found.</div>;

  return (
    <div className="product-details-page">
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/SellerHome" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2 fs-4"></i>
            <span className="fw-bold fs-4 text-success">Village Cart</span>
          </Link>
          <div className="d-flex gap-3">
            <Link to="/view-products" className="btn btn-outline-primary">
              <i className="fas fa-arrow-left me-2"></i>Back to Products
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      <div className="container mt-4">
        <h4 className="mb-4 fw-bold">Product Details</h4>
        <div className="card shadow-sm">
          <div className="row g-0 align-items-center">
            <div className="col-md-8 p-4">
              <p><strong>Name:</strong> {product.productName}</p>
              <p><strong>Description:</strong> {product.productDescription}</p>
              <p><strong>Quantity:</strong> {product.productQuantity} {product.productQuantityType && product.productQuantityType}</p>
              <p><strong>Price:</strong> ₹{product.productPrice} {product.productQuantityType ? `per ${product.productQuantityType}` : ''}</p>

              {product.productQuantity === 0 && (
                <span className="badge bg-danger mb-3">Out of Stock</span>
              )}

              {/* Ratings & Feedback Section */}
              {averageRating ? (
                <div className="mb-2">
                  <strong>Rating:</strong> {renderStars(averageRating)}{' '}
                  <small className="text-muted">({averageRating} / 5, {ratings.length} ratings)</small>
                </div>
              ) : (
                <div className="mb-2">
                  <strong>Rating:</strong> <span className="text-muted">No ratings yet</span>
                </div>
              )}

              {/* Action Buttons */}
              <div className="d-flex flex-wrap gap-3 mt-4">
                {product.productQuantity === 0 ? (
                  <button className="btn btn-secondary" disabled>
                    <i className="fas fa-edit me-2"></i>Edit Product
                  </button>
                ) : (
                  <Link to={`/edit-product/${id}`} className="btn btn-primary">
                    <i className="fas fa-edit me-2"></i>Edit Product
                  </Link>
                )}
                <button className="btn btn-danger" onClick={handleDelete}>
                  <i className="fas fa-trash-alt me-2"></i>Delete Product
                </button>
              </div>
            </div>

            {product.imageUrl && (
              <div className="col-md-4 p-4">
                <img
                  src={product.imageUrl}
                  alt={product.productName}
                  className="img-fluid rounded product-image"
                  style={{ maxHeight: '200px', objectFit: 'cover', width: '100%' }}
                />
              </div>
            )}
          </div>
        </div>

        {/* Enhanced Customer Feedback Section */}
        <div className="card shadow-sm mt-4">
          <div className="card-header bg-light">
            <h5 className="mb-0">Customer Ratings & Feedback</h5>
          </div>
          <div className="card-body">
            {ratings.length > 0 ? (
              <div className="feedback-container">
                {ratings.map((rating, index) => (
                  <div key={index} className="feedback-item mb-3 pb-3 border-bottom">
                    <div className="d-flex justify-content-between align-items-center mb-2">
                      <div>
                        <span className="badge bg-secondary me-2">Customer #{index + 1}</span>
                        <span>{renderStars(rating.ratingValue)}</span>
                        <span className="ms-2 fw-bold">{rating.ratingValue}/5</span>
                      </div>
                      <small className="text-muted">{formatDate(rating.createdAt)}</small>
                    </div>
                    <div className="feedback-text">
                      {rating.feedback ? (
                        <p className="mb-0">{rating.feedback}</p>
                      ) : (
                        <p className="text-muted fst-italic mb-0">No comment provided</p>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-4">
                <i className="fas fa-comment-slash text-muted mb-3" style={{ fontSize: '2rem' }}></i>
                <p className="text-muted mb-0">No feedback available yet.</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetails;