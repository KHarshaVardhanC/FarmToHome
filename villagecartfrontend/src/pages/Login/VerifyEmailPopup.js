/*import React, { useState } from "react";
import "../../assets/popup.css";

const VerifyEmailPopup = ({ isOpen, onClose }) => {
  const [step, setStep] = useState("verifyEmail"); // "verifyEmail" or "changePassword"
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  const handleSendOtp = async (e) => {
    e.preventDefault();
    if (!email.trim()) {
      setError("Email is required");
      return;
    }
    if (!/\S+@\S+\.\S+/.test(email)) {
      setError("Invalid email format");
      return;
    }

    setError("");
    setIsLoading(true);
    try {
      const response = await fetch("http://localhost:5000/api/verifyemail", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to send OTP");
      }

      setSuccessMessage("OTP sent successfully! Check your email.");
      setTimeout(() => {
        setStep("changePassword"); // Move to the next step
        setSuccessMessage("");
      }, 2000);
    } catch (error) {
      setError(error.message || "Failed to send OTP. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (!newPassword.trim() || !repeatPassword.trim()) {
      setError("Both password fields are required");
      return;
    }
    if (newPassword !== repeatPassword) {
      setError("Passwords do not match");
      return;
    }

    setError("");
    setIsLoading(true);
    try {
      const response = await fetch("http://localhost:5000/api/changepassword", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, newPassword }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to change password");
      }

      setSuccessMessage("Password changed successfully! You can now log in.");
      setTimeout(() => {
        onClose(); // Close the popup
      }, 2000);
    } catch (error) {
      setError(error.message || "Failed to change password. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="popup-overlay">
      <div className="popup-content">
        <button className="close-btn" onClick={onClose}>
          &times;
        </button>
        {step === "verifyEmail" ? (
          <>
            <h2>Verify Email</h2>
            <form onSubmit={handleSendOtp}>
              <div className="form-group">
                <input
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className={error ? "error" : ""}
                />
                {error && <div className="error-message">{error}</div>}
              </div>
              <button type="submit" className="submit-btn" disabled={isLoading}>
                {isLoading ? "Sending OTP..." : "Send OTP"}
              </button>
            </form>
          </>
        ) : (
          <>
            <h2>Change Password</h2>
            <form onSubmit={handleChangePassword}>
              <div className="form-group">
                <input
                  type="email"
                  placeholder="Email"
                  value={email}
                  disabled
                  className="disabled-input"
                />
              </div>
              <div className="form-group">
                <input
                  type="password"
                  placeholder="New Password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className={error ? "error" : ""}
                />
              </div>
              <div className="form-group">
                <input
                  type="password"
                  placeholder="Repeat Password"
                  value={repeatPassword}
                  onChange={(e) => setRepeatPassword(e.target.value)}
                  className={error ? "error" : ""}
                />
                {error && <div className="error-message">{error}</div>}
              </div>
              <button type="submit" className="submit-btn" disabled={isLoading}>
                {isLoading ? "Changing Password..." : "Change Password"}
              </button>
            </form>
          </>
        )}
        {successMessage && <div className="success-message">{successMessage}</div>}
      </div>
    </div>
  );
};

export default VerifyEmailPopup;

*/





/*

import React, { useState } from "react";
import "../../assets/popup.css";

const VerifyEmailPopup = ({ isOpen, onClose }) => {
  const [step, setStep] = useState("verifyEmail"); // "verifyEmail" or "changePassword"
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  const handleSendOtp = async (e) => {
    e.preventDefault();
    if (!email.trim()) {
      setError("Email is required");
      return;
    }
    if (!/\S+@\S+\.\S+/.test(email)) {
      setError("Invalid email format");
      return;
    }

    setError("");
    setIsLoading(true);

    // Simulate a delay to mimic backend processing
    setTimeout(() => {
      setSuccessMessage("OTP sent successfully! Check your email.");
      setTimeout(() => {
        setStep("changePassword"); // Move to the next step
        setSuccessMessage("");
      }, 2000);
      setIsLoading(false);
    }, 1000);
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (!newPassword.trim() || !repeatPassword.trim()) {
      setError("Both password fields are required");
      return;
    }
    if (newPassword !== repeatPassword) {
      setError("Passwords do not match");
      return;
    }

    setError("");
    setIsLoading(true);

    // Simulate a delay to mimic backend processing
    setTimeout(() => {
      setSuccessMessage("Password changed successfully! You can now log in.");
      setTimeout(() => {
        onClose(); // Close the popup
      }, 2000);
      setIsLoading(false);
    }, 1000);
  };

  if (!isOpen) return null;

  return (
    <div className="popup-overlay">
      <div className="popup-content">
        <button className="close-btn" onClick={onClose}>
          &times;
        </button>
        {step === "verifyEmail" ? (
          <>
            <h2>Verify Email</h2>
            <form onSubmit={handleSendOtp}>
              <div className="form-group">
                <input
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className={error ? "error" : ""}
                />
                {error && <div className="error-message">{error}</div>}
              </div>
              <button type="submit" className="submit-btn" disabled={isLoading}>
                {isLoading ? "Sending OTP..." : "Send OTP"}
              </button>
            </form>
          </>
        ) : (
          <>
            <h2>Change Password</h2>
            <form onSubmit={handleChangePassword}>
              <div className="form-group">
              <input
                type="email"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="popup-input"
              />
              </div>
              <div className="form-group">
                <input
                  type="password"
                  placeholder="New Password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className={error ? "error" : ""}
                />
              </div>
              <div className="form-group">
                <input
                  type="password"
                  placeholder="Repeat Password"
                  value={repeatPassword}
                  onChange={(e) => setRepeatPassword(e.target.value)}
                  className={error ? "error" : ""}
                />
                {error && <div className="error-message">{error}</div>}
              </div>
              <button type="submit" className="submit-btn" disabled={isLoading}>
                {isLoading ? "Changing Password..." : "Change Password"}
              </button>
            </form>
          </>
        )}
        {successMessage && <div className="success-message">{successMessage}</div>}
      </div>
    </div>
  );
};

export default VerifyEmailPopup;


*/

import React, { useState } from "react";
import "../../assets/popup.css";

const VerifyEmailPopup = ({ isOpen, onClose }) => {
  const [step, setStep] = useState("verifyEmail"); // "verifyEmail", "verifyOtp", or "changePassword"
  const [email, setEmail] = useState("");
  const [userType, setUserType] = useState(""); // Role: "customer", "seller", or "admin"
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  // Handle sending OTP
// Update the handleSendOtp function
const handleSendOtp = async (e) => {
  e.preventDefault();
  if (!email.trim()) {
    setError("Email is required");
    return;
  }
  if (!userType) {
    setError("Please select a role");
    return;
  }
  if (!/\S+@\S+\.\S+/.test(email)) {
    setError("Invalid email format");
    return;
  }

  setError("");
  setIsLoading(true);

  try {
    const response = await fetch(`${API_BASE_URL}/auth/verifyEmail/${userType}/${email}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json(); // Parse the JSON response only once
    console.log("Backend Response:", data);

    if (!response.ok) {
      throw new Error(data.error || data.message || "Failed to send OTP");
    }

    setSuccessMessage(data.message || "OTP sent successfully! Check your email.");
    setTimeout(() => {
      setStep("verifyOtp"); // Move to the OTP verification step
      setSuccessMessage("");
    }, 2000);
  } catch (error) {
    console.error("Error in handleSendOtp:", error);
    setError(error.message || "Failed to send OTP. Please try again.");
  } finally {
    setIsLoading(false);
  }
};

  // Handle verifying OTP
  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    if (!otp.trim()) {
      setError("OTP is required");
      return;
    }

    setError("");
    setIsLoading(true);

    try {
      const response = await fetch(`${API_BASE_URL}/auth/verifyOtp/${email}/${otp}`, {
        method: "POST",
      });

      const text = await response.text();
      console.log("Raw response:", text);

      let data;
      try {
        data = JSON.parse(text);
      } catch (e) {
        // If the response is not JSON, use the text as the message
        data = { message: text };
      }

      console.log("Backend Response:", data);

      if (!response.ok) {
        throw new Error(data.message || "Failed to verify OTP");
      }

      setSuccessMessage(data.message || "OTP verified successfully!");
      setTimeout(() => {
        setStep("changePassword"); // Move to the password reset step
        setSuccessMessage("");
      }, 2000);
    } catch (error) {
      console.error("Error in handleVerifyOtp:", error);
      setError(error.message || "Failed to verify OTP. Please try again.");
    } finally {
      setIsLoading(false);
    }
};
  // Handle changing password
  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (!newPassword.trim() || !repeatPassword.trim()) {
      setError("Both password fields are required");
      return;
    }
    if (newPassword !== repeatPassword) {
      setError("Passwords do not match");
      return;
    }

    setError("");
    setIsLoading(true);

    try {
      const response = await fetch("${API_BASE_URL}/auth/resetPassword", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email,
          userType,
          newPassword,
        }),
      });

      const text = await response.text();
      console.log("Raw response:", text);

      let data;
      try {
        data = JSON.parse(text);
      } catch (e) {
        // If the response is not JSON, use the text as the message
        data = { message: text };
      }

      console.log("Backend Response:", data);

      if (!response.ok) {
        throw new Error(data.message || "Failed to change password");
      }

      setSuccessMessage(data.message || "Password changed successfully! You can now log in.");
      setTimeout(() => {
        onClose(); // Close the popup
      }, 2000);
    } catch (error) {
      console.error("Error in handleChangePassword:", error);
      setError(error.message || "Failed to change password. Please try again.");
    } finally {
      setIsLoading(false);
    }
};

  if (!isOpen) return null;

  return (
    <div className="popup-overlay">
      <div className="popup-content">
      <button className="close-button" onClick={onClose}>
          <span className="close-icon">×</span>
        </button>
        {step === "verifyEmail" && (
          <>
            <h2>Verify Email</h2>
            <form onSubmit={handleSendOtp}>
              <div className="form-group">
                <input
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className={error ? "error" : ""}
                />
              </div>
              <div className="form-group">
                <select
                  value={userType}
                  onChange={(e) => setUserType(e.target.value)}
                  className={error ? "error" : ""}
                >
                  <option value="">Select Role</option>
                  <option value="customer">Customer</option>
                  <option value="seller">Seller</option>
                  <option value="admin">Admin</option>
                </select>
              </div>
              {error && <div className="error-message">{error}</div>}
              <button type="submit" className="submit-btn" disabled={isLoading}>
                {isLoading ? "Sending OTP..." : "Send OTP"}
              </button>
            </form>
          </>
        )}
        {step === "verifyOtp" && (
          <>
            <h2>Verify OTP</h2>
            <form onSubmit={handleVerifyOtp}>
              <div className="form-group">
                <input
                  type="text"
                  placeholder="Enter OTP"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  className={error ? "error" : ""}
                />
                {error && <div className="error-message">{error}</div>}
              </div>
              <button type="submit" className="submit-btn" disabled={isLoading}>
                {isLoading ? "Verifying OTP..." : "Verify OTP"}
              </button>
            </form>
          </>
        )}
        {step === "changePassword" && (
          <>
            <h2>Change Password</h2>
            <form onSubmit={handleChangePassword}>
              <div className="form-group">
                <input
                  type="password"
                  placeholder="New Password"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className={error ? "error" : ""}
                />
              </div>
              <div className="form-group">
                <input
                  type="password"
                  placeholder="Repeat Password"
                  value={repeatPassword}
                  onChange={(e) => setRepeatPassword(e.target.value)}
                  className={error ? "error" : ""}
                />
                {error && <div className="error-message">{error}</div>}
              </div>
              <button type="submit" className="submit-btn" disabled={isLoading}>
                {isLoading ? "Changing Password..." : "Change Password"}
              </button>
            </form>
          </>
        )}
        {successMessage && <div className="success-message">{successMessage}</div>}
      </div>
    </div>
  );
};

export default VerifyEmailPopup;