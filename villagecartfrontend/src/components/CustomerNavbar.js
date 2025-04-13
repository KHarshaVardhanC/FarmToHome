import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';

function CustomerNavbar({ cartCount = 0, searchTerm = '', onSearch, userName = '' }) {
  const [showSearch, setShowSearch] = useState(false);
  const [showProfileDropdown, setShowProfileDropdown] = useState(false);
  const searchInputRef = useRef(null);
  const profileDropdownRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (showSearch && searchInputRef.current) {
      searchInputRef.current.focus();
    }
  }, [showSearch]);

  useEffect(() => {
    // Handle clicks outside the profile dropdown
    function handleClickOutside(event) {
      if (profileDropdownRef.current && !profileDropdownRef.current.contains(event.target)) {
        setShowProfileDropdown(false);
      }
    }

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleSearchChange = (e) => {
    if (onSearch) {
      onSearch(e.target.value);
    }
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    // Additional search submit logic if needed
  };

  const toggleSearch = () => {
    setShowSearch(!showSearch);
  };

  const toggleProfileDropdown = () => {
    setShowProfileDropdown(!showProfileDropdown);
  };

  const handleLogout = () => {
    // Clear user data from localStorage
    localStorage.removeItem('userName');
    localStorage.removeItem('customerId');
    localStorage.removeItem('cart');
    
    // Redirect to home page
    navigate('/');
  };

  return (
    <nav className="customer-navbar">
      <div className="navbar-container">
        <Link to="/customer-home" className="navbar-logo">
          <span className="logo-text">Fresh</span>
          <span className="logo-text-accent">Market</span>
        </Link>

        <div className="navbar-search-container">
          {showSearch && (
            <form onSubmit={handleSearchSubmit} className="search-form">
              <input
                ref={searchInputRef}
                type="text"
                placeholder="Search products..."
                value={searchTerm}
                onChange={handleSearchChange}
                className="search-input"
              />
              <button type="button" className="search-close-btn" onClick={toggleSearch}>
                ✕
              </button>
            </form>
          )}
        </div>

        <div className="navbar-actions">
          {!showSearch && (
            <button className="action-btn search-icon" onClick={toggleSearch}>
              <i className="fas fa-search"></i>
              <span className="visually-hidden">Search</span>
            </button>
          )}

          {userName && (
            <div className="welcome-text">
              Welcome, {userName}
            </div>
          )}

          <Link to="/cart" className="action-btn cart-icon">
            <i className="fas fa-shopping-cart"></i>
            {cartCount > 0 && <span className="cart-count">{cartCount}</span>}
            <span className="visually-hidden">Cart</span>
          </Link>

          <div className="profile-dropdown-container" ref={profileDropdownRef}>
            <button className="action-btn profile-icon" onClick={toggleProfileDropdown}>
              <i className="fas fa-user"></i>
              <span className="visually-hidden">Profile</span>
            </button>

            {showProfileDropdown && (
              <div className="profile-dropdown">
                <Link to="/customer-profile" className="dropdown-item">
                  <i className="fas fa-user-circle"></i> My Profile
                </Link>
                <Link to="/my-orders" className="dropdown-item">
                  <i className="fas fa-shopping-bag"></i> My Orders
                </Link>
                <button onClick={handleLogout} className="dropdown-item">
                  <i className="fas fa-sign-out-alt"></i> Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}

export default CustomerNavbar;