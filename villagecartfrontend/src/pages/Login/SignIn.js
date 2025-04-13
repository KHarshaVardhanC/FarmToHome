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
      let endpoint = "http://localhost:8080";
      if (formData.userType === "customer") {
        endpoint += "/customer/login";
      } else if (formData.userType === "seller") {
        endpoint += "/seller/login";
      }

      // Create login request body to match backend LoginRequest
      const loginRequest = {
        email: formData.email,
        password: formData.password,
      };

      console.log("Sending login request to:", endpoint);
      console.log("Request body:", loginRequest);

      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(loginRequest),
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error("Login failed:", errorData);
        throw new Error(errorData.message || "Invalid email or password");
      }

      const data = await response.json();
      console.log("Login successful:", data);

      // Store user data based on role
      if (formData.userType === "customer") {
        localStorage.setItem("customerId", data.customerId);
        localStorage.setItem("customerEmail", data.customerEmail);
        localStorage.setItem("userType", "customer");
        navigate("/customer/dashboard");
      } else if (formData.userType === "seller") {
        localStorage.setItem("sellerId", data.sellerId);
        localStorage.setItem("sellerEmail", data.sellerEmail);
        localStorage.setItem(
          "sellerName",
          `${data.sellerFirstName} ${data.sellerLastName}`
        );
        localStorage.setItem("userType", "seller");
        navigate("/SellerHome");
      }
    } catch (error) {
      console.error("Login error:", error);
      setError(error.message || "Login failed. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="signin-container">
      <div className="signin-box">
        {formData.userType ? (
          <h1 className="signin-title">{formData.userType} Login</h1>
        ) : (
          <h1 className="signin-title">Login</h1>
        )}
        <p className="signin-subtitle">Please select your role and login</p>

        {!formData.userType && (
          <div className="toggle-buttons">
            <button
              className={formData.userType === "Customer" ? "active" : ""}
              onClick={() => handleRoleSelect("Customer")}
            >
              Customer
            </button>
            <button
              className={formData.userType === "Seller" ? "active" : ""}
              onClick={() => handleRoleSelect("Seller")}
            >
              Seller
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