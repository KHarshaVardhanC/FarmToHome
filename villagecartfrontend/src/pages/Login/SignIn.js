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
      } else if (formData.userType === "admin") {
        endpoint += "/admin/login";
      }

      const requestBody = {
        email: formData.email,
        password: formData.password,
      };

      console.log(`Attempting login to ${endpoint} with email: ${formData.email}`);

      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Login failed.");
      }

      const data = await response.json();
      console.log("Login successful:", data);

      // Handle login for different user types
      if (formData.userType === "customer") {
        localStorage.setItem("customerId", data.customerId); // Store customerId in local storage
        localStorage.setItem("customerEmail", data.customerEmail);
        localStorage.setItem("userType", "customer");
        navigate("/customer-home"); // Redirect to customer homepage
      } else if (formData.userType === "seller") {
        localStorage.setItem("sellerId", data.sellerId);
        localStorage.setItem("sellerEmail", data.sellerEmail);
        localStorage.setItem("userType", "seller");
        navigate("/SellerHome"); // Redirect to seller homepage
      } else if (formData.userType === "admin") {
        localStorage.setItem("adminId", data.adminId);
        localStorage.setItem("adminEmail", data.adminEmail);
        localStorage.setItem("userType", "admin");
        navigate("/admin"); // Redirect to admin dashboard
      }
    } catch (error) {
      console.error("Login error:", error);
      setError(error.message || "Login failed. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const resetSelection = () => {
    setFormData((prev) => ({
      ...prev,
      userType: "",
    }));
    setError("");
  };

  return (
    <div className="signin-container">
      <div className="signin-box">
        {formData.userType ? (
          <h1 className="signin-title">
            {`${formData.userType.charAt(0).toUpperCase() + formData.userType.slice(1)} Login`}
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
          </div>
        )}

        {formData.userType && (
          <>
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

            <button
              onClick={resetSelection}
              className="back-button"
              style={{
                marginTop: "10px",
                background: "transparent",
                border: "1px solid #ccc",
                padding: "8px 15px",
                borderRadius: "4px",
                cursor: "pointer",
              }}
            >
              Back to Selection
            </button>
          </>
        )}

        {/* Hidden admin login option */}
        {!formData.userType && (
          <div className="admin-login-section" style={{ marginTop: "20px" }}>
            <p>
              <a
                href="#"
                onClick={(e) => {
                  e.preventDefault();
                  handleRoleSelect("admin");
                }}
                style={{
                  fontSize: "0.8rem",
                  color: "#777",
                  textDecoration: "none",
                }}
              >
                Admin Login
              </a>
            </p>
          </div>
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