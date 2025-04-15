import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Navbar from './CustomerNavbar';
import '../styles/CustomerProfilePage.css';

function CustomerProfilePage() {
  const [customerData, setCustomerData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // In CustomerProfilePage.js
useEffect(() => {
    const fetchCustomerData = async () => {
      try {
        const customerId = localStorage.getItem('customerId');
        console.log("Profile - Customer ID from localStorage:", customerId);
        
        if (!customerId) {
          setError('Please log in to view your profile');
          setLoading(false);
          // Optional: Redirect to login page after a delay
          setTimeout(() => navigate('/signin'), 2000);
          return;
        }
        
        const response = await axios.get(`http://localhost:8080/customer/${customerId}`);
        console.log("Profile - Customer data response:", response.data);
        
        if (response.data) {
          setCustomerData(response.data);
          // Make sure we update the userName in localStorage
          if (response.data.name) {
            localStorage.setItem('userName', response.data.name);
            // Force a refresh of the navbar
            window.dispatchEvent(new Event('storage'));
          }
        } else {
          setError('Unable to load profile data');
        }
        
        setLoading(false);
      } catch (error) {
        console.error('Error fetching customer data:', error);
        setError('Failed to load profile. Please try again.');
        setLoading(false);
      }
    };
  
    fetchCustomerData();
  }, [navigate]);

  if (loading) return (
    <div>
      <Navbar />
      <div className="profile-loading">Loading profile...</div>
    </div>
  );
  
  if (error) return (
    <div>
      <Navbar />
      <div className="profile-container">
        <div className="profile-error">
          <i className="fas fa-exclamation-circle"></i>
          <p>{error}</p>
        </div>
      </div>
    </div>
  );

  return (
    <div className="profile-page">
      <Navbar />
      
      <div className="profile-container">
        <h1>My Profile</h1>
        
        {customerData && (
          <div className="profile-details">
            <div className="profile-header">
              <div className="profile-avatar">
                <i className="fas fa-user-circle"></i>
              </div>
              <div className="profile-name">
                <h2>{customerData.name}</h2>
                <p className="member-since">Member since {customerData.joinDate || 'N/A'}</p>
              </div>
            </div>
            
            <div className="profile-section">
              <h3>Contact Information</h3>
              <div className="info-row">
                <div className="info-label">Email:</div>
                <div className="info-value">{customerData.email}</div>
              </div>
              <div className="info-row">
                <div className="info-label">Phone:</div>
                <div className="info-value">{customerData.phone}</div>
              </div>
            </div>
            
            <div className="profile-section">
              <h3>Address</h3>
              {customerData.address ? (
                <>
                  <div className="info-row">
                    <div className="info-label">Street:</div>
                    <div className="info-value">{customerData.address.street}</div>
                  </div>
                  <div className="info-row">
                    <div className="info-label">City:</div>
                    <div className="info-value">{customerData.address.city}</div>
                  </div>
                  <div className="info-row">
                    <div className="info-label">State:</div>
                    <div className="info-value">{customerData.address.state}</div>
                  </div>
                  <div className="info-row">
                    <div className="info-label">Zip Code:</div>
                    <div className="info-value">{customerData.address.zipCode}</div>
                  </div>
                </>
              ) : (
                <p>No address information available</p>
              )}
            </div>
            
            <div className="profile-actions">
              <button className="edit-profile-btn">Edit Profile</button>
              <button className="change-password-btn">Change Password</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default CustomerProfilePage;