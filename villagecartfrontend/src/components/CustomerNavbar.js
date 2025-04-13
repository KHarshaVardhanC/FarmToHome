import React, { useState, useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';

const CustomerNavbar = ({ cartCount, searchTerm, onSearch, userName }) => {
  const [showDropdown, setShowDropdown] = useState(false);
  const dropdownRef = useRef(null);
  const navigate = useNavigate();

  const handleSearchChange = (e) => {
    onSearch(e.target.value);
  };

  const toggleDropdown = () => {
    setShowDropdown(!showDropdown);
  };

  const handleLogout = () => {
    // Clear localStorage
    localStorage.removeItem('userName');
    localStorage.removeItem('customerId');
    localStorage.removeItem('cart');
    
    // Redirect to home page
    navigate('/');
  };

  // Close dropdown when clicking outside
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [dropdownRef]);

  return (
    <nav className="customer-navbar">
      <div className="navbar-container">
        <Link to="/customer-home" className="navbar-brand">
          <span className="brand-text">Krishi Connect</span>
        </Link>

        <div className="navbar-search">
          <input
            type="text"
            placeholder="Search products..."
            value={searchTerm}
            onChange={handleSearchChange}
          />
          <button className="search-btn">
            <i className="fas fa-search"></i>
          </button>
        </div>

        <div className="navbar-actions">
          {userName && (
            <div className="welcome-message">
              Welcome, <span className="user-name">{userName}</span>
            </div>
          )}

          <Link to="/cart" className="cart-icon">
            <i className="fas fa-shopping-cart"></i>
            {cartCount > 0 && <span className="cart-count">{cartCount}</span>}
          </Link>

          <div className="profile-dropdown" ref={dropdownRef}>
            <button className="profile-btn" onClick={toggleDropdown}>
              <i className="fas fa-user-circle"></i>
            </button>

            {showDropdown && (
              <div className="dropdown-menu">
                <Link to="/profile" className="dropdown-item">
                  <i className="fas fa-user"></i> My Profile
                </Link>
                <Link to="/my-orders" className="dropdown-item">
                  <i className="fas fa-box"></i> My Orders
                </Link>
                <div className="dropdown-divider"></div>
                <button onClick={handleLogout} className="dropdown-item logout-btn">
                  <i className="fas fa-sign-out-alt"></i> Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default CustomerNavbar;