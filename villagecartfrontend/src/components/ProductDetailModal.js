import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/ProductDetailModal.css';

const API_BASE_URL = process.env.REACT_APP_BACKEND_URL;

function ProductDetailModal({ product, productDetails, onClose, onAddToCart,cartItem, updateCartItemQuantity  }) {
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Check if productDetails exists and extract data
  const productId = productDetails?.productId || product?.productId || 'N/A';
  const productName = productDetails?.productName || product?.productName || 'N/A';
  const productPrice = productDetails?.productPrice || product?.productPrice || 'N/A';
  const productDescription = productDetails?.productDescription || product?.productDescription || 'N/A';
  const imageUrl = productDetails?.imageUrl || product?.imageUrl || '';
  const productQuantity = productDetails?.productQuantity || product?.productQuantity || 0;

  // Extract seller information from productDetails
  const sellerName = productDetails?.sellerName || 'N/A';
  const sellerPlace = productDetails?.sellerPlace || 'N/A';
  const sellerCity = productDetails?.sellerCity || 'N/A';

  useEffect(() => {
    const fetchRatings = async () => {
      if (productId === 'N/A') return;

      try {
        setLoading(true);
        const response = await axios.get(`${API_BASE_URL}/rating/product/${productId}`);
        // Make sure we're setting all ratings from the response
        setRatings(response.data || []);
        console.log('Fetched ratings:', response.data);
      } catch (err) {
        console.error('Error fetching ratings:', err);
        setError('Failed to load ratings');
      } finally {
        setLoading(false);
      }
    };

    fetchRatings();
  }, [productId]);

  // Calculate average rating
  const calculateAverageRating = () => {
    if (!ratings || ratings.length === 0) return 0;

    const sum = ratings.reduce((total, rating) => total + rating.ratingValue, 0);
    return (sum / ratings.length).toFixed(1);
  };

  // Render rating stars using divs and background colors
  const renderRatingStars = (rating) => {
    const fullStars = Math.floor(rating);
    const remainder = rating - fullStars;
    const stars = [];

    // Add full stars
    for (let i = 0; i < fullStars; i++) {
      stars.push(
        <div
          key={`full-${i}`}
          className="star-icon filled-star"
          style={{ color: "#FFD700" }}
        >
          ★
        </div>
      );
    }

    // Add half star if needed
    if (remainder >= 0.5) {
      stars.push(
        <div
          key="half"
          className="star-icon half-filled"
          style={{ color: "#FFD700" }}
        >
          ★
        </div>
      );
    }

    // Add empty stars
    const emptyStarsCount = 5 - stars.length;
    for (let i = 0; i < emptyStarsCount; i++) {
      stars.push(
        <div
          key={`empty-${i}`}
          className="star-icon empty-star"
          style={{ color: "#D3D3D3" }}
        >
          ☆
        </div>
      );
    }

    return <div className="stars-container">{stars}</div>;
  };

  return (
    <div className="product-modal-overlay" onClick={onClose}>
      <div className="product-modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close-btn" onClick={onClose}>
          ×
        </button>

        <div className="product-modal-body">
          <div className="product-modal-image">
            <img src={imageUrl} alt={productName} />
            {productQuantity === 0 && (
              <div className="modal-out-of-stock">Out of Stock</div>
            )}
          </div>

          <div className="product-modal-details">
            <h2>{productName}</h2>
            <p className="modal-price">₹{productPrice}/kg</p>

            {/* Rating Summary */}
            <div className="rating-summary">
              {renderRatingStars(calculateAverageRating())}
              <span className="rating-value">{calculateAverageRating()}</span>
              <span className="rating-count">({ratings.length} ratings)</span>
            </div>

            <p className="product-description">{productDescription}</p>

            {/* Stock information */}
            {productQuantity > 0 ? (
              <p className="product-quantity">
                {productQuantity < 5 ? `Only ${productQuantity}` : productQuantity} kg available
              </p>
            ) : (
              <p className="product-quantity out-of-stock">Out of Stock</p>
            )}

            <div className="seller-information">
              <h3>Seller Information</h3>
              <p>Name: {sellerName}</p>
              <p>Location: {sellerPlace}, {sellerCity}</p>
            </div>

            {cartItem ? (
              <div className="modal-quantity-control">
                <button 
                  className="modal-quantity-btn" 
                  onClick={() => updateCartItemQuantity(cartItem.orderQuantity - 1)}
                >
                  -
                </button>
                <span className="modal-quantity-display">{cartItem.orderQuantity}</span>
                <button 
                  className="modal-quantity-btn" 
                  onClick={() => updateCartItemQuantity(cartItem.orderQuantity + 1)}
                  disabled={cartItem.orderQuantity >= product.productQuantity}
                >
                  +
                </button>
              </div>
            ) : (
              <button 
                className="add-to-cart-modal-btn" 
                onClick={onAddToCart}
                disabled={product.productQuantity <= 0}
              >
                {product.productQuantity > 0 ? 'Add to Cart' : 'Out of Stock'}
              </button>
            )}
          </div>
        </div>

        {/* Customer Reviews Section */}
        <div className="customer-reviews-section">
          <h3>Customer Reviews</h3>

          {loading && <p className="loading-text">Loading reviews...</p>}

          {error && <p className="error-text">{error}</p>}

          {!loading && !error && (!ratings || ratings.length === 0) && (
            <p className="no-reviews">No reviews yet for this product.</p>
          )}

          {!loading && !error && ratings && ratings.length > 0 && (
            <div className="reviews-list">
              {/* Make sure we're rendering ALL ratings */}
              {ratings.map((rating) => (
                <div key={rating.ratingId} className="review-item">
                  <div className="review-header">
                    <span className="customer-name">{rating.customerName || "Anonymous Customer"}</span>
                    {renderRatingStars(rating.ratingValue)}
                    <span className="review-date">
                      {new Date(rating.createdAt).toLocaleDateString()}
                    </span>
                  </div>
                  <p className="review-feedback">{rating.feedback}</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ProductDetailModal;