import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import ProfileDropdown from '../components/ProfileDropdown';
import { sellerApi } from '../utils/api';

const Profile = () => {
  const [profile, setProfile] = useState({
    sellerFirstName: '',
    sellerLastName: '',
    sellerEmail: '',
    sellerMobileNumber: '',
    sellerCity: '',
    sellerState: '',
    sellerPincode: '',
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  
  // Replace with actual seller ID from authentication
  const sellerId = 2; // This should come from your auth context

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await sellerApi.getProfile(sellerId);
        setProfile(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch profile');
        setLoading(false);
      }
    };

    fetchProfile();
  }, [sellerId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfile(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      console.log('Updating profile with data:', profile);
      await sellerApi.updateProfile(sellerId, profile);
      console.log('Profile updated successfully');
      setIsEditing(false);
    } catch (err) {
      console.error('Error updating profile:', err.response?.data || err.message);
      setError('Failed to update profile');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="profile-page">
      {/* Top Navigation */}
      <nav className="navbar navbar-light bg-white">
        <div className="container-fluid px-4">
          <div className="d-flex align-items-center">
            <Link to="/" className="text-decoration-none">
              <div className="logo text-dark d-flex align-items-center">
                <i className="fas fa-leaf text-success me-2" style={{ fontSize: '24px' }}></i>
                <span className="fw-bold">FarmToHome</span>
              </div>
            </Link>
          </div>

          <div className="d-flex flex-grow-1 mx-4">
            <div className="input-group">
              <input
                type="text"
                className="form-control border-end-0"
                placeholder="Search"
              />
              <button className="btn btn-outline-secondary border-start-0">
                <i className="fas fa-search"></i>
              </button>
            </div>
          </div>

          <div className="d-flex align-items-center gap-3">
            <Link to="/add-product" className="btn btn-primary px-4">Add Product</Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container-fluid px-4 py-4">
        <div className="row justify-content-center">
          <div className="col-lg-8">
            <div className="card">
              <div className="card-body">
                <div className="d-flex justify-content-between align-items-center mb-4">
                  <h4 className="card-title mb-0">Profile Details</h4>
                  {!isEditing ? (
                    <button className="btn btn-primary" onClick={() => setIsEditing(true)}>
                      <i className="fas fa-edit me-2"></i>
                      Edit Profile
                    </button>
                  ) : (
                    <div className="d-flex gap-2">
                      <button className="btn btn-success" onClick={handleSubmit}>
                        <i className="fas fa-save me-2"></i>
                        Save
                      </button>
                      <button className="btn btn-secondary" onClick={() => setIsEditing(false)}>
                        <i className="fas fa-times me-2"></i>
                        Cancel
                      </button>
                    </div>
                  )}
                </div>

                {error && <div className="alert alert-danger">{error}</div>}

                <form onSubmit={handleSubmit}>
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">First Name</label>
                      <input
                        type="text"
                        className="form-control"
                        name="sellerFirstName"
                        value={profile.sellerFirstName}
                        onChange={handleChange}
                        disabled={!isEditing}
                        required
                      />
                    </div>

                    <div className="col-md-6 mb-3">
                      <label className="form-label">Last Name</label>
                      <input
                        type="text"
                        className="form-control"
                        name="sellerLastName"
                        value={profile.sellerLastName}
                        onChange={handleChange}
                        disabled={!isEditing}
                        required
                      />
                    </div>
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Email</label>
                    <input
                      type="email"
                      className="form-control"
                      name="sellerEmail"
                      value={profile.sellerEmail}
                      onChange={handleChange}
                      disabled={!isEditing}
                      required
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Mobile Number</label>
                    <input
                      type="tel"
                      className="form-control"
                      name="sellerMobileNumber"
                      value={profile.sellerMobileNumber}
                      onChange={handleChange}
                      disabled={!isEditing}
                      required
                    />
                  </div>

                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">City</label>
                      <input
                        type="text"
                        className="form-control"
                        name="sellerCity"
                        value={profile.sellerCity}
                        onChange={handleChange}
                        disabled={!isEditing}
                        required
                      />
                    </div>

                    <div className="col-md-6 mb-3">
                      <label className="form-label">State</label>
                      <input
                        type="text"
                        className="form-control"
                        name="sellerState"
                        value={profile.sellerState}
                        onChange={handleChange}
                        disabled={!isEditing}
                        required
                      />
                    </div>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Pincode</label>
                    <input
                      type="text"
                      className="form-control"
                      name="sellerPincode"
                      value={profile.sellerPincode}
                      onChange={handleChange}
                      disabled={!isEditing}
                      required
                      pattern="[0-9]{6}"
                      maxLength="6"
                      placeholder="Enter 6-digit pincode"
                    />
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile; 