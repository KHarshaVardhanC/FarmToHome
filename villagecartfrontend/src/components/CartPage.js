// import React, { useState, useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
// import Navbar from './CustomerNavbar';
// import '../styles/CartPage.css';

// function CartPage() {
//   const [cartItems, setCartItems] = useState([]);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);
//   const [showOrderPopup, setShowOrderPopup] = useState(false);
//   const [selectedItem, setSelectedItem] = useState(null);
//   const [placingOrder, setPlacingOrder] = useState(false);
//   const navigate = useNavigate();
//   const customerId = localStorage.getItem('customerId');

//   useEffect(() => {
//     fetchCartItems();
//   }, []);

//   const fetchCartItems = async () => {
//     if (!customerId) {
//       setError('Please login to view your cart');
//       setLoading(false);
//       return;
//     }

//     try {
//       const response = await axios.get(`http://localhost:8080/order/orders/incart/${customerId}`);

//       if (Array.isArray(response.data)) {
//         setCartItems(response.data);
//         setError(null);
//       } else {
//         // Handle case where response is not in expected format
//         setCartItems([]);
//         setError(null); // Don't show error for empty cart
//       }
//     } catch (err) {
//       console.error('Error fetching cart items:', err);

//       setCartItems([]);
//       setError(null);
//     } finally {
//       setLoading(false);
//     }
//   };

//   const handleQuantityChange = async (orderId, newQuantity) => {
//     if (newQuantity < 1) return;
  
//     try {
//       // 1. Update backend (assumes you have an endpoint like this)
//       await axios.put(`http://localhost:8080/order/update/${orderId}/${newQuantity}`);
  
//       // 2. Update state
//       setCartItems(prevItems =>
//         prevItems.map(item =>
//           item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
//         )
//       );
  
//       // 3. Update localStorage
//       const cart = JSON.parse(localStorage.getItem('cart')) || [];
//       const updatedCart = cart.map(item =>
//         item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
//       );
//       localStorage.setItem('cart', JSON.stringify(updatedCart));
//     } catch (error) {
//       console.error("Failed to update quantity:", error);
//       alert("Failed to update quantity. Please try again.");
//     }
//   };
  

//     // const handleQuantityChange = (orderId, newQuantity) => {
//     //   if (newQuantity < 1) return;

//     //   setCartItems(prevItems =>
//     //     prevItems.map(item =>
//     //       item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
//     //     )
//     //   );
//     //   // console.log(prevItems);

//     //   const cart = JSON.parse(localStorage.getItem('cart')) || [];
//     //   const updatedCart = cart.map(item =>
//     //     item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
//     //   );
//     //   localStorage.setItem('cart', JSON.stringify(updatedCart));
//     // };

//   const removeFromCart = async (orderId) => {
//     try {
//       await axios.delete(`http://localhost:8080/order/delete/${orderId}`);
//       setCartItems(prevItems => prevItems.filter(item => item.orderId !== orderId));

//       const cart = JSON.parse(localStorage.getItem('cart')) || [];
//       const updatedCart = cart.filter(item => item.orderId !== orderId);
//       localStorage.setItem('cart', JSON.stringify(updatedCart));
//     } catch (err) {
//       console.error('Error removing item from cart:', err);
//       setError('Failed to remove item from cart.');
//     }
//   };

//   const calculateTotal = (items = cartItems) => {
//     return items.reduce((total, item) => total + item.productPrice * item.orderQuantity, 0).toFixed(2);
//   };

//   const handleBuyNow = (item) => {
//     setSelectedItem(item);
//     setShowOrderPopup(true);
//   };

//   const handleBuyAll = () => {
//     setSelectedItem(null);
//     setShowOrderPopup(true);
//   };

//   const placeOrder = async () => {
//     try {
//       setPlacingOrder(true);
//       const itemsToOrder = selectedItem ? [selectedItem] : cartItems;
//       let allOrdersSuccessful = true;

//       for (const item of itemsToOrder) {
//         // Use PUT request to update order status
//         const response = await axios.put(
//           `http://localhost:8080/order/order/${item.orderId}/ordered`
//         );

//         if (response.status === 200 || response.status === 201) {
//           // Remove from cart items in state
//           setCartItems(prev => prev.filter(cartItem => cartItem.orderId !== item.orderId));

//           // Update local storage cart
//           const cart = JSON.parse(localStorage.getItem('cart')) || [];
//           const updatedCart = cart.filter(cartItem => cartItem.orderId !== item.orderId);
//           localStorage.setItem('cart', JSON.stringify(updatedCart));
//         } else {
//           allOrdersSuccessful = false;
//           console.error("Order failed with status", response.status);
//         }
//       }

//       if (allOrdersSuccessful) {
//         alert("Order placed successfully!");
//         setShowOrderPopup(false);
//         navigate("/my-orders");
//       } else {
//         alert("Some orders failed to place. Please check console.");
//       }
//     } catch (error) {
//       console.error("Error placing order:", error);
//       alert("An error occurred while placing the order.");
//     } finally {
//       setPlacingOrder(false);
//     }
//   };

//   if (loading) return (
//     <div className="cart-page">
//       <Navbar />
//       <div className="loading">Loading your cart...</div>
//     </div>
//   );

//   if (error) return (
//     <div className="cart-page">
//       <Navbar />
//       <div className="error-message">{error}</div>
//     </div>
//   );

//   return (
//     <div className="cart-page">
//       <Navbar cartCount={cartItems.length} />
//       <div className="cart-container">
//         <h1 className="cart-title">Your Shopping Cart</h1>
//         {cartItems.length === 0 ? (
//           <div className="empty-cart">
//             <p>Your cart is empty</p>
//             <button className="continue-shopping-btn" onClick={() => navigate('/customer-home')}>
//               Continue Shopping
//             </button>
//           </div>
//         ) : (
//           <>
//             <div className="cart-items">
//               {cartItems.map(item => (
//                 <div key={item.orderId} className="cart-item">
//                   <div className="cart-item-image">
//                     <img src={item.imageUrl} alt={item.productName} />
//                   </div>
//                   <div className="cart-item-details">
//                     <h3>{item.productName}</h3>
//                     <p className="item-price">₹{item.productPrice}/kg</p>
//                     <div className="quantity-controls">
//                       <button onClick={() => handleQuantityChange(item.orderId, item.orderQuantity - 1)}>-</button>
//                       <span>{item.orderQuantity.toFixed(1)} kg</span>
//                       <button onClick={() => handleQuantityChange(item.orderId, item.orderQuantity + 1)}>+</button>
//                     </div>
//                     <p className="item-total">Total: ₹{(item.productPrice * item.orderQuantity).toFixed(2)}</p>
//                     <div className="item-actions">
//                       <button className="buy-now-btn" onClick={() => handleBuyNow(item)}>Buy Now</button>
//                       <button className="remove-btn" onClick={() => removeFromCart(item.orderId)}>Remove</button>
//                     </div>
//                   </div>
//                 </div>
//               ))}
//             </div>

//             <div className="cart-summary">
//               <h2>Order Summary</h2>
//               <div className="summary-details">
//                 <div className="summary-row">
//                   <span>Items ({cartItems.length}):</span>
//                   <span>₹{calculateTotal()}</span>
//                 </div>
//                 <div className="summary-row">
//                   <span>Delivery:</span>
//                   <span>Free</span>
//                 </div>
//                 <div className="summary-row total">
//                   <span>Total:</span>
//                   <span>₹{calculateTotal()}</span>
//                 </div>
//               </div>
//               <button className="checkout-btn" onClick={handleBuyAll}>
//                 Buy All Items
//               </button>
//             </div>
//           </>
//         )}
//       </div>

//       {showOrderPopup && (
//         <div className="order-popup-overlay">
//           <div className="order-popup">
//             <button className="close-popup" onClick={() => setShowOrderPopup(false)}>×</button>
//             <h2>Complete Your Order</h2>

//             <div className="order-items-summary">
//               {selectedItem ? (
//                 <div className="order-item">
//                   <img src={selectedItem.imageUrl} alt={selectedItem.productName} />
//                   <div className="order-item-details">
//                     <h3>{selectedItem.productName}</h3>
//                     <p>Quantity: {selectedItem.orderQuantity.toFixed(1)} kg</p>
//                     <p>Price: ₹{selectedItem.productPrice}/kg</p>
//                     <p className="order-item-total">Item Total: ₹{(selectedItem.productPrice * selectedItem.orderQuantity).toFixed(2)}</p>
//                   </div>
//                 </div>
//               ) : (
//                 <>
//                   <h3>Order Summary ({cartItems.length} items)</h3>
//                   <div className="order-items-list">
//                     {cartItems.map(item => (
//                       <div key={item.orderId} className="order-summary-item">
//                         <span>{item.productName} ({item.orderQuantity.toFixed(1)} kg)</span>
//                         <span>₹{(item.productPrice * item.orderQuantity).toFixed(2)}</span>
//                       </div>
//                     ))}
//                   </div>
//                   <div className="order-total">
//                     <strong>Total: ₹{calculateTotal()}</strong>
//                   </div>
//                 </>
//               )}
//             </div>

//             <button
//               className="place-order-btn"
//               onClick={placeOrder}
//               disabled={placingOrder}
//             >
//               {placingOrder ? "Processing..." : "Place Order"}
//             </button>
//           </div>
//         </div>
//       )}
//     </div>
//   );
// }

// export default CartPage;












import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Navbar from './CustomerNavbar';
import '../styles/CartPage.css';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const API_BASE_URL = process.env.REACT_APP_BACKEND_URL;

function loadRazorpayScript(src) {
  return new Promise((resolve) => {
    const script = document.createElement("script");
    script.src = src;
    script.onload = () => resolve(true);
    script.onerror = () => resolve(false);
    document.body.appendChild(script);
  });
}

function CartPage() {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showOrderPopup, setShowOrderPopup] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  const [placingOrder, setPlacingOrder] = useState(false);
  const [orderId, setOrderId] = useState(null);
  const navigate = useNavigate();
  const customerId = localStorage.getItem('customerId');

  useEffect(() => {
    fetchCartItems();
  }, []);

  const fetchCartItems = async () => {
    if (!customerId) {
      setError('Please login to view your cart');
      setLoading(false);
      return;
    }
    try {
      const response = await axios.get(`${API_BASE_URL}/order/orders/incart/${customerId}`);

      if (Array.isArray(response.data)) {
        // Transform the data to ensure product information is properly structured
        const transformedItems = response.data.map(item => ({
          ...item,
          productId: item.product?.id || item.productId, // Ensure productId is available
          productName: item.product?.name || item.productName,
          productPrice: item.product?.price || item.productPrice
        }));

      setCartItems(transformedItems);

      setError(null);
      } else {
        setCartItems([]);
        setError(null);
      }
    } catch (err) {
      console.error('Error fetching cart items:', err);
      setCartItems([]);
      setError('Failed to fetch cart items');
    } finally {
      setLoading(false);
    }
  };

  const handleQuantityChange = async (orderId, newQuantity) => {
    if (newQuantity < 1) return;
    try {
      // 1. Update backend (assumes you have an endpoint like this)
      await axios.put(`${API_BASE_URL}/order/update/${orderId}/${newQuantity}`);

      // 2. Update state
      setCartItems(prevItems =>
        prevItems.map(item =>
          item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
        )
      );

      // 3. Update localStorage
      const cart = JSON.parse(localStorage.getItem('cart')) || [];
      const updatedCart = cart.map(item =>
        item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
      );
      localStorage.setItem('cart', JSON.stringify(updatedCart));
    } catch (error) {
      console.error("Failed to update quantity:", error);
      alert("Failed to update quantity. Please try again.");
    }
  };


  // const handleQuantityChange = (orderId, newQuantity) => {
  //   if (newQuantity < 1) return;

  //   setCartItems(prevItems =>
  //     prevItems.map(item =>
  //       item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
  //     )
  //   );
  //   // console.log(prevItems);

  //   const cart = JSON.parse(localStorage.getItem('cart')) || [];
  //   const updatedCart = cart.map(item =>
  //     item.orderId === orderId ? { ...item, orderQuantity: newQuantity } : item
  //   );
  //   localStorage.setItem('cart', JSON.stringify(updatedCart));
  // };

  const removeFromCart = async (orderId) => {
    try {
      await axios.delete(`${API_BASE_URL}/order/delete/${orderId}`);
      setCartItems(prevItems => prevItems.filter(item => item.orderId !== orderId));

      const cart = JSON.parse(localStorage.getItem('cart')) || [];
      const updatedCart = cart.filter(item => item.orderId !== orderId);
      localStorage.setItem('cart', JSON.stringify(updatedCart));
    } catch (err) {
      console.error('Error removing item from cart:', err);
      setError('Failed to remove item from cart.');
    }
  };

  const calculateTotal = (items = cartItems) => {
    return items.reduce((total, item) => total + item.productPrice * item.orderQuantity, 0).toFixed(2);
  };

  const handleBuyNow = (item) => {
    setSelectedItem(item);
    setShowOrderPopup(true);
  };

  const handleBuyAll = () => {
    setSelectedItem(null);
    setShowOrderPopup(true);
  };

 
const placeOrder = async () => {
  if (!customerId) {
    alert('Please login to place an order.');
    return;
  }
  try {
    setPlacingOrder(true);
    const itemsToOrder = selectedItem ? [selectedItem] : cartItems;

    // Validation checks
    if (!itemsToOrder || itemsToOrder.length === 0) {
      throw new Error('No items to order');
    }

    // Validate all items
    itemsToOrder.forEach((item, index) => {
      if (!item.productId) {
        throw new Error(`Product ID is missing from item at index ${index}`);
      }
      if (!item.productName) {
        throw new Error(`Product Name is missing from item at index ${index}`);
      }
    });

    // Calculate total amount in rupees first
    const totalAmountInRupees = itemsToOrder.reduce((sum, item) => 
      sum + (parseFloat(item.productPrice) * parseFloat(item.orderQuantity)), 0);
    
    // Convert to paise for Razorpay (multiply by 100)
    const totalAmountInPaise = Math.round(totalAmountInRupees * 100);

    // Create order request
    const orderRequest = {
      productId: itemsToOrder[0].productId,
      customerId: parseInt(customerId),
      orderQuantity: itemsToOrder.length === 1 ? 
        parseFloat(itemsToOrder[0].orderQuantity) : 
        itemsToOrder.reduce((sum, item) => sum + parseFloat(item.orderQuantity), 0),
      orderStatus: "PENDING",
      paymentStatus: "INITIATED",
      amount: totalAmountInPaise, // Send amount in paise
      items: itemsToOrder.map(item => ({
        productId: item.productId,
        quantity: parseFloat(item.orderQuantity),
        price: Math.round(parseFloat(item.productPrice) * 100) // Convert price to paise
      }))
    };

    // Debug logging
    console.log('Total Amount in Rupees:', totalAmountInRupees);
    console.log('Total Amount in Paise:', totalAmountInPaise);
    console.log('Order Request:', orderRequest);

    const paymentInitRes = await axios.post(
      "http://localhost:8080/order/payment/create", 
      orderRequest
    );

    if (!paymentInitRes.data) {
      throw new Error('No response data from payment creation');
    }
    //navigate("/my-orders");  
    const { orderId, amount, currency, razorpayKey } = paymentInitRes.data;
    setOrderId(orderId);

    const options = {
      key: razorpayKey || "rzp_test_KRRNUHKH42XUxO",
      amount: totalAmountInPaise, // Use our calculated amount
      currency: currency || "INR",
      order_id: orderId,
      name: "Farm To Home",
      description: itemsToOrder.length === 1 
        ? `Order for ${itemsToOrder[0].productName}`
        : `Order for ${itemsToOrder.length} items`,
      handler: async function (response) {
        try {
          const verificationRequest = {
            orderId: orderId,
            razorpayOrderId: response.razorpay_order_id,
            razorpayPaymentId: response.razorpay_payment_id,
            razorpaySignature: response.razorpay_signature,
            productId: itemsToOrder[0].productId,
            customerId: parseInt(customerId),
            orderQuantity: orderRequest.orderQuantity,
            orderStatus: "ORDERED",
            paymentStatus: "COMPLETED",
            amount: totalAmountInPaise, // Include amount in verification
            items: itemsToOrder.map(item => ({
              productId: item.productId,
              quantity: parseFloat(item.orderQuantity),
              price: Math.round(parseFloat(item.productPrice) * 100)
            }))
          };

          const verificationResponse = await axios.post(
            "http://localhost:8080/order/payment/verify",
            verificationRequest
          );

          if (verificationResponse.status === 200) {
           // alert("Payment successful! Order confirmed.");
            toast.success("Payment successful! Your order has been confirmed.");
            navigate("/my-orders");
          } else {
            alert("Payment verification failed.");
          }
        } catch (error) {
          console.error("Payment verification error:", error);
          // alert("Error verifying payment: " + (error.response?.data || error.message));
          const errorMsg = error.response?.data
          ? JSON.stringify(error.response.data)
          : error.message || "Unknown error occurred";

        //alert("Error verifying payment: " + errorMsg);

        }
      },
      prefill: {
        name: "Customer",
        email: "",
        contact: ""
      },
      theme: {
        color: "#F37254"
      }
    };

    await loadRazorpayScript("https://checkout.razorpay.com/v1/checkout.js");
    const rzp = new window.Razorpay(options);
    rzp.open();

  } catch (error) {
    console.error("Error details:", error);
    console.error("Response data:", error.response?.data);
    const errorMessage = error.response?.data?.message || error.response?.data || error.message || "Unknown error";
    alert("Error verifying payment: " + errorMessage);

   // alert(error.message || error.response?.data || "An error occurred while placing the order.");
  } finally {
    setPlacingOrder(false);
  }
};
const placeOrderAll = async () => {
  if (!customerId) {
    alert('Please login to place an order.');
    return;
  }

  try {
    setPlacingOrder(true);

    const itemsToOrder = cartItems;

    if (!itemsToOrder || itemsToOrder.length === 0) {
      throw new Error('Your cart is empty.');
    }

    // Validate item details
    itemsToOrder.forEach((item, index) => {
      if (!item.productId) throw new Error(`Missing product ID at index ${index}`);
      if (!item.productName) throw new Error(`Missing product name at index ${index}`);
    });

    const totalAmountInRupees = itemsToOrder.reduce(
      (sum, item) => sum + (parseFloat(item.productPrice) * parseFloat(item.orderQuantity)),
      0
    );

    const totalAmountInPaise = Math.round(totalAmountInRupees * 100);

const orderRequestList = itemsToOrder.map(item => ({
  productId: item.productId,
  customerId: parseInt(customerId),
  orderQuantity: parseFloat(item.orderQuantity),
  orderStatus: "PENDING",
  paymentStatus: "INITIATED"
}));


    const paymentInitRes = await axios.post(
  "http://localhost:8080/order/payment/createAll",
  orderRequestList
);

    if (!paymentInitRes.data) {
      throw new Error('Payment initiation failed.');
    }
    //navigate('/my-orders') 
    const { orderId, amount, currency, razorpayKey } = paymentInitRes.data;
    setOrderId(orderId);

     
    const options = {
  key: razorpayKey || "rzp_test_KRRNUHKH42XUxO", // Your Razorpay API key
  amount: totalAmountInPaise, // Amount in paise (1 INR = 100 paise)
  currency: currency || "INR", // Currency type, default is INR
  order_id: orderId, // Razorpay order ID
  name: "Farm To Home", // Your company name
  description: `Order for ${itemsToOrder.length} items`, // Order description
  handler: async function (response) {
    try {
      const verificationRequest = {
        orderId: orderId, // Order ID from your database or backend
        razorpayOrderId: response.razorpay_order_id, // Razorpay order ID
        razorpayPaymentId: response.razorpay_payment_id, // Razorpay payment ID
        razorpaySignature: response.razorpay_signature, // Razorpay payment signature
        productId: itemsToOrder[0].productId, // Product ID of the first item in the order
        customerId: parseInt(customerId), // Customer ID from your user system
        orderQuantity: orderRequestList.orderQuantity, // Total order quantity
        orderStatus: "ORDERED", // Status of the order
        paymentStatus: "COMPLETED", // Payment status
        amount: totalAmountInPaise, // Total amount in paise
        items: itemsToOrder.map(item => ({
          productId: item.productId, // Product ID
          quantity: parseFloat(item.orderQuantity), // Quantity of the product
          price: Math.round(parseFloat(item.productPrice) * 100) // Price in paise (multiplied by 100)
        }))
      };

      // Send payment verification request to backend
      const verificationResponse = await axios.post("http://localhost:8080/order/payment/verifyAll", verificationRequest);

      // Handle backend response
      if (verificationResponse.status === 200) {
        alert("Payment successful! Order confirmed.");
        navigate("/my-orders"); // Navigate to the "My Orders" page
      } else {
        alert("Payment verification failed.");
      }
    } catch (error) {
      console.error("Payment verification error:", error);
      alert("Error verifying payment: " + (error.response?.data || error.message));
    }
  },
  prefill: {
    name: "Customer", // Prefill customer name
    email: "farha@gmail.com", // Prefill customer email
    contact: "7386175772" // Prefill customer contact number
  },
  theme: {
    color: "#F37254" // Razorpay payment form color
  }
};


    await loadRazorpayScript("https://checkout.razorpay.com/v1/checkout.js");
    const rzp = new window.Razorpay(options);
    rzp.open();

  } catch (error) {
    console.error("Order Error:", error);
    alert(error.message || "An error occurred while placing the order.");
  } finally {
    setPlacingOrder(false);
  }
};

// const placeOrderAll = async () => {
//   if (!customerId) {
//     alert('Please login to place an order.');
//     return;
//   }

//   try {
//     setPlacingOrder(true);
    
//     // Fetch all items from the cart
//     const itemsToOrder = cartItems;

//     // Validation checks
//     if (!itemsToOrder || itemsToOrder.length === 0) {
//       throw new Error('No items to order');
//     }

//     // Validate all items
//     itemsToOrder.forEach((item, index) => {
//       if (!item.productId) {
//         throw new Error(`Product ID is missing from item at index ${index}`);
//       }
//       if (!item.productName) {
//         throw new Error(`Product Name is missing from item at index ${index}`);
//       }
//     });

//     // Calculate total amount in rupees
//     const totalAmountInRupees = itemsToOrder.reduce((sum, item) => 
//       sum + (parseFloat(item.productPrice) * parseFloat(item.orderQuantity)), 0);

//     // Convert to paise for Razorpay (multiply by 100)
//     const totalAmountInPaise = Math.round(totalAmountInRupees * 100);

//     // Create order request
//     const orderRequest = {
//       productId: itemsToOrder[0].productId,  // You can keep the first product's ID, or decide based on your requirements
//       customerId: parseInt(customerId),
//       orderQuantity: itemsToOrder.reduce((sum, item) => sum + parseFloat(item.orderQuantity), 0),
//       orderStatus: "PENDING",
//       paymentStatus: "INITIATED",
//       amount: totalAmountInPaise,  // Send amount in paise
//       items: itemsToOrder.map(item => ({
//         productId: item.productId,
//         quantity: parseFloat(item.orderQuantity),
//         price: Math.round(parseFloat(item.productPrice) * 100)  // Convert price to paise
//       }))
//     };

//     // Debug logging
//     console.log('Total Amount in Rupees:', totalAmountInRupees);
//     console.log('Total Amount in Paise:', totalAmountInPaise);
//     console.log('Order Request:', orderRequest);

//     const paymentInitRes = await axios.post(
//       "http://localhost:8080/order/payment/create", 
//       orderRequest
//     );

//     if (!paymentInitRes.data) {
//       throw new Error('No response data from payment creation');
//     }

//     const { orderId, amount, currency, razorpayKey } = paymentInitRes.data;
//     setOrderId(orderId);

//     const options = {
//       key: razorpayKey || "rzp_test_KRRNUHKH42XUxO",
//       amount: totalAmountInPaise, // Use our calculated amount
//       currency: currency || "INR",
//       order_id: orderId,
//       name: "Farm To Home",
//       description: itemsToOrder.length === 1 
//         ? `Order for ${itemsToOrder[0].productName}` 
//         : `Order for ${itemsToOrder.length} items`,
//       handler: async function (response) {
//         try {
//           const verificationRequest = {
//             orderId: orderId,
//             razorpayOrderId: response.razorpay_order_id,
//             razorpayPaymentId: response.razorpay_payment_id,
//             razorpaySignature: response.razorpay_signature,
//             productId: itemsToOrder[0].productId,
//             customerId: parseInt(customerId),
//             orderQuantity: orderRequest.orderQuantity,
//             orderStatus: "CONFIRMED",
//             paymentStatus: "COMPLETED",
//             amount: totalAmountInPaise,  // Include amount in verification
//             items: itemsToOrder.map(item => ({
//               productId: item.productId,
//               quantity: parseFloat(item.orderQuantity),
//               price: Math.round(parseFloat(item.productPrice) * 100)
//             }))
//           };

//           const verificationResponse = await axios.post(
//             "http://localhost:8080/order/payment/verify",
//             verificationRequest
//           );

//           if (verificationResponse.status === 200) {
//             alert("Payment successful! Order confirmed.");
//             navigate("/my-orders");
//           } else {
//             alert("Payment verification failed.");
//           }
//         } catch (error) {
//           console.error("Payment verification error:", error);
//           alert("Error verifying payment: " + (error.response?.data || error.message));
//         }
//       },
//       prefill: {
//         name: "Customer",
//         email: "",
//         contact: ""
//       },
//       theme: {
//         color: "#F37254"
//       }
//     };

//     await loadRazorpayScript("https://checkout.razorpay.com/v1/checkout.js");
//     const rzp = new window.Razorpay(options);
//     rzp.open();

//   } catch (error) {
//     console.error("Error details:", error);
//     console.error("Response data:", error.response?.data);
//     alert(error.message || error.response?.data || "An error occurred while placing the order.");
//   } finally {
//     setPlacingOrder(false);
//   }
// };




  const handlePaymentSuccess = (paymentResponse) => {
    alert('Payment successful!');
    navigate('/my-orders');
  };

  if (loading) return (
    <div className="cart-page">
      <Navbar />
      <div className="loading">Loading your cart...</div>
    </div>
  );

  if (error) return (
    <div className="cart-page">
      <Navbar />
      <div className="error-message">{error}</div>
    </div>
  );
  

  //if (loading) return <div className="cart-page"><Navbar /><div className="loading">Loading your cart...</div></div>;
  //if (error) return <div className="cart-page"><Navbar /><div className="error-message">{error}</div></div>;

  return (
    <div className="cart-page">
      <Navbar cartCount={cartItems.length} />
      <div className="cart-container">
        <h1 className="cart-title">Your Shopping Cart</h1>
        {cartItems.length === 0 ? (
          <div className="empty-cart">
            <p>Your cart is empty</p>
            <button className="continue-shopping-btn" onClick={() => navigate('/customer-home')}>Continue Shopping</button>
          </div>
        ) : (
          <>
            <div className="cart-items">
              {cartItems.map(item => (
                <div key={item.orderId} className="cart-item">
                  <div className="cart-item-image">
                    <img src={item.imageUrl} alt={item.productName} />
                  </div>
                  <div className="cart-item-details">
                    <h3>{item.productName}</h3>
                    <p className="item-price">₹{item.productPrice}/kg</p>
                    <div className="quantity-controls">
                      <button onClick={() => handleQuantityChange(item.orderId, item.orderQuantity - 1)}>-</button>
                      <span>{item.orderQuantity.toFixed(1)} kg</span>
                      <button onClick={() => handleQuantityChange(item.orderId, item.orderQuantity + 1)}>+</button>
                    </div>
                    <p className="item-total">Total: ₹{(item.productPrice * item.orderQuantity).toFixed(2)}</p>
                    <div className="item-actions">
                      <button className="buy-now-btn" onClick={() => handleBuyNow(item)}>Buy Now</button>
                      <button className="remove-btn" onClick={() => removeFromCart(item.orderId)}>Remove</button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <div className="cart-summary">
              <h2>Order Summary</h2>
              <div className="summary-details">
                <div className="summary-row">
                  <span>Items ({cartItems.length}):</span>
                  <span>₹{calculateTotal()}</span>
                </div>
                <div className="summary-row">
                  <span>Delivery:</span>
                  <span>Free</span>
                </div>
                <div className="summary-row total">
                  <span>Total:</span>
                  <span>₹{calculateTotal()}</span>
                </div>
              </div>
              <button className="checkout-btn" onClick={placeOrderAll}>Buy All Items</button>
            </div>
          </>
        )}
      </div>

      {showOrderPopup && (
        <div className="order-popup-overlay">
          <div className="order-popup">
            <button className="close-popup" onClick={() => setShowOrderPopup(false)}>×</button>
            <h2>Complete Your Order</h2>
            <div className="order-items-summary">
              {selectedItem ? (
                <div className="order-item">
                  <img src={selectedItem.imageUrl} alt={selectedItem.productName} />
                  <div className="order-item-details">
                    <h3>{selectedItem.productName}</h3>
                    <p>Quantity: {selectedItem.orderQuantity.toFixed(1)} kg</p>
                    <p>Price: ₹{selectedItem.productPrice}/kg</p>
                    <p className="order-item-total">Item Total: ₹{(selectedItem.productPrice * selectedItem.orderQuantity).toFixed(2)}</p>
                  </div>
                </div>
              ) : (
                <>
                  <h3>Order Summary ({cartItems.length} items)</h3>
                  <div className="order-items-list">
                    {cartItems.map(item => (
                      <div key={item.orderId} className="order-summary-item">
                        <span>{item.productName} ({item.orderQuantity.toFixed(1)} kg)</span>
                        <span>₹{(item.productPrice * item.orderQuantity).toFixed(2)}</span>
                      </div>
                    ))}
                  </div>
                  <div className="order-total">
                    <strong>Total: ₹{calculateTotal()}</strong>
                  </div>
                </>
              )}
            </div>

            <button className="place-order-btn" onClick={placeOrder} disabled={placingOrder}>
              {placingOrder ? "Processing..." : "Proceed to Payment"}
            </button>

          </div>
        </div>
      )}
    </div>
  );
}

export default CartPage;
