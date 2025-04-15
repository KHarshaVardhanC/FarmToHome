import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './ProfileDropdown.css';

const ProfileDropdown = () => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleLogout = () => {
    // ✅ Clear tokens or session data here
    localStorage.removeItem('authToken');
    localStorage.removeItem('sellerId');
    sessionStorage.clear();

    // ✅ Redirect to login page
    navigate('/login');
  };

  return (
    <div className="dropdown" ref={dropdownRef}>
      <div 
        className="profile-icon"
        onClick={toggleDropdown}
        role="button"
        aria-expanded={isOpen}
        style={{ cursor: 'pointer' }}
      >
        <i className="fas fa-user-circle"></i>
      </div>

      <div className={`dropdown-menu ${isOpen ? 'show' : ''}`}>
        <Link to="/profile" className="dropdown-item">
          <i className="fas fa-user me-2"></i>
          Profile
        </Link>
        <div className="dropdown-divider"></div>
        <div 
          className="dropdown-item text-danger" 
          style={{ cursor: 'pointer' }} 
          onClick={handleLogout}
        >
          <i className="fas fa-sign-out-alt me-2"></i>
          Logout
        </div>
      </div>
    </div>
  );
};

export default ProfileDropdown;
