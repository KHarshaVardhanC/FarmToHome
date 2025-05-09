// src/components/RazorpayCheckout.js
import React, { useEffect } from 'react';



const RazorpayCheckout = ({ amount, orderId, onSuccess }) => {
  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://checkout.razorpay.com/v1/checkout.js';
    script.async = true;
    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script);
    };
  }, []);

  const openRazorpay = () => {
    const options = {
      key: 'rzp_test_KRRNUHKH42XUxO',
      amount: amount * 100, // Convert to paise
      currency: 'INR',
      name: 'Farm To Home',
      description: 'Test Transaction',
      order_id: orderId, // From your backend
      handler: function (response) {
        // Send response to backend for verification
        onSuccess(response); // callback from parent
      },
      theme: {
        color: '#3399cc',
      },
    };

    if (typeof window.Razorpay === 'undefined') {
      alert("Razorpay SDK failed to load. Please check your internet connection or try again.");
      return;
    }
    
    const rzp = new window.Razorpay(options);
    rzp.open();
    
  };
  return (
    <button
      className="place-order-btn"
      onClick={openRazorpay}
      style={{ backgroundColor: "#4CAF50", color: "white" }} // Green background, white text
    >
      Pay with Razorpay
    </button>
  );
  
};

export default RazorpayCheckout;
