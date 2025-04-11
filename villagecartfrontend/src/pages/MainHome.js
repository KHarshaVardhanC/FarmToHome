import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Slider from 'react-slick';
import { getAllProducts, getCategoryProducts } from '../utils/api';
import '../styles/MainHome.css';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import fruitsImg from '../images/fruit.jpg';
import vegetablesImg from '../images/vegetables.jpg';
import dairyImg from '../images/dairy.jpg';
import seedsImg from '../images/grains.jpg';

const Home = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [categoryProducts, setCategoryProducts] = useState([]);
  const [categoryLoading, setCategoryLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredProducts, setFilteredProducts] = useState([]);

  const categories = [
    { id: 1, name: 'Fruits', image: fruitsImg, slug: 'fruits' },
    { id: 2, name: 'Vegetables', image: vegetablesImg, slug: 'vegetables' },
    { id: 3, name: 'Dairy', image: dairyImg, slug: 'dairy' },
    { id: 4, name: 'Seeds', image: seedsImg, slug: 'seeds' }
  ];

  const handleCategorySelect = async (categorySlug) => {
    setCategoryLoading(true);
    setSelectedCategory(categorySlug);

    try {
      const products = await getCategoryProducts(categorySlug);
      setCategoryProducts(products);
    } catch (err) {
      console.error('Error fetching category products:', err);
      setCategoryProducts([]);
    } finally {
      setCategoryLoading(false);
    }
  };

  const carouselSettings = {
    dots: true,
    infinite: true,
    speed: 1000,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 1500,
    responsive: [
      {
        breakpoint: 992,
        settings: {
          slidesToShow: 3,
        },
      },
      {
        breakpoint: 768,
        settings: {
          slidesToShow: 2,
        },
      },
      {
        breakpoint: 576,
        settings: {
          slidesToShow: 1,
        },
      },
    ],
  };

  const handleSearch = (term) => {
    setSearchTerm(term);
    if (!term.trim()) {
      setFilteredProducts(products);
      return;
    }

    const filtered = products.filter((product) =>
      product.productName.toLowerCase().includes(term.toLowerCase())
    );
    setFilteredProducts(filtered);
  };

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const all = await getAllProducts();
        setProducts(all);
        setFilteredProducts(all);
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
      {/* Navbar */}
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2 logo-icon"></i>
            <span className="fw-bold fs-4 text-success">Village Cart</span>
          </Link>
          <div className="search-container">
            <input
              type="text"
              placeholder="Search products..."
              value={searchTerm}
              onChange={(e) => handleSearch(e.target.value)}
              className="form-control"
            />
          </div>
          <div className="d-flex gap-4">
            <Link className="nav-link" to="/">Home</Link>
            <Link className="nav-link" to="/login">Login</Link> {/* Redirects to SignIn page */}
            <Link className="nav-link" to="/register">Register</Link>
            <Link className="nav-link" to="/about">About Us</Link>
            <Link className="nav-link" to="/contact">Contact Us</Link>
          </div>
        </div>
      </nav>

      {/* Category Carousel */}
      <div className="category-carousel-container">
        <div className="container">
          <h3 className="section-title">Shop by Category</h3>
          <Slider {...carouselSettings}>
            {categories.map(category => (
              <div
                key={category.id}
                className="category-card"
                onClick={() => handleCategorySelect(category.slug)}
              >
                <img src={category.image} alt={category.name} className="category-image" />
                <h5 className="category-name">{category.name}</h5>
              </div>
            ))}
          </Slider>
        </div>
      </div>

      {/* Category Products */}
      {selectedCategory && (
        <div className="container py-4">
          <h2 className="mb-4 text-capitalize fw-bold">
            {selectedCategory} Products
          </h2>

          {categoryLoading ? (
            <div className="text-center py-5">
              <div className="spinner-border text-success" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
            </div>
          ) : categoryProducts.length > 0 ? (
            <div className="row g-4">
              {categoryProducts.map((product, index) => (
                <div key={index} className="col-sm-6 col-md-4 col-lg-3">
                  <div className="card h-100 shadow-sm product-card">
                    <img
                      src={product.imageUrl}
                      className="card-img-top product-img"
                      alt={product.productName}
                    />
                    <div className="card-body">
                      <h5 className="card-title">{product.productName}</h5>

                      <p className="card-text fw-bold">₹{product.productPrice}/kg</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="alert alert-info">
              No products found in this category.
            </div>
          )}
        </div>
      )}

      {/* All Products */}
      <div className="container py-5">
        <h2 className="mb-4 fw-bold">All Products</h2>
        <div className="row g-4">
          {filteredProducts.map((product, index) => (
            <div key={index} className="col-sm-6 col-md-4 col-lg-3">
              <div className="card h-100 shadow-sm product-card">
                <img
                  src={product.imageUrl}
                  className="card-img-top product-img"
                  alt={product.productName}
                />
                <div className="card-body">
                  <h5 className="card-title">{product.productName}</h5>
                  <p className="card-text">{product.productDescription}</p>
                  <p className="card-text fw-bold">₹{product.productPrice}/kg</p>
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