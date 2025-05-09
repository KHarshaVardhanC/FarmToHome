import React from 'react';

function CartQuantityControl({ orderId, quantity, onIncrease, onDecrease, maxQuantity }) {
  return (
    <div className="cart-quantity-control">
      <button 
        className="quantity-btn decrease" 
        onClick={() => onDecrease(orderId)}
        disabled={quantity <= 1}
      >
        -
      </button>
      <span className="quantity-display">{quantity}</span>
      <button 
        className="quantity-btn increase" 
        onClick={() => onIncrease(orderId)}
        disabled={quantity >= maxQuantity}
      >
        +
      </button>
    </div>
  );
}

export default CartQuantityControl;