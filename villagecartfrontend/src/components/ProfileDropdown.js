import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';

const ProfileDropdown = () => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

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

  return (
    <div className="dropdown" ref={dropdownRef}>
      <div 
        className="profile-icon"
        onClick={toggleDropdown}
        role="button"
        aria-expanded={isOpen}
      >
        <i className="fas fa-user-circle"></i>
      </div>

      <div className={`dropdown-menu ${isOpen ? 'show' : ''}`}>
        <Link to="/profile" className="dropdown-item">
          <i className="fas fa-user"></i>
          Profile
        </Link>
        <div className="dropdown-divider"></div>
        <Link to="/logout" className="dropdown-item text-danger">
          <i className="fas fa-sign-out-alt"></i>
          Logout
        </Link>
      </div>
    </div>
  );
};

export default ProfileDropdown; 