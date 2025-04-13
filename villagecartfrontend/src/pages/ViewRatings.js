import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ratingsApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import '../styles/ViewRatings.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const ViewRatings = () => {
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const sellerId = localStorage.getItem('sellerId');

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
      <div className="loader">
        <div className="spinner-border text-primary" role="status" />
      </div>
    );
  }

  return (
    <div className="view-ratings-page">
      {/* Navbar */}
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/seller-home" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2 fs-4"></i>
            <span className="fw-bold fs-4 text-success">FarmToHome</span>
          </Link>
          <div className="d-flex align-items-center gap-3">
            <Link to="/view-products" className="btn btn-outline-primary">
              <i className="fas fa-arrow-left me-2"></i>Back to Dashboard
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Ratings Section */}
      <div className="container-fluid px-4 py-4">
        <h4 className="mb-4">Product Ratings</h4>

        {error && <div className="alert alert-danger">{error}</div>}

        {ratings.length === 0 ? (
          <div className="alert alert-info">No ratings found.</div>
        ) : (
          <div className="row g-4">
            {ratings.map((rating) => (
              <div key={rating.ratingId} className="col-md-6 col-lg-4">
                <div className="card rating-card h-100 shadow-sm">
                  <div className="card-body">
                    <h5 className="card-title">{rating.productName}</h5>
                    <div className="rating-stars mb-2">
                      <span className="text-warning fs-5">{getRatingStars(rating.ratingValue)}</span>
                      <span className="ms-2 text-muted">({rating.ratingValue}/5)</span>
                    </div>
                    {rating.feedback && (
                      <p className="card-text small text-muted">{rating.feedback}</p>
                    )}
                    <div className="small text-muted mt-2">
                      By: {rating.customerFirstName} {rating.customerLastName}
                    </div>
                    <div className="small text-muted">
                      Date: {rating.createdAt ? new Date(rating.createdAt).toLocaleDateString() : 'N/A'}
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
