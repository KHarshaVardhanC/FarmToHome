import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ratingsApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const ViewRatings = () => {
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Replace with actual seller ID from authentication
  const sellerId = 1;

  useEffect(() => {
    const fetchRatings = async () => {
      try {
        setLoading(true);
        const response = await ratingsApi.getSellerRatings(sellerId);
        setRatings(response.data);
      } catch (err) {
        console.error('Error fetching ratings:', err);
        setError('Failed to load ratings. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchRatings();
  }, [sellerId]);

  const getRatingStars = (rating) => {
    return '★'.repeat(rating) + '☆'.repeat(5 - rating);
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

  return (
    <div className="view-ratings-page">
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

          <div className="d-flex align-items-center gap-3">
            <Link to="/" className="btn btn-outline-primary">
              <i className="fas fa-arrow-left me-2"></i>Back to Dashboard
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h4 className="mb-0">Product Ratings</h4>
        </div>

        {error && <div className="alert alert-danger">{error}</div>}

        {ratings.length === 0 ? (
          <div className="alert alert-info">No ratings found.</div>
        ) : (
          <div className="row g-4">
            {ratings.map((rating) => (
              <div key={rating.ratingId} className="col-md-6 col-lg-4">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title mb-2">{rating.productName}</h5>
                    <div className="mb-2">
                      <span className="text-warning" style={{ fontSize: '1.2rem' }}>
                        {getRatingStars(rating.ratingValue)}
                      </span>
                      <span className="ms-2 text-muted">({rating.ratingValue}/5)</span>
                    </div>
                    {rating.feedback && (
                      <p className="card-text">
                        <small className="text-muted">{rating.feedback}</small>
                      </p>
                    )}
                    <div className="mt-2">
                      <small className="text-muted">
                        By: {rating.customerFirstName} {rating.customerLastName}
                      </small>
                    </div>
                    <div>
                      <small className="text-muted">
                        Date: {rating.createdAt ? new Date(rating.createdAt).toLocaleDateString() : 'N/A'}
                      </small>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default ViewRatings; 