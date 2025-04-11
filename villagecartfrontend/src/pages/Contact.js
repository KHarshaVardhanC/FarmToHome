import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Contact.css'; // Import external CSS

const Contact = () => {
  return (
    <div className="contact-page">
      {/* Navbar */}
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf logo-icon me-2"></i>
            <span className="fw-bold fs-4 text-success">Village Cart</span>
          </Link>
          <div className="d-flex gap-4">
            <Link className="nav-link" to="/">Home</Link>
            <Link className="nav-link" to="/login">Login</Link>
            <Link className="nav-link" to="/register">Register</Link>
            <Link className="nav-link" to="/about">About Us</Link>
            <Link className="nav-link" to="/contact">Contact Us</Link>
          </div>
        </div>
      </nav>

      {/* Contact Content */}
      <section className="container py-5">
        <h2 className="mb-4 fw-bold text-dark">📞 Contact Us</h2>
        <div className="row g-4">
          <div className="col-md-6">
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <h5 className="card-title text-success mb-3">We’re here to help!</h5>
                <p className="card-text">
                  <i className="fas fa-envelope me-2 text-secondary"></i>
                  <strong>Email:</strong> support@villagecart.com
                </p>
                <p className="card-text">
                  <i className="fas fa-phone-alt me-2 text-secondary"></i>
                  <strong>Phone:</strong> +91-9876543210
                </p>
                <p className="card-text">
                  <i className="fas fa-map-marker-alt me-2 text-secondary"></i>
                  <strong>Address:</strong> 123 Village Lane, Organic Town, India
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        © 2025 Village Cart. All rights reserved.
      </footer>
    </div>
  );
};

export default Contact;
