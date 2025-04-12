import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { productApi } from '../utils/api';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import '../styles/EditProduct.css';

const EditProduct = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState({
    productName: '',
    productPrice: '',
    productQuantity: '',
    productDescription: '',
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const res = await productApi.getProduct(id);
        setProduct(res.data);
      } catch (err) {
        console.error('Error fetching product:', err);
        setError('Unable to load product.');
      } finally {
        setLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProduct((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await productApi.updateProduct(id, product);
      alert('Product updated successfully!');
      navigate(`/product/${id}`);
    } catch (err) {
      console.error('Update failed:', err);
      alert('Failed to update product.');
    }
  };

  if (loading) return <div className="text-center p-5">Loading...</div>;
  if (error) return <div className="alert alert-danger m-4">{error}</div>;

  return (
    <div className="edit-product-container">
      <h3 className="mb-4">Edit Product</h3>
      <form className="edit-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Product Name</label>
          <input
            type="text"
            name="productName"
            value={product.productName}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Price (₹)</label>
          <input
            type="number"
            name="productPrice"
            value={product.productPrice}
            onChange={handleChange}
            required
            min={1}
          />
        </div>
        <div className="form-group">
          <label>Quantity</label>
          <input
            type="number"
            name="productQuantity"
            value={product.productQuantity}
            onChange={handleChange}
            required
            min={0}
          />
        </div>
        <div className="form-group">
          <label>Description</label>
          <textarea
            name="productDescription"
            value={product.productDescription}
            onChange={handleChange}
            rows={3}
          ></textarea>
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-success">
            <i className="fas fa-save me-2"></i>Save Changes
          </button>
          <Link to={`/product/${id}`} className="btn btn-secondary">
            <i className="fas fa-times me-2"></i>Cancel
          </Link>
        </div>
      </form>
    </div>
  );
};

export default EditProduct;
