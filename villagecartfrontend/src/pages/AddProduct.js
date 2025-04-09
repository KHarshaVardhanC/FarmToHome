import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { productApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const AddProduct = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState(null);
  
  // Replace with actual seller ID from authentication
  const sellerId = 2; // This should come from your auth context

  const [productData, setProductData] = useState({
    productName: '',
    productPrice: '',
    productQuantity: '',
    // sellerPlace: '',
    // sellerArea: '',
    sellerId: sellerId,
    productDescription: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProductData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setSelectedFile(file);
      // Create a preview URL for the selected image
      const fileReader = new FileReader();
      fileReader.onload = () => {
        setPreviewUrl(fileReader.result);
      };
      fileReader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Create FormData object
      const formData = new FormData();
      
      // Add product data
      const formattedData = {
        ...productData,
        productPrice: parseFloat(productData.productPrice),
        productQuantity: parseInt(productData.productQuantity),
        sellerId: parseInt(sellerId)
      };

      // Append all product data to FormData
      Object.keys(formattedData).forEach(key => {
        formData.append(key, formattedData[key]);
      });

      // Append the image file if selected
      if (selectedFile) {
        formData.append('image', selectedFile);
      }

      console.log('Submitting product data:', formattedData);
      const response = await productApi.addProduct(formData);
      console.log('Product added successfully:', response);
      navigate('/view-products');
    } catch (err) {
      console.error('Error adding product:', err);
      setError(err.response?.data?.message || 'Failed to add product. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-product-page">
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

          <div className="d-flex flex-grow-1 mx-4">
            <div className="input-group">
              <input
                type="text"
                className="form-control border-end-0"
                placeholder="Search products..."
              />
              <button className="btn btn-outline-secondary border-start-0">
                <i className="fas fa-search"></i>
              </button>
            </div>
          </div>

          <div className="d-flex align-items-center gap-3">
            <Link to="/view-products" className="btn btn-outline-primary px-4">
              <i className="fas fa-arrow-left me-2"></i>Back to Products
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container-fluid px-4 py-4">
        <div className="row justify-content-center">
          <div className="col-md-8">
            <div className="card">
              <div className="card-header">
                <h3 className="mb-0">Add New Product</h3>
              </div>
              <div className="card-body">
                {error && <div className="alert alert-danger">{error}</div>}
                
                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label className="form-label">Product Name</label>
                    <input
                      type="text"
                      className="form-control"
                      name="productName"
                      value={productData.productName}
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Price (₹)</label>
                    <input
                      type="number"
                      className="form-control"
                      name="productPrice"
                      value={productData.productPrice}
                      onChange={handleChange}
                      required
                      min="0"
                      step="0.01"
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Quantity</label>
                    <input
                      type="number"
                      className="form-control"
                      name="productQuantity"
                      value={productData.productQuantity}
                      onChange={handleChange}
                      required
                      min="0"
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Description</label>
                    <textarea
                      className="form-control"
                      name="productDescription"
                      value={productData.productDescription}
                      onChange={handleChange}
                      required
                      rows="3"
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Product Image</label>
                    <input
                      type="file"
                      className="form-control"
                      accept="image/*"
                      onChange={handleFileChange}
                      required
                    />
                    {previewUrl && (
                      <div className="mt-2">
                        <img
                          src={previewUrl}
                          alt="Preview"
                          className="img-thumbnail"
                          style={{ maxHeight: '200px' }}
                        />
                      </div>
                    )}
                  </div>
{/* 
                  <div className="mb-3">
                    <label className="form-label">Seller Place</label>
                    <input
                      type="text"
                      className="form-control"
                      name="sellerPlace"
                      value={productData.sellerPlace}
                      onChange={handleChange}
                      required
                    />
                  </div> */}
{/* 
                  <div className="mb-3">
                    <label className="form-label">Seller Area</label>
                    <input
                      type="text"
                      className="form-control"
                      name="sellerArea"
                      value={productData.sellerArea}
                      onChange={handleChange}
                      required
                    />
                  </div> */}

                  <div className="d-grid gap-2">
                    <button
                      type="submit"
                      className="btn btn-primary"
                      disabled={loading}
                    >
                      {loading ? (
                        <>
                          <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                          Adding Product...
                        </>
                      ) : (
                        'Add Product'
                      )}
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddProduct; 