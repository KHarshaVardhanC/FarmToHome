import React from 'react';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const Navbar = () => {
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container">
        <Link className="navbar-brand" to="/dashboard">
          <i className="fas fa-store me-2"></i>
          FarmToHome Seller
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            <li className="nav-item">
              <Link className="nav-link" to="/dashboard">
                <i className="fas fa-chart-line me-1"></i>
                Dashboard
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/products">
                <i className="fas fa-box me-1"></i>
                Products
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/orders">
                <i className="fas fa-shopping-cart me-1"></i>
                Orders
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/profile">
                <i className="fas fa-user me-1"></i>
                Profile
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/logout">
                <i className="fas fa-sign-out-alt me-1"></i>
                Logout
              </Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar; 