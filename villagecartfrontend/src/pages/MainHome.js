import React, { useEffect, useState, useRef } from 'react';
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

const MainHome = () => {
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]); // Added state for filtered products
  // const [searchTerm, setSearchTerm] = useState(''); // Added state for search term
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [categoryProducts, setCategoryProducts] = useState([]);
  const [categoryLoading, setCategoryLoading] = useState(false);

  // Reference to the category products section for scrolling
  const categoryProductsRef = useRef(null);

  // Use capitalized category names to match the database
  const categories = [
    { id: 1, name: 'Fruits', image: fruitsImg, slug: 'Fruits' },
    { id: 2, name: 'Vegetables', image: vegetablesImg, slug: 'Vegetables' },
    { id: 3, name: 'Dairy', image: dairyImg, slug: 'Dairy' },
    { id: 4, name: 'Seeds', image: seedsImg, slug: 'Seeds' }
  ];

  const handleCategorySelect = async (categorySlug) => {
    setCategoryLoading(true);
    setSelectedCategory(categorySlug);

    try {
      const products = await getCategoryProducts(categorySlug);
      setCategoryProducts(products);

      // After loading products and updating state, scroll down to show them
      setTimeout(() => {
        if (categoryProductsRef.current) {
          categoryProductsRef.current.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
          });
        }
      }, 10); // Small delay to ensure state has updated
    } catch (err) {
      console.error('Error fetching category products:', err);
      setCategoryProducts([]);
      setError(`Failed to load ${categorySlug} products`);
    } finally {
      setCategoryLoading(false);
    }
  };

  const carouselSettings = {
    dots: true,
    infinite: true,
    speed: 1000,
    slidesToShow: 1, // Show one category at a time
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    responsive: [
      {
        breakpoint: 992,
        settings: {
          slidesToShow: 1,
        },
      },
      {
        breakpoint: 768,
        settings: {
          slidesToShow: 1,
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

  // const handleSearch = (term) => {
  //   setSearchTerm(term);
  //   if (!term.trim()) {
  //     setFilteredProducts(products);
  //     return;
  //   }

  //   const filtered = products.filter((product) =>
  //     product.productName.toLowerCase().includes(term.toLowerCase())
  //   );
  //   setFilteredProducts(filtered);
  // };

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const all = await getAllProducts();
        setProducts(all);
        setFilteredProducts(all); // Initialize filtered products with all products
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
      <div className="mainhome-loading-screen">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error && !selectedCategory) {
    return <div className="alert alert-danger text-center m-4">{error}</div>;
  }

  return (
    <div className="mainhome-container">
      {/* Top Nav */}
      <nav className="navbar navbar-light bg-white shadow-sm mainhome-navbar">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2 mainhome-logo-icon"></i>
            <span className="fw-bold fs-4 text-success">Village Cart</span>
          </Link>
          <div className="d-flex gap-4">
            <Link className="nav-link" to="/">Home</Link>
            <Link className="nav-link" to="/login">Login</Link>
            <Link className="nav-link" to="/signup">Register</Link>
            <Link className="nav-link" to="/about">About Us</Link>
            <Link className="nav-link" to="/contact">Contact Us</Link>
          </div>
        </div>
      </nav>

      {/* Category Carousel using react-slick */}
      <div className="mainhome-category-carousel">
        <div className="container">
          <h3 className="mainhome-section-title">Shop by Category</h3>
          <Slider {...carouselSettings}>
            {categories.map(category => (
              <div
                key={category.id}
                className="mainhome-category-card"
                onClick={() => handleCategorySelect(category.slug)}
              >
                <img src={category.image} alt={category.name} className="mainhome-category-image" />
                <h5 className="mainhome-category-name">{category.name}</h5>
              </div>
            ))}
          </Slider>
        </div>
      </div>

      {/* Category Products - Adding ref for scrolling */}
      <div ref={categoryProductsRef}>
        {selectedCategory ? (
          <div className="container py-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h2 className="text-capitalize fw-bold mainhome-section-title">
                {selectedCategory} Products
              </h2>
              <button
                className="btn mainhome-category-filter-btn"
                onClick={() => setSelectedCategory(null)}
              >
                Show All Products
              </button>
            </div>

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
                    <div className="card h-100 shadow-sm mainhome-product-card">
                      <div className="mainhome-product-img-container">
                        <img
                          src={product.imageUrl}
                          className="mainhome-product-img"
                          alt={product.productName}
                        />
                      </div>
                      <div className="mainhome-card-body">
                        <h5 className="mainhome-card-title text-center">{product.productName}</h5>
                        <p className="mainhome-card-price text-center">₹{product.productPrice}/{product.productQuantityType || 'kg'}
                        </p>
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
        ) : (
          /* All Products */
          <div className="container py-5">
            <h2 className="mb-4 fw-bold mainhome-section-title">All Products</h2>
            <div className="row g-4">
              {filteredProducts.map((product, index) => (
                <div key={index} className="col-sm-6 col-md-4 col-lg-3">
                  <div className="card h-100 shadow-sm mainhome-product-card">
                    <div className="mainhome-product-img-container">
                      <img
                        src={product.imageUrl}
                        className="mainhome-product-img"
                        alt={product.productName}
                      />
                    </div>
                    <div className="mainhome-card-body">
                      <h5 className="mainhome-card-title text-center">{product.productName}</h5>
                      <p className="mainhome-card-price text-center">₹{product.productPrice}/{product.productQuantityType || 'kg'}
                      </p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Footer */}
      <footer className="mainhome-footer text-white text-center py-3">
        © 2025 Village Cart. All rights reserved.
      </footer>
    </div>
  );
};

export default MainHome;