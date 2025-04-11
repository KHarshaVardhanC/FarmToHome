import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { productApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import '../styles/ProductDetails.css';

const ProductDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [product, setProduct] = useState(null);
  const [editedProduct, setEditedProduct] = useState({
    productName: '',
    productPrice: 0,
    productQuantity: 0,
    productDescription: '',
    sellerId: 2,
  });

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        setLoading(true);
        const response = await productApi.getProduct(id);
        const productData = response.data;
        setProduct(productData);
        setEditedProduct({
          productName: productData.productName,
          productPrice: productData.productPrice,
          productQuantity: productData.productQuantity,
          productDescription: productData.productDescription || '',
          sellerId: productData.sellerId || 2,
        });
      } catch (err) {
        console.error('Error fetching product:', err);
        setError('Failed to load product details. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedProduct(prev => ({
      ...prev,
      [name]: name === 'productPrice' || name === 'productQuantity' ? parseFloat(value) : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await productApi.updateProduct(id, editedProduct);
      setProduct({ ...product, ...editedProduct });
      setIsEditing(false);
      alert('Product updated successfully!');
    } catch (err) {
      console.error('Error updating product:', err);
      setError('Failed to update product. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productApi.deleteProduct(id);
        alert('Product deleted successfully!');
        navigate('/view-products');
      } catch (err) {
        console.error('Error deleting product:', err);
        alert('Failed to delete product. Please try again.');
      }
    }
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return <div className="alert alert-danger m-4">{error}</div>;
  }

  if (!product) {
    return <div className="alert alert-warning m-4">Product not found.</div>;
  }

  return (
    <div className="product-details-page">
      <nav className="navbar navbar-light bg-white">
        <div className="container-fluid px-4">
          <div className="d-flex align-items-center">
            <Link to="/SellerHome" className="text-decoration-none">
              <div className="logo text-dark d-flex align-items-center">
                <i className="fas fa-leaf text-success me-2" style={{ fontSize: '24px' }}></i>
                <span className="fw-bold">FarmToHome</span>
              </div>
            </Link>
          </div>
          <div className="d-flex align-items-center gap-3">
            <Link to="/view-products" className="btn btn-outline-primary">
              <i className="fas fa-arrow-left me-2"></i>Back to Products
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      <div className="container-fluid px-4 py-4">
        <h4 className="mb-4 fw-bold">Product Details</h4>
        <div className="card">
          <div className="card-body d-flex justify-content-between align-items-start flex-wrap">
            <div className="product-info">
              <p><span className="fw-bold">Name:</span> <span className="text-dark fs-5">{product.productName}</span></p>
              <p><span className="fw-bold">Description:</span> <span className="text-dark fs-5">{product.productDescription}</span></p>
              <p><span className="fw-bold">Quantity:</span> <span className="text-dark fs-5">{product.productQuantity}</span></p>
              <p><span className="fw-bold">Price:</span> <span className="text-dark fs-5">₹{product.productPrice}</span></p>
              <div className="btn-group mt-3">
                <button className="btn btn-primary" onClick={() => setIsEditing(true)}>
                  <i className="fas fa-edit me-2"></i>Edit Product
                </button>
                <button className="btn btn-danger" onClick={handleDelete}>
                  <i className="fas fa-trash-alt me-2"></i>Delete Product
                </button>
              </div>
            </div>
            {product.imageUrl && (
              <div className="product-image">
                <img src={product.imageUrl} alt={product.productName} className="img-fluid rounded shadow-sm" />
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetails;
