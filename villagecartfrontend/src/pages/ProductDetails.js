import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { productApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

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
    // sellerPlace: '',
    // sellerArea: '',
    productDescription: ''
  });
  
  const sellerId = 2;

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
          // sellerPlace: productData.sellerPlace,
          // sellerArea: productData.sellerArea,
          productDescription: productData.productDescription,
          sellerId: sellerId
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
      [name]: name === 'productPrice' || name === 'productQuantity' ? parseFloat(value) : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await productApi.updateProduct(id, editedProduct);
      setProduct(editedProduct);
      setIsEditing(false);
      alert('Product updated successfully!');
      navigate('/');
    } catch (err) {
      console.error('Error updating product:', err);
      setError('Failed to update product. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger m-4" role="alert">
        {error}
      </div>
    );
  }

  if (!product) {
    return (
      <div className="alert alert-warning m-4" role="alert">
        Product not found
      </div>
    );
  }

  return (
    <div className="product-details-page">
      {/* Top Navigation */}
      <nav className="navbar navbar-light bg-white">
        <div className="container-fluid px-4">
          <div className="d-flex align-items-center">
            <Link to="/" className="text-decoration-none">
              <div className="logo text-dark d-flex align-items-center">
                <i className="fas fa-leaf text-success me-2" style={{ fontSize: '24px' }}></i>
                <span className="fw-bold">FarmToHome</span>
              </div>
            </Link>
          </div>

          <div className="d-flex align-items-center gap-3">
            <Link to="/" className="btn btn-outline-primary">
              <i className="fas fa-arrow-left me-2"></i>Back to Products
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container-fluid px-4 py-4">
        <h4 className="mb-4">Product Details</h4>
        
        {isEditing ? (
          <div className="card">
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                <div className="row">
                  <div className="col-md-8">
                    <div className="mb-3">
                      <label className="form-label fw-bold">Product Name</label>
                      <input
                        type="text"
                        className="form-control"
                        name="productName"
                        value={editedProduct.productName}
                        onChange={handleChange}
                        required
                      />
                    </div>

                    <div className="mb-3">
                      <label className="form-label fw-bold">Description</label>
                      <textarea
                        className="form-control"
                        name="productDescription"
                        value={editedProduct.productDescription}
                        onChange={handleChange}
                        rows="3"
                      />
                    </div>

                    <div className="mb-3">
                      <label className="form-label fw-bold">Quantity</label>
                      <input
                        type="number"
                        className="form-control"
                        name="productQuantity"
                        value={editedProduct.productQuantity}
                        onChange={handleChange}
                        required
                        min="0"
                      />
                    </div>

                    <div className="mb-3">
                      <label className="form-label fw-bold">Price (₹)</label>
                      <input
                        type="number"
                        className="form-control"
                        name="productPrice"
                        value={editedProduct.productPrice}
                        onChange={handleChange}
                        required
                        min="0"
                        step="0.01"
                      />
                    </div>

                    {/* <div className="mb-3">
                      <label className="form-label fw-bold">Location</label>
                      <input
                        type="text"
                        className="form-control mb-2"
                        name="sellerPlace"
                        value={editedProduct.sellerPlace}
                        onChange={handleChange}
                        placeholder="City"
                        required
                      />
                      <input
                        type="text"
                        className="form-control"
                        name="sellerArea"
                        value={editedProduct.sellerArea}
                        onChange={handleChange}
                        placeholder="Area"
                        required
                      />
                    </div> */}

                    <div className="d-flex gap-2 mt-4">
                      <button type="submit" className="btn btn-primary">
                        Save Changes
                      </button>
                      <button
                        type="button"
                        className="btn btn-secondary"
                        onClick={() => setIsEditing(false)}
                      >
                        Cancel
                      </button>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>
        ) : (
          <div className="card">
            <div className="card-body">
              <div className="row">
                <div className="col-md-8">
                  <div className="mb-3">
                    <h5 className="mb-1">Product Name</h5>
                    <p>{product.productName}</p>
                  </div>

                  <div className="mb-3">
                    <h5 className="mb-1">Description</h5>
                    <p>{product.productDescription}</p>
                  </div>

                  <div className="mb-3">
                    <h5 className="mb-1">Quantity</h5>
                    <p>{product.productQuantity}</p>
                  </div>

                  <div className="mb-3">
                    <h5 className="mb-1">Price</h5>
                    <p>₹{product.productPrice}</p>
                  </div>

                  {/* <div className="mb-3">
                    <h5 className="mb-1">Location</h5>
                    <p>
                    {product.sellerPlace}, {product.sellerArea}
                    </p>
                  </div> */}

                  <button
                    className="btn btn-primary"
                    onClick={() => setIsEditing(true)}
                  >
                    <i className="fas fa-edit me-2"></i>
                    Edit Product
                  </button>
                </div>
                {product.imageUrl && (
                  <div className="col-md-4">
                    <img
                      src={product.imageUrl}
                      alt={product.productName}
                      className="img-fluid rounded"
                      style={{ maxHeight: '300px', objectFit: 'cover' }}
                    />
                  </div>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProductDetails; 