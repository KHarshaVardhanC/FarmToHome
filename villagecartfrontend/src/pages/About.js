// About.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/About.css'; // Assuming you have a CSS file for styling
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';

const About = () => {
  return (
    <div className="about-page">
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2" style={{ fontSize: '24px' }}></i>
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

      <div className="container py-5">
        <h2 className="fw-bold mb-4">About <span className="text-success">Village Cart</span></h2>
        <p className="lead">
          <strong>Village Cart</strong> is an innovative online marketplace dedicated to bringing the rich heritage,
          craftsmanship, and natural produce of rural India straight to your doorstep.
        </p>

        <hr className="my-4" />

        <h3 className="section-title">🌟 Our Mission</h3>
        <p>
          We connect artisans, farmers, and small-scale producers with fair opportunities, preserving traditions,
          promoting organics, and valuing rural hard work.
        </p>

        <h3 className="section-title">🎯 What We Offer</h3>
        <ul className="list-group list-group-flush mb-4">
          <li className="list-group-item">✅ Fresh and organic fruits, vegetables, and groceries</li>
          <li className="list-group-item">🧵 Handcrafted textiles, pottery, bamboo crafts, and more</li>
          <li className="list-group-item">🌱 Naturally grown grains, pulses, and spices from Indian villages</li>
          <li className="list-group-item">🎁 Custom gift hampers with local flavor and sustainable packaging</li>
        </ul>

        <h3 className="section-title">💡 Why Choose Village Cart?</h3>
        <ul className="list-unstyled">
          <li><strong>Fair Pricing:</strong> More earnings for farmers and artisans.</li>
          <li><strong>Authenticity:</strong> Locally sourced, quality-checked products.</li>
          <li><strong>Fast Delivery:</strong> Reliable and timely delivery to your doorstep.</li>
          <li><strong>Sustainability:</strong> Eco-conscious products and practices.</li>
          <li><strong>Community Impact:</strong> Your purchase strengthens rural livelihoods.</li>
        </ul>

        <h3 className="section-title">🔧 How It Works</h3>
        <p>
          Partnering with cooperatives and NGOs, we collect, curate, and ship products ensuring quality and transparency
          for both vendors and customers.
        </p>

        <h3 className="section-title">🌍 Our Vision</h3>
        <p>
          A future where rural India is digitally connected, economically strong, and globally recognized.
        </p>

        <h3 className="section-title">🙌 Join Us</h3>
        <p>
          Your support helps dreams grow, empowers farmers and artisans, and builds a better future.
        </p>

        <p className="fw-bold text-success">Thank you for supporting local. Thank you for choosing Village Cart.</p>
      </div>

      <footer className="bg-success text-white text-center py-3 mt-5">
        © 2025 Village Cart. All rights reserved.
      </footer>
    </div>
  );
};

export default About;
