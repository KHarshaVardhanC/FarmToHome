import React, { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { FaEnvelope, FaLock } from "react-icons/fa";
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

  const loginUser = async (credentials) => {
    try {
      const response = await fetch('http://localhost:8080/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        throw new Error('Login failed. Please check your credentials.');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      throw new Error(error.message || 'An error occurred during login.');
    }
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
      const userData = await loginUser(formData);
      localStorage.setItem("token", userData.token);
      localStorage.setItem("userType", userData.userType);
      localStorage.setItem("userId", userData.id);

      if (userData.userType === "customer") {
        navigate("/customer-home"); // Navigate to CustomerHomePage
      } else {
        navigate("/seller/dashboard"); // Navigate to seller dashboard
      }
    } catch (error) {
      setErrors({
        general:
          error.message || "Login failed. Please check your credentials.",
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
            <Link to="/forgot-password">Forgot Password?</Link>
          </div>

          <button type="submit" className="submit-btn" disabled={isLoading}>
            {isLoading ? "Logging In..." : "Login"}
          </button>
        </form>

        <div className="signup-link">
          Don’t have an account? <Link to="/signup">Sign Up</Link>
        </div>
      </div>
    </div>
  );
};

export default Signin;