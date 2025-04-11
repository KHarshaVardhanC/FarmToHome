// Home.js (JSX/Logic file)
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAllProducts } from '../utils/api';
import '../styles/MainHome.css'; // Assuming you have a CSS file for styling

const Home = () => {
  const [products, setProducts] = useState([]); 
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const all = await getAllProducts();
        setProducts(all);
      } catch (err) {
        setError('Failed to load products');
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, []);

  if (loading) {
    return (
      <div className="loading-screen">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return <div className="alert alert-danger text-center m-4">{error}</div>;
  }

  return (
    <div className="home-page">
      {/* Top Nav */}
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2 logo-icon"></i>
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

      {/* All Products */}
      <div className="container py-5">
        <h2 className="mb-4 fw-bold">All Products</h2>
        <div className="row g-4">
          {products.map((product, index) => (
            <div key={index} className="col-sm-6 col-md-4 col-lg-3">
              <div className="card h-100 shadow-sm">
                <img
                  src={product.imageUrl}
                  className="card-img-top product-img"
                  alt={product.productName}
                />
                <div className="card-body">
                  <h5 className="card-title">{product.productName}</h5>
                  <p className="card-text fw-bold">₹{product.productPrice}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Footer */}
      <footer className="footer text-white text-center py-3 mt-5">
        © 2025 Village Cart. All rights reserved.
      </footer>
    </div>
  );
};

export default Home;
