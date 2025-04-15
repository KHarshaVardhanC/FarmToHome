import React, { useState } from "react";


import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

import '../../assets/signupp.css';

const Signup = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    place: "",
    city: "",
    pincode: "",
    state: "",
    phone: "",
    email: "",
    password: "",
    confirmPassword: "",
    userType: "customer", // Default to customer
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleRoleChange = (e) => {
    const selectedRole = e.target.value;
    setFormData((prevState) => ({
      ...prevState,
      userType: selectedRole,
    }));
  };

  const validateForm = () => {
    const newErrors = {};

    // Basic validations
    if (!formData.firstName.trim()) newErrors.firstName = "First name is required";
    if (!formData.lastName.trim()) newErrors.lastName = "Last name is required";
    if (!formData.place.trim()) newErrors.place = "Place is required";
    if (!formData.city.trim()) newErrors.city = "City is required";
    if (!formData.state.trim()) newErrors.state = "State is required";

    // Pincode validation
    if (!formData.pincode.trim()) {
      newErrors.pincode = "Pincode is required";
    } else if (!/^\d{6}$/.test(formData.pincode)) {
      newErrors.pincode = "Pincode must be 6 digits";
    }

    // Phone validation
    if (!formData.phone.trim()) {
      newErrors.phone = "Phone number is required";
    } else if (!/^\d{10}$/.test(formData.phone)) {
      newErrors.phone = "Phone number must be 10 digits";
    }

    // Email validation
    if (!formData.email.trim()) {
      newErrors.email = "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = "Invalid email format";
    }

    // Password validation
    if (!formData.password) {
      newErrors.password = "Password is required";
    } else if (formData.password.length < 6) {
      newErrors.password = "Password must be at least 6 characters";
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }

    return newErrors;
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validateForm();

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setIsLoading(true);
    setErrors({});

    try {
      let endpoint;
      let requestBody;

      if (formData.userType === "customer") {
        endpoint = "http://localhost:8080/customer";
        requestBody = {
          customerFirstName: formData.firstName,
          customerLastName: formData.lastName,
          customerEmail: formData.email,
          customerPassword: formData.password,
          customerPlace: formData.place,
          customerCity: formData.city,
          customerState: formData.state,
          customerPincode: formData.pincode,
          customerPhoneNumber: formData.phone,
          customerRole: "CUSTOMER"
        };
      } else if (formData.userType === "seller") {
        endpoint = "http://localhost:8080/seller";
        requestBody = {
          sellerFirstName: formData.firstName,
          sellerLastName: formData.lastName,
          sellerEmail: formData.email,
          sellerPassword: formData.password,
          sellerPlace: formData.place,
          sellerCity: formData.city,
          sellerState: formData.state,
          sellerPincode: formData.pincode,
          sellerMobileNumber: formData.phone,
          sellerStatus: "ACTIVE",
          sellerRole: "SELLER"
        };
      }

      console.log('Sending request to:', endpoint);
      console.log('Request body:', requestBody);

      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(requestBody)
      });

      // First try to get the response as text
      const responseText = await response.text();
      console.log('Response text:', responseText);

      let data;
      try {
        // Try to parse the response text as JSON
        data = responseText ? JSON.parse(responseText) : {};
      } catch (e) {
        // If parsing fails, use the text as message
        data = { message: responseText || 'Unknown error occurred' };
      }

   // Check if response includes email already exists message
//    if (responseText.toLowerCase().includes('already exists') || 
//    responseText.toLowerCase().includes('already registered')) {
//  setErrors({
//    general: "An account with this email already exists. Please login."
//  });

    // Replace the email exists check section in handleSubmit
if (responseText.toLowerCase().includes('already exists') || 
responseText.toLowerCase().includes('already registered')) {

// Show alert with user type
alert(`You already have an account as a ${formData.userType}. Please login to continue.`);

// Navigate to login page after alert
navigate("/login", {
    state: {
        email: formData.email,
        userType: formData.userType,
        message: "Please login with your existing account"
    }
});
return;
}
 
 // Auto-redirect to signin after 3 seconds
//  setTimeout(() => {
//    navigate("/login", {
//      state: {
//        email: formData.email,
//        userType: formData.userType,
//        message: "Please login with your existing account"
//      }
//    });
//  }, 3000);
//  return;
// }

if (!response.ok) {
 throw new Error(data.message || `Registration failed with status ${response.status}`);
}

// Success case
navigate("/login", {
 state: {
   message: "Registration successful! Please log in.",
   userType: formData.userType
 }
});

} catch (error) {
console.error('Registration error:', error);
setErrors({
 general: error.message || "Registration failed. Please try again."
});
} finally {
setIsLoading(false);
}
};
  return (
    <motion.div
      className="signup-page"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.5 }}
    >
      <div className="signup-container">
        <div className="signup-header">
          <h1>Village Cart!!</h1>
        </div>

        <div className="signup-form-group">
          <label htmlFor="userType" id="lab">Choose Role:</label>
          <select
            name="userType"
            value={formData.userType}
            onChange={handleRoleChange}
            className="signup-role-dropdown"
          >
            <option value="customer">Customer</option>
            <option value="seller">Seller</option>
            <option value="seller">Seller</option>
          </select>
        </div>

        {errors.general && (
          <div className="signup-error-message">{errors.general}</div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="signup-form-group">
            <input
              type="text"
              name="firstName"
              placeholder="First Name"
              value={formData.firstName}
              onChange={handleChange}
              className={errors.firstName ? "signup-input error" : "signup-input"}
            />
            {errors.firstName && (
              <div className="signup-error-message">{errors.firstName}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="text"
              name="lastName"
              placeholder="Last Name"
              value={formData.lastName}
              onChange={handleChange}
              className={errors.lastName ? "signup-input error" : "signup-input"}
            />
            {errors.lastName && (
              <div className="signup-error-message">{errors.lastName}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="text"
              name="place"
              placeholder="Place"
              value={formData.place}
              onChange={handleChange}
              className={errors.place ? "signup-input error" : "signup-input"}
            />
            {errors.place && (
              <div className="signup-error-message">{errors.place}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="text"
              name="city"
              placeholder="City"
              value={formData.city}
              onChange={handleChange}
              className={errors.city ? "signup-input error" : "signup-input"}
            />
            {errors.city && (
              <div className="signup-error-message">{errors.city}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="text"
              name="pincode"
              placeholder="Pincode"
              value={formData.pincode}
              onChange={handleChange}
              className={errors.pincode ? "signup-input error" : "signup-input"}
            />
            {errors.pincode && (
              <div className="signup-error-message">{errors.pincode}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="text"
              name="state"
              placeholder="State"
              value={formData.state}
              onChange={handleChange}
              className={errors.state ? "signup-input error" : "signup-input"}
            />
            {errors.state && (
              <div className="signup-error-message">{errors.state}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="tel"
              name="phone"
              placeholder="Phone Number"
              value={formData.phone}
              onChange={handleChange}
              className={errors.phone ? "signup-input error" : "signup-input"}
            />
            {errors.phone && (
              <div className="signup-error-message">{errors.phone}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="email"
              name="email"
              placeholder="Email Address"
              value={formData.email}
              onChange={handleChange}
              className={errors.email ? "signup-input error" : "signup-input"}
            />
            {errors.email && (
              <div className="signup-error-message">{errors.email}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              className={errors.password ? "signup-input error" : "signup-input"}
            />
            {errors.password && (
              <div className="signup-error-message">{errors.password}</div>
            )}
          </div>

          <div className="signup-form-group">
            <input
              type="password"
              name="confirmPassword"
              placeholder="Confirm Password"
              value={formData.confirmPassword}
              onChange={handleChange}
              className={errors.confirmPassword ? "signup-input error" : "signup-input"}
            />
            {errors.confirmPassword && (
              <div className="signup-error-message">{errors.confirmPassword}</div>
            )}
          </div>
          <button type="submit" className="signup-btn" disabled={isLoading}>
            {isLoading ? "Creating Account..." : "Sign Up"}
          </button>
        </form>

        <div className="signup-footer">
          <p>
            Already have an account?{" "}
            <Link to="/login" className="signup-link" id="signup">
              Login
            </Link>
          </p>
        </div>
      </div>
      <motion.div 
        className="right-content"
        initial={{ opacity: 0, x: 100 }}
        animate={{ opacity: 1, x: 0 }}
        transition={{ duration: 0.9, delay: 0.4 }}>
        <h1>"Join Today,</h1>
        <h1>Experience Freshness Everyday"</h1>
        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.8 }}>
        </motion.p>
      </motion.div>
    </motion.div>
  );
};

export default Signup;





