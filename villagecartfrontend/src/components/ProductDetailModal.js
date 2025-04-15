import React from 'react';
import '../styles/ProductDetailModal.css';

function ProductDetailModal({ product, productDetails, onClose, onAddToCart }) {
  // Check if productDetails exists and extract data
  const productName = productDetails?.productName || product?.productName || 'N/A';
  const productPrice = productDetails?.productPrice || product?.productPrice || 'N/A';
  const productDescription = productDetails?.productDescription || product?.productDescription || 'N/A';
  const imageUrl = productDetails?.imageUrl || product?.imageUrl || '';
  
  // Extract seller information from productDetails
  const sellerName = productDetails?.sellerName || 'N/A';
  const sellerPlace = productDetails?.sellerPlace || 'N/A';
  const sellerCity = productDetails?.sellerCity || 'N/A';

  return (
    <div className="product-modal-overlay" onClick={onClose}>
      <div className="product-modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close-btn" onClick={onClose}>
          ×
        </button>
        
        <div className="product-modal-body">
          <div className="product-modal-image">
            <img src={imageUrl} alt={productName} />
          </div>
          
          <div className="product-modal-details">
            <h2>{productName}</h2>
            <p className="modal-price">₹{productPrice}/kg</p>
            <p className="product-description">{productDescription}</p>
            
            <div className="seller-information">
              <h3>Seller Information</h3>
              <p>Name: {sellerName}</p>
              <p>Location: {sellerPlace}, {sellerCity}</p>
            </div>
            
            <button className="add-to-cart-modal-btn" onClick={onAddToCart}>
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProductDetailModal;