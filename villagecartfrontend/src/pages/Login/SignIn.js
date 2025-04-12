/*import React, { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { FaEnvelope, FaLock } from "react-icons/fa";
import VerifyEmailPopup from "./VerifyEmailPopup";
import '../../assets/signin.css';

const Signin = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isSellerLogin, setIsSellerLogin] = useState(false);
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    userType: "customer",
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [isPopupOpen, setIsPopupOpen] = useState(false); // Popup state

  useEffect(() => {
    if (location.state?.message) {
      setMessage(location.state.message);
    }
  }, [location.state]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const toggleForm = () => {
    setIsSellerLogin(!isSellerLogin);
    setFormData((prevState) => ({
      ...prevState,
      userType: !isSellerLogin ? "seller" : "customer",
    }));
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.email.trim()) newErrors.email = "Email is required";
    else if (!/\S+@\S+\.\S+/.test(formData.email))
      newErrors.email = "Email is invalid";
    if (!formData.password) newErrors.password = "Password is required";
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
    try {
      const response = await fetch("http://localhost:8081/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Login failed");
      }

      const userData = await response.json();
      localStorage.setItem("token", userData.token);
      localStorage.setItem("userType", userData.userType);
      localStorage.setItem("userId", userData.id);

      if (userData.userType === "customer") {
        navigate("/customer/dashboard");
      } else {
        navigate("/seller/dashboard");
      }
    } catch (error) {
      setErrors({
        general: error.message || "Login failed. Please check your credentials.",
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="signin-container">
      <div className="signin-box">
        <h2 className="signin-title">
          {isSellerLogin ? "Seller" : "Customer"} Login
        </h2>
        <p className="signin-subtitle">Welcome back to Village Cart!</p>

        {message && <div className="success-message">{message}</div>}

        <div className="toggle-buttons">
          <button
            className={!isSellerLogin ? "active" : ""}
            onClick={() => (!isSellerLogin ? null : toggleForm())}
          >
            Customer
          </button>
          <button
            className={isSellerLogin ? "active" : ""}
            onClick={() => (isSellerLogin ? null : toggleForm())}
          >
            Seller
          </button>
        </div>

        {errors.general && (
          <div className="error-message">{errors.general}</div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <FaEnvelope className="input-icon" />
            <input
              type="email"
              name="email"
              placeholder="Email Address"
              value={formData.email}
              onChange={handleChange}
            />
          </div>
          {errors.email && <div className="error-message">{errors.email}</div>}

          <div className="input-group">
            <FaLock className="input-icon" />
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
            />
          </div>
          {errors.password && (
            <div className="error-message">{errors.password}</div>
          )}

          <div className="forgot-password">
            <button
              type="button"
              className="link-button"
              onClick={() => setIsPopupOpen(true)}
            >
              Forgot Password?
            </button>
          </div>

          <button type="submit" className="submit-btn" disabled={isLoading}>
            {isLoading ? "Logging In..." : "Login"}
          </button>
        </form>

        <div className="signup-link">
          Don’t have an account? <Link to="/signup">Sign Up</Link>
        </div>
      </div>

    
      <VerifyEmailPopup
        isOpen={isPopupOpen}
        onClose={() => setIsPopupOpen(false)}
      />
    </div>
  );
};

export default Signin;
*/


import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../assets/signin.css"
import VerifyEmailPopup from "./VerifyEmailPopup"; // Import the popup component

const Signin = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    userType: "", // Initially empty, user selects role
  });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [isPopupOpen, setIsPopupOpen] = useState(false); // State for forgot password popup

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleRoleSelect = (role) => {
    setFormData((prevState) => ({
      ...prevState,
      userType: role,
    }));
    setError(""); // Clear any previous errors
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.userType) {
      setError("Please select a role.");
      return;
    }

    setIsLoading(true);
    setError("");

    try {
      // Determine the backend endpoint based on the selected role
      let endpoint = "http://localhost:8080"; // Changed to 8080
      if (formData.userType === "customer") {
        endpoint += "/customer/login";
      } else if (formData.userType === "seller") {
        endpoint += "/seller/login";
      }

      // Create login request body to match backend LoginRequest
      const loginRequest = {
        email: formData.email,
        password: formData.password
      };

      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(loginRequest)
      });

      if (!response.ok) {
        throw new Error("Invalid email or password");
      }

      const data = await response.json();

      // Store user data based on role
      if (formData.userType === "customer") {
        localStorage.setItem("customerId", data.customerId);
        localStorage.setItem("customerEmail", data.customerEmail);
        localStorage.setItem("userType", "customer");
        navigate("/customer/dashboard");
      } else if (formData.userType === "seller") {
        localStorage.setItem("sellerId", data.sellerId);
        localStorage.setItem("sellerEmail", data.sellerEmail);
        localStorage.setItem("sellerName", `${data.sellerFirstName} ${data.sellerLastName}`);
        localStorage.setItem("userType", "seller");
        navigate("/seller/dashboard");
      }

    } catch (error) {
      console.error('Login error:', error);
      setError(error.message || "Login failed. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="signin-container">
      <div className="signin-box">
        {formData.userType ? (
          <h1 className="signin-title">{`${formData.userType.charAt(0).toUpperCase() + formData.userType.slice(1)} Login`}</h1>
        ) : (
          <h1 className="signin-title">Login</h1>
        )}
        <p className="signin-subtitle">Please select your role and login</p>

        {!formData.userType && (
          <div className="toggle-buttons">
            <button
              className={formData.userType === "customer" ? "active" : ""}
              onClick={() => handleRoleSelect("customer")}
            >
              Customer
            </button>
            <button
              className={formData.userType === "seller" ? "active" : ""}
              onClick={() => handleRoleSelect("seller")}
            >
              Seller
            </button>
            <button
              className={formData.userType === "admin" ? "active" : ""}
              onClick={() => handleRoleSelect("admin")}
            >
              Admin
            </button>
          </div>
        )}

        {formData.userType && (
          <form onSubmit={handleSubmit}>
            <div className="input-group">
              <input
                type="email"
                name="email"
                placeholder="Email Address"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            <div className="input-group">
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            {error && <div className="error-message">{error}</div>}

            <div className="forgot-password">
              <button
                type="button"
                className="link-button"
                onClick={() => setIsPopupOpen(true)} // Open the forgot password popup
              >
                Forgot Password?
              </button>
            </div>

            <button type="submit" className="submit-btn" disabled={isLoading}>
              {isLoading ? "Logging in..." : "Login"}
            </button>
          </form>
        )}

        <div className="signup-link">
          <p>
            Don't have an account?{" "}
            <a href="/signup" className="signup-link">
              Sign Up
            </a>
          </p>
        </div>
      </div>

      {/* Forgot Password Popup */}
      <VerifyEmailPopup
        isOpen={isPopupOpen}
        onClose={() => setIsPopupOpen(false)} // Close the popup
      />
    </div>
  );
};

export default Signin;