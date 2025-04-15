import React, { useEffect, useState } from "react";
import "../styles/MyProfile.css";

const MyProfile = () => {
  const [customerDetails, setCustomerDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editedDetails, setEditedDetails] = useState({});

  useEffect(() => {
    const fetchCustomerDetails = async () => {
      const customerId = localStorage.getItem("customerId");
      console.log("Customer ID from local storage:", customerId);

      if (!customerId) {
        setError("Customer ID not found. Please log in again.");
        setLoading(false);
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/customer/${customerId}`);
        console.log("API Response Status:", response.status);

        if (!response.ok) {
          const errorData = await response.json();
          console.error("Error fetching customer details:", errorData);
          throw new Error(errorData.message || "Failed to fetch customer details.");
        }

        const data = await response.json();
        console.log("Fetched Customer Details:", data);

        // Set the fetched customer details in state
        setCustomerDetails(data);
        setEditedDetails(data);
      } catch (error) {
        console.error("Error fetching customer details:", error);
        setError(error.message || "Failed to fetch customer details.");
      } finally {
        setLoading(false);
      }
    };

    fetchCustomerDetails();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditedDetails({
      ...editedDetails,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const customerId = localStorage.getItem("customerId");

    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8080/customer/${customerId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editedDetails)
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to update profile.");
      }

      setCustomerDetails(editedDetails);
      setIsEditing(false);
    } catch (error) {
      console.error("Error updating profile:", error);
      setError(error.message || "Failed to update profile.");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading customer details...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="my-profile-container">
      <h1>My Profile</h1>
      {customerDetails ? (
        <div className="profile-details">
          {isEditing ? (
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>First Name:</label>
                <input
                  type="text"
                  name="customerFirstName"
                  value={editedDetails.customerFirstName || ""}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label>Last Name:</label>
                <input
                  type="text"
                  name="customerLastName"
                  value={editedDetails.customerLastName || ""}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label>Email:</label>
                <input
                  type="email"
                  name="customerEmail"
                  value={editedDetails.customerEmail || ""}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label>Phone Number:</label>
                <input
                  type="tel"
                  name="customerPhoneNumber"
                  value={editedDetails.customerPhoneNumber || ""}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label>Place:</label>
                <input
                  type="text"
                  name="customerPlace"
                  value={editedDetails.customerPlace || ""}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label>City:</label>
                <input
                  type="text"
                  name="customerCity"
                  value={editedDetails.customerCity || ""}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label>State:</label>
                <input
                  type="text"
                  name="customerState"
                  value={editedDetails.customerState || ""}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label>Pincode:</label>
                <input
                  type="text"
                  name="customerPincode"
                  value={editedDetails.customerPincode || ""}
                  onChange={handleInputChange}
                />
              </div>
              <div className="button-group">
                <button type="submit">Save</button>
                <button type="button" onClick={() => setIsEditing(false)}>Cancel</button>
              </div>
            </form>
          ) : (
            <>
              <p><strong>Name:</strong> {customerDetails.customerFirstName} {customerDetails.customerLastName}</p>
              <p><strong>Email:</strong> {customerDetails.customerEmail}</p>
              <p><strong>Phone Number:</strong> {customerDetails.customerPhoneNumber || "N/A"}</p>
              <p><strong>Address:</strong> {customerDetails.customerPlace}, {customerDetails.customerCity}, {customerDetails.customerState} - {customerDetails.customerPincode}</p>
              <button className="edit-button" onClick={() => setIsEditing(true)}>
                Edit Profile
              </button>
            </>
          )}
        </div>
      ) : (
        <p>No customer details available.</p>
      )}
    </div>
  );
};

export default MyProfile;