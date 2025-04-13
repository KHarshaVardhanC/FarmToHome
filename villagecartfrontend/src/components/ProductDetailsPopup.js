import React from 'react';
import '../styles/ProductDetailsPopup.css';

const ProductDetailsPopup = ({ product, onClose, onAddToCart }) => {
  return (
    <div className="product-details-overlay">
      <div className="product-details-popup">
        <button className="close-btn" onClick={onClose}>×</button>
        
        <div className="product-details-content">
          <div className="product-details-image">
            <img src={product.imageUrl} alt={product.productName} />
          </div>
          
          <div className="product-details-info">
            <h2>{product.productName}</h2>
            
            <div className="product-detail-item">
              <span className="detail-label">Description:</span>
              <p>{product.productDescription}</p>
            </div>
            
            <div className="product-detail-item">
              <span className="detail-label">Category:</span>
              <p>{product.productCategory}</p>
            </div>
            
            <div className="product-detail-item">
              <span className="detail-label">Price:</span>
              <p className="product-price">₹{product.productPrice}/kg</p>
            </div>
            
            <div className="product-detail-item">
              <span className="detail-label">Available Quantity:</span>
              <p>{product.productQuantity} kg</p>
            </div>
            
            {product.seller && (
              <div className="seller-details">
                <h3>Seller Information</h3>
                <div className="product-detail-item">
                  <span className="detail-label">Seller Name:</span>
                  <p>{product.seller.sellerName || 'Not available'}</p>
                </div>
                
                {product.seller.sellerLocation && (
                  <div className="product-detail-item">
                    <span className="detail-label">Location:</span>
                    <p>{product.seller.sellerLocation}</p>
                  </div>
                )}
                
                {product.productionDate && (
                  <div className="product-detail-item">
                    <span className="detail-label">Production Date:</span>
                    <p>{new Date(product.productionDate).toLocaleDateString()}</p>
                  </div>
                )}
              </div>
            )}
            
            <div className="product-detail-actions">
              <button className="add-to-cart-btn" onClick={onAddToCart}>
                Add to Cart
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetailsPopup;