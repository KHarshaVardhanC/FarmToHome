// import React from 'react';
// import '../styles/ProductDetailModal.css';

// function ProductDetailModal({ product, productDetails, onClose, onAddToCart }) {
//   // Check if we have seller info
//   const seller = productDetails.seller || {};

//   return (
//     <div className="product-modal-overlay" onClick={onClose}>
//       <div className="product-modal-content" onClick={(e) => e.stopPropagation()}>
//         <button className="modal-close-btn" onClick={onClose}>×</button>
        
//         <div className="product-modal-body">
//           <div className="product-modal-image">
//             <img src={product.imageUrl} alt={product.productName} />
//           </div>
          
//           <div className="product-modal-details">
//             <h2>{product.productName}</h2>
//             <p className="modal-price">₹{product.productPrice}/kg</p>
            
//             <div className="product-description">
//               <h3>Description</h3>
//               <p>{product.productDescription}</p>
//             </div>
            
//             {product.nutrition && (
//               <div className="product-nutrition">
//                 <h3>Nutrition Information</h3>
//                 <p>{product.nutrition}</p>
//               </div>
//             )}
            
//             {seller && Object.keys(seller).length > 0 && (
//               <div className="seller-information">
//                 <h3>Seller Information</h3>
//                 <p><strong>Name:</strong> {seller.name || 'N/A'}</p>
//                 <p><strong>Location:</strong> {seller.location || 'N/A'}</p>
//                 {seller.rating && <p><strong>Rating:</strong> {seller.rating} ★</p>}
//                 {seller.organic && <p className="organic-badge">Certified Organic</p>}
//               </div>
//             )}
            
//             <button className="add-to-cart-modal-btn" onClick={onAddToCart}>
//               Add to Cart
//             </button>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// }

// export default ProductDetailModal;