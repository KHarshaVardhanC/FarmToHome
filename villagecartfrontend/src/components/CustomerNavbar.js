import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';
import ProfileModal from './ProfileModal';
import { FaShoppingCart, FaUser } from 'react-icons/fa'; // Removed FaSearch import

function CustomerNavbar({ cartCount, searchTerm, onSearch }) {
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [showProfileModal, setShowProfileModal] = useState(false);
  const navigate = useNavigate();
  const profileMenuRef = useRef(null);

  // Close profile menu when clicking outside
  useEffect(() => {
    function handleClickOutside(event) {
      if (profileMenuRef.current && !profileMenuRef.current.contains(event.target)) {
        setShowProfileMenu(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    onSearch(searchTerm); // Call the search handler passed from the parent component
  };

  const handleLogout = () => {
    localStorage.removeItem('customerToken');
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/" className="logo">
          <img src="/logo.png" alt="FarmToHome" />
        </Link>
      </div>

      <div className="search-bar">
        <form onSubmit={handleSearch}>
          <input 
            type="text" 
            placeholder="Search for farm fresh products..." 
            value={searchTerm}
            onChange={(e) => onSearch(e.target.value)} // Update the search term dynamically
          />
        </form>
      </div>

      <div className="navbar-right">
        <Link to="/cart" className="cart-icon">
          <FaShoppingCart />
          {cartCount > 0 && <span className="cart-count">{cartCount}</span>}
        </Link>

        <div className="profile-dropdown" ref={profileMenuRef}>
          <button 
            className="profile-icon" 
            onClick={() => setShowProfileMenu(!showProfileMenu)}
          >
            <FaUser />
          </button>

          {showProfileMenu && (
            <div className="profile-menu">
              <button onClick={() => {
                setShowProfileModal(true);
                setShowProfileMenu(false);
              }}>Profile</button>
              <Link to="/my-orders" onClick={() => setShowProfileMenu(false)}>My Orders</Link>
              <button onClick={handleLogout}>Logout</button>
            </div>
          )}
        </div>
      </div>

      {showProfileModal && <ProfileModal onClose={() => setShowProfileModal(false)} />}
    </nav>
  );
}

export default CustomerNavbar;