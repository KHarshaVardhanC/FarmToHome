// ProfileModal.js - Updated to fetch customer details from API
import React, { useState, useEffect } from 'react';
import '../styles/ProfileModal.css';

function ProfileModal({ onClose }) {
  const [customer, setCustomer] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    city: '',
    state: ''
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchCustomerProfile();
  }, []);

  const fetchCustomerProfile = async () => {
    try {
      setLoading(true);
      // Get auth token from localStorage (assuming you store it there after login)
      const token = localStorage.getItem('customerToken');
      
      if (!token) {
        throw new Error('Authentication required. Please login again.');
      }
      
      // Make API call to fetch customer profile
      const response = await fetch('http://your-backend-url/api/customer/profile', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      
      if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
      }
      
      const customerData = await response.json();
      setCustomer(customerData);
      setFormData(customerData);
      setLoading(false);
    } catch (err) {
      console.error("Failed to fetch customer profile:", err);
      setError(err.message);
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      setLoading(true);
      const token = localStorage.getItem('customerToken');
      
      // Send updated profile data to backend
      const response = await fetch('http://your-backend-url/api/customer/profile', {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });
      
      if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
      }
      
      // Get updated profile from response
      const updatedProfile = await response.json();
      
      // Update local state
      setCustomer(updatedProfile);
      setIsEditing(false);
      setLoading(false);
      
      // Show success message
      alert('Profile updated successfully!');
    } catch (err) {
      console.error("Failed to update profile:", err);
      setError(err.message);
      setLoading(false);
      alert(`Failed to update profile: ${err.message}`);
    }
  };

  return (
    <div className="profile-modal-overlay" onClick={onClose}>
      <div className="profile-modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Customer Profile</h2>
          <button className="close-button" onClick={onClose}>&times;</button>
        </div>
        
        {loading && (
          <div className="loading">Loading profile...</div>
        )}
        
        {error && (
          <div className="error-message">
            <p>{error}</p>
            <button onClick={fetchCustomerProfile} className="retry-btn">Try Again</button>
          </div>
        )}
        
        {!loading && !error && (
          <>
            {isEditing ? (
              <form onSubmit={handleSubmit} className="edit-profile-form">
                <div className="form-group">
                  <label htmlFor="name">Name</label>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="email">Email</label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="phone">Phone Number</label>
                  <input
                    type="tel"
                    id="phone"
                    name="phone"
                    value={formData.phone}
                    onChange={handleChange}
                    required
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="city">City</label>
                  <input
                    type="text"
                    id="city"
                    name="city"
                    value={formData.city}
                    onChange={handleChange}
                    required
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="state">State</label>
                  <input
                    type="text"
                    id="state"
                    name="state"
                    value={formData.state}
                    onChange={handleChange}
                    required
                  />
                </div>
                
                <div className="form-actions">
                  <button type="submit" className="save-button" disabled={loading}>
                    {loading ? 'Saving...' : 'Save Changes'}
                  </button>
                  <button 
                    type="button" 
                    className="cancel-button"
                    onClick={() => {
                      setFormData(customer);
                      setIsEditing(false);
                    }}
                    disabled={loading}
                  >
                    Cancel
                  </button>
                </div>
              </form>
            ) : (
              <div className="profile-details">
                <div className="profile-field">
                  <span className="field-label">Name:</span>
                  <span className="field-value">{customer?.name}</span>
                </div>
                
                <div className="profile-field">
                  <span className="field-label">Email:</span>
                  <span className="field-value">{customer?.email}</span>
                </div>
                
                <div className="profile-field">
                  <span className="field-label">Phone Number:</span>
                  <span className="field-value">{customer?.phone}</span>
                </div>
                
                <div className="profile-field">
                  <span className="field-label">City:</span>
                  <span className="field-value">{customer?.city}</span>
                </div>
                
                <div className="profile-field">
                  <span className="field-label">State:</span>
                  <span className="field-value">{customer?.state}</span>
                </div>
                
                <button 
                  className="edit-profile-button"
                  onClick={() => setIsEditing(true)}
                >
                  Edit Profile
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default ProfileModal;