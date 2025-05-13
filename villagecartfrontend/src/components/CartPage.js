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
  
  const customerId = parseInt(localStorage.getItem('customerId'), 10);

  // const customerId = localStorage.getItem('customerId');
 



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
        productId: item.product?.id || item.productId,
        productName: item.product?.name || item.productName,
        productPrice: item.product?.price || item.productPrice,
        orderPrice: item.orderPrice // no fallback
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
  
  // Fixed handleQuantityChange function to properly handle quantity changes
  const handleQuantityChange = async (orderId, newQuantity) => {
    if (newQuantity < 1) return;

    try {
      // Find the item in the cart
      const item = cartItems.find(item => item.orderId === orderId);
      if (!item) return;
      
      // 1. Update backend quantity
      await axios.put(`http://localhost:8080/order/update/${orderId}/${newQuantity}`);
      
      // 2. Check if this item has a special offer and if the new quantity qualifies
      const hasOffer = item.minOrderQuantity > 0 && item.discountPercentage > 0;
      const oldQualified = item.orderQuantity >= item.minOrderQuantity;
      const newQualifies = newQuantity >= item.minOrderQuantity;
      
      // 3. If qualification status changed, update the price
      if (hasOffer && (oldQualified !== newQualifies)) {
        const newPrice = newQualifies 
          ? parseFloat((item.originalPrice || item.productPrice) - ((item.originalPrice || item.productPrice) * item.discountPercentage / 100))
          : parseFloat(item.originalPrice || item.productPrice);
          
        // Update price in backend
        await axios.put(`http://localhost:8080/order/updatePrice/${orderId}`, {
          productPrice: newPrice
        });
        
        // Update state with new price
        setCartItems(prevItems =>
          prevItems.map(cartItem =>
            cartItem.orderId === orderId 
              ? { ...cartItem, productPrice: newPrice, orderQuantity: newQuantity } 
              : cartItem
          )
        );
      } else {
        // Just update quantity in state
        setCartItems(prevItems =>
          prevItems.map(cartItem =>
            cartItem.orderId === orderId ? { ...cartItem, orderQuantity: newQuantity } : cartItem
          )
        );
      }
    } catch (error) {
      console.error("Failed to update quantity:", error);
      alert("Failed to update quantity. Please try again.");
    }
  };

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


//   const calculateTotal = (items = cartItems) => {
//   const subtotal = items.reduce((total, item) => total + item.orderPrice, 0);
//   const processingFee = subtotal * 0.03;
//   const total = subtotal + processingFee;

//   return {
//     subtotal: subtotal.toFixed(2),
//     processingFee: processingFee.toFixed(2),
//     total: total.toFixed(2)
//   };
// };


const calculateTotal = (items) => {
  const subtotal = items.reduce((sum, item) => sum + parseFloat(item.orderPrice), 0);

  const processingFee = parseFloat((subtotal * 0.03).toFixed(2));
  const total = parseFloat((subtotal + processingFee).toFixed(2));

  return {
    subtotal: subtotal.toFixed(2),
    processingFee: processingFee.toFixed(2),
    total: total.toFixed(2),
    totalAmountInRupees: total // for payment gateway
  };
};


 const { subtotal, processingFee, total } = calculateTotal(cartItems);

  // const handleBuyNow = (item) => {

  //   console.log(item.orderId);
  //   setSelectedItem(item);
  //   setShowOrderPopup(true);
  //   // ✅ Store original cart item orderId for later use (e.g., in payment step)
  //    localStorage.setItem("selectedOrderId", item.orderId);
  // };

 const handleBuyNow = (item) => {

    console.log(item.orderId);
    setSelectedItem(item);
    setShowOrderPopup(true);
    // ✅ Store original cart item orderId for later use (e.g., in payment step)
     localStorage.setItem("selectedOrderId", item.orderId);
  };

  const handleBuyAll = () => {
    setSelectedItem(null);
    //setShowOrderPopup(true);
  };



const placeOrder = async () => {
  if (!customerId) {
    alert('Please login to place an order.');
    return;
  }

console.log("hello1");
try {
    console.log("hello1");
    setPlacingOrder(true);
    const itemsToOrder = selectedItem ? [selectedItem] : cartItems;

    console.log(itemsToOrder[0].orderId);
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

    const { totalAmountInRupees } = calculateTotal(itemsToOrder);

    // Convert to paise for Razorpay (multiply by 100)
    const totalAmountInPaise = Math.round(totalAmountInRupees * 100);

    // Get the existing orderId stored earlier (from cart or previous session)
    let orderId = localStorage.getItem('orderId');
    console.log(orderId);
    if (!itemsToOrder[0].orderId) {
      throw new Error('Order ID is missing');
    }
    console.log(itemsToOrder[0].orderId);


    // Prepare the order request (no new orderId is created, using the same orderId)
    const orderRequest = {
      existingOrderId: itemsToOrder[0].orderId,
      productId: itemsToOrder[0].productId,
      customerId: parseInt(customerId),
      orderId:parseInt(itemsToOrder[0].orderId),
      orderQuantity: itemsToOrder.length === 1 ? 
        parseFloat(itemsToOrder[0].orderQuantity) : 
        itemsToOrder.reduce((sum, item) => sum + parseFloat(item.orderQuantity), 0),
      orderStatus: "PENDING", // Initial status before payment
      paymentStatus: "INITIATED",
      amount: totalAmountInPaise, // Send amount in paise
      items: itemsToOrder.map(item => ({
        productId: item.productId,
        customerId:itemsToOrder[0].customerId,
        quantity: parseFloat(item.orderQuantity),
        price: Math.round(parseFloat(item.productPrice) * 100) // Convert price to paise
      }))
    };

    const paymentInitRes = await axios.post(
      `${API_BASE_URL}/order/payment/create`, 
      orderRequest
    );

    if (!paymentInitRes.data) {
      throw new Error('No response data from payment creation');
    }

    const { amount, currency, razorpayKey } = paymentInitRes.data;

    // Razorpay payment options
    const options = {
      key: razorpayKey || "rzp_test_KRRNUHKH42XUxO",
      amount: totalAmountInPaise, // Use our calculated amount
      currency: currency || "INR",
      order_id: orderId, // Use the existing orderId here
      name: "Village Cart",
      description: itemsToOrder.length === 1 
        ? `Order for ${itemsToOrder[0].productName}` 
        : `Order for ${itemsToOrder.length} items`,
      handler: async function (response) {
        try {
          console.log(response);
          const verificationRequest = {
            orderId: orderId, // Same orderId from cart
            razorpayOrderId: response.razorpay_order_id,
            razorpayPaymentId: response.razorpay_payment_id,
            razorpaySignature: response.razorpay_signature,
            productId: itemsToOrder[0].productId,
            customerId: parseInt(customerId),
            orderQuantity: itemsToOrder.length === 1 ? parseFloat(itemsToOrder[0].orderQuantity) : 0,
            orderStatus: "ORDERED", // Change the status to ORDERED after payment
            paymentStatus: "COMPLETED",
            amount: totalAmountInPaise,
            items: itemsToOrder.map(item => ({
              productId: item.productId,
              quantity: parseFloat(item.orderQuantity),
              price: Math.round(parseFloat(item.productPrice) * 100)
            }))
          };

          // Verify the payment and update the order status
          // const verificationResponse = await axios.post(
          //   `${API_BASE_URL}/order/payment/verify`,
          //   verificationRequest
          // );

          toast.success("Payment successful! Your order has been confirmed.");
          navigate("/my-orders");
          // if (verificationResponse.status === 200) {
          // } else {
          //   alert("Payment verification failed.");
          // }
        } catch (error) {
          console.error("Payment verification error:", error);
          const errorMsg = error.response?.data
            ? JSON.stringify(error.response.data)
            : error.message || "Unknown error occurred";
          alert("Error verifying payment: " + errorMsg);
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
    const errorMessage = error.response?.data?.message || error.response?.data || error.message || "Unknown error";
    alert("Error verifying payment: " + errorMessage);
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

    // const totalAmountInRupees = itemsToOrder.reduce(
    //   (sum, item) => sum + (parseFloat(item.productPrice) * parseFloat(item.orderQuantity)),
    //   0
    // );

    const { totalAmountInRupees } = calculateTotal(itemsToOrder);

    const totalAmountInPaise = Math.round(totalAmountInRupees * 100);

const orderRequestList = itemsToOrder.map(item => ({
  productId: item.productId,
  customerId: parseInt(customerId),
  orderQuantity: parseFloat(item.orderQuantity),
  orderStatus: "PENDING",
  paymentStatus: "INITIATED"
}));


    const paymentInitRes = await axios.post(
  `${API_BASE_URL}/order/payment/createAll`,
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
      // const verificationResponse = await axios.post(`${API_BASE_URL}/order/payment/verifyAll`, verificationRequest);

      // Handle backend response
      // if (verificationResponse.status === 200) {
         toast.success("Payment successful! Your order has been confirmed.");
          navigate("/my-orders");
      // } else {
      //   alert("Payment verification failed.");
      // }
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
                    {/* Use the product's quantity type instead of hardcoding 'kg' */}
                    <p className="item-price">₹{item.productPrice}/{item.productQuantityType || 'kg'}</p>
                    <div className="quantity-controls">
                      <button onClick={() => handleQuantityChange(item.orderId, item.orderQuantity - 1)}>-</button>
                      {/* Show the correct quantity type */}
                      <span>{item.orderQuantity.toFixed(1)} {item.productQuantityType || 'kg'}</span>
                      <button onClick={() => handleQuantityChange(item.orderId, item.orderQuantity + 1)}>+</button>
                    </div>
                    {/* <p className="item-total">Total: ₹{(item.productPrice * item.orderQuantity).toFixed(2)}</p> */}
                    <p className="item-total">
                      Total: ₹{(
                        (item.orderQuantity >= item.minOrderQuantity
                          ? item.productPrice * (1 - item.discountPercentage / 100)
                          : item.productPrice) * item.orderQuantity
                      ).toFixed(2)}
                    </p>
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
                  <span>Subtotal:</span>
                  <span>₹{subtotal}</span>
                </div>
                <div className="summary-row">
                  <span>Processing Fee (3%):</span>
                  <span>₹{processingFee}</span>
                </div>
                <div className="summary-row total">
                  <span>Total:</span>
                  <span>₹{total}</span>
                </div>
              </div>

              <button className="checkout-btn" onClick={handleBuyAll}>Buy All Items</button>
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
                    {/* Show the correct quantity type in the order popup */}
                    <p>Quantity: {selectedItem.orderQuantity.toFixed(1)} {selectedItem.productQuantityType || 'kg'}</p>
                    <p>Price: ₹{selectedItem.productPrice}/{selectedItem.productQuantityType || 'kg'}</p>
                  
                     <p className="order-item-total">Item Total: ₹{selectedItem.orderPrice.toFixed(2)}</p>
                    {/* <p>Processing Fee (3% share): ₹{itemProcessingFee.toFixed(2)}</p>
        <p>
          <strong>Total (Incl. Fee): ₹{itemFinalTotal.toFixed(2)}</strong>
        </p> 
       */}
                  </div>
                </div>
              ) : (
                <>
                  <h3>Order Summary ({cartItems.length} items)</h3>
                  <div className="order-items-list">
                    {cartItems.map(item => {
                      const isEligibleForDiscount = item.orderQuantity >= item.minOrderQuantity;
                      const finalPrice = isEligibleForDiscount
                        ? item.productPrice * (1 - item.discountPercentage / 100)
                        : item.productPrice;
                      const itemTotal = (finalPrice * item.orderQuantity).toFixed(2);

                      return (
                        <div key={item.orderId} className="order-summary-item">
                          <span>{item.productName} ({item.orderQuantity.toFixed(1)} kg)</span>
                          <span>₹{itemTotal}</span>
                        </div>
                      );
                    })}
                  </div>

                  <div className="order-total">
                    <strong>Total: ₹{calculateTotal()}</strong>
                  </div>
                </>
              )}
            </div>

           <button
              className="place-order-btn"
              onClick={selectedItem ? placeOrder : placeOrderAll}
              disabled={placingOrder}>
              {placingOrder ? "Processing..." : "Proceed to Payment"}
            </button>

          </div>
        </div>
      )}
    </div>
  );
}

export default CartPage;