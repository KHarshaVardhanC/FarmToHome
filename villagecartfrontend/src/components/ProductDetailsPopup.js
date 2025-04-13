import React from "react";
import "../styles/ProductDetailsPopup.css";

function ProductDetailsPopup({ product, onClose }) {
  return (
    <div className="popup-overlay">
      <div className="popup-content">
        <button className="close-btn" onClick={onClose}>
          &times;
        </button>
        <h2>{product.productName}</h2>
        <img src={product.imageUrl} alt={product.productName} />
        <p>{product.productDescription}</p>
        <p>Price: <span>₹{product.productPrice}</span></p>
        <p>Seller: <span>{product.sellerName}</span></p>
        <p>Contact: <span>{product.sellerContact}</span></p>
      </div>
    </div>
  );
}

export default ProductDetailsPopup;