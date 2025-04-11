import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../assets/signin.css";
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
      let endpoint = "";
      if (formData.userType === "customer") {
        endpoint = "http://localhost:8081/customer/login";
      } else if (formData.userType === "seller") {
        endpoint = "http://localhost:8081/seller/login";
      } else if (formData.userType === "admin") {
        endpoint = "http://localhost:8081/admin/login";
      }

      // Send login request to the backend
      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Login failed");
      }

      const data = await response.json();
      console.log("Login successful:", data);

      // Navigate to the dashboard or home page after successful login
      navigate("/dashboard", { state: { user: data } });
    } catch (error) {
      setError(error.message || "Login failed. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="signin-container">
      <div className="signin-box">
        {formData.userType ? (
         <h1 className="signin-title">
         {formData.userType.charAt(0).toUpperCase() + formData.userType.slice(1)} Login
       </h1>
       
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