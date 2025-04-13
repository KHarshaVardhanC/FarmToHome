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
      // Updated endpoint URLs
      let endpoint = "http://localhost:8080"; // Base URL
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

      console.log("Sending login request to:", endpoint);
      console.log("Request body:", loginRequest);
      console.log(`Attempting login to ${endpoint} with email: ${formData.email}`);

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
        body: JSON.stringify(requestBody)
      });

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

      if (!response.ok) {
        throw new Error(data.message || "Invalid email or password");
      }

      // Updated data handling for different user types
      if (formData.userType === "seller") {
        localStorage.setItem("token", data.token || "token"); // Store JWT token if provided
        localStorage.setItem("sellerId", data.sellerId);
        localStorage.setItem("sellerEmail", data.sellerEmail);
        localStorage.setItem("sellerName", `${data.sellerFirstName || ""} ${data.sellerLastName || ""}`);
        localStorage.setItem("userType", "seller");
        navigate("/seller/dashboard");
      } else if (formData.userType === "customer") {
        localStorage.setItem("token", data.token || "token");
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
      } else if (formData.userType === "admin") {
        localStorage.setItem("token", data.token || "token");
        localStorage.setItem("adminId", data.adminId);
        localStorage.setItem("adminEmail", data.adminEmail);
        localStorage.setItem("userType", "admin");
        navigate("/admin");
      }
    }  
  };

  // Special admin login function
  const attemptAdminLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      const endpoint = "http://localhost:8080/admin/login";
      const requestBody = {
        email: formData.email,
        password: formData.password
      };

      console.log(`Attempting admin login with email: ${formData.email}`);

      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody)
      });

      const responseText = await response.text();
      console.log('Admin response text:', responseText);

      let data;
      try {
        // Try to parse the response text as JSON
        data = responseText ? JSON.parse(responseText) : {};
      } catch (e) {
        // If parsing fails, use the text as message
        data = { message: responseText || 'Unknown error occurred' };
      }

      if (!response.ok) {
        throw new Error(data.message || "Invalid admin credentials");
      }

      // Admin login successful
      localStorage.setItem("token", data.token || "admin_token");
      localStorage.setItem("adminId", data.adminId);
      localStorage.setItem("adminEmail", data.adminEmail);
      localStorage.setItem("userType", "admin");
      navigate("/admin");

    } catch (error) {
      console.error('Admin login error:', error);
      setError(error.message || "Admin login failed. Please try again.");
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
                  textDecoration: "none"
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