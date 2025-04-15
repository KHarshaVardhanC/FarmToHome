import React from 'react';
import '../styles/ProductCard.css';

function ProductCard({ product, onAddToCart, onKnowMore }) {
  return (
    <div className="product-card">
      <div className="product-image">
        <img src={product.imageUrl} alt={product.productName} />
      </div>
      <div className="product-info">
        <h3>{product.productName}</h3>
        <p>{product.productDescription}</p>
        <p>Price: ${product.productPrice}</p>
        <div className="product-buttons">
          <button className="product-btn" onClick={onAddToCart}>Add to Cart</button>
          <button className="product-btn" onClick={onKnowMore}>Know More</button>
        </div>
      </div>
    </div>
  );
}

export default ProductCard;