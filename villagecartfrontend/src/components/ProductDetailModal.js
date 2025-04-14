import React from 'react';
import '../styles/ProductDetailModal.css';

function ProductDetailModal({ product, productDetails, onClose, onAddToCart }) {
  const seller = productDetails.seller || {};

  return (
    <div className="product-modal-overlay" onClick={onClose}>
      <div className="product-modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close-btn" onClick={onClose}>
          ×
        </button>

        <div className="product-modal-body">
          <div className="product-modal-image">
            <img src={product.imageUrl} alt={product.productName} />
          </div>

          <div className="product-modal-details">
            <h2>{product.productName}</h2>
            <p className="modal-price">₹{product.productPrice}/kg</p>
            <p className="product-description">{product.productDescription}</p>

            <div className="seller-information">
              <h3>Seller Information</h3>
              <p>Name: {seller.name || 'N/A'}</p>
              <p>Location: {seller.location || 'N/A'}</p>
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