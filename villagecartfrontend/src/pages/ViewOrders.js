import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ordersApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import '../styles/ViewOrders.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const ViewOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [updatingStatus, setUpdatingStatus] = useState(false);

  const sellerId = localStorage.getItem('sellerId');

  const statusOptions = [
    { label: 'In Cart', value: 'Incart' },
    { label: 'Ordered', value: 'Ordered' },
    { label: 'Delivered', value: 'Delivered' },
    { label: 'Deleted', value: 'Deleted' },
    { label: 'Failed', value: 'Failed' }
  ];

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const response = await ordersApi.getSellerOrders(sellerId);
      const ordersData = response.data || [];
      setOrders(ordersData.sort((a, b) => b.orderId - a.orderId));
    } catch (err) {
      console.log('Orders fetch error:', err);
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetails = (order) => {
    setSelectedOrder(order);
    setShowModal(true);
  };

  const handleStatusUpdate = async (orderId, newStatus) => {
    try {
      setUpdatingStatus(true);
      await ordersApi.updateOrderStatus(orderId, newStatus);

      // Refresh orders after status update
      await fetchOrders();

      if (selectedOrder?.orderId === orderId) {
        setSelectedOrder(prev => ({ ...prev, orderStatus: newStatus }));
      }
    } catch (err) {
      let errorMessage = 'Failed to update order status. Try again.';

      if (err?.response?.data) {
        if (typeof err.response.data === 'string') {
          errorMessage = err.response.data;
        } else if (err.response.data.message) {
          errorMessage = err.response.data.message;
        }
      }

      if (errorMessage.includes('Quantity Exceeded')) {
        window.alert('❌ Quantity exceeds stock! Please update the quantity or restock.');
      } else {
        window.alert('⚠️ ' + errorMessage);
      }
    } finally {
      setUpdatingStatus(false);
    }
  };

  const getStatusBadgeClass = (status) => {
    switch ((status || '').toLowerCase()) {
      case 'ordered': return 'bg-info';
      case 'delivered': return 'bg-success';
      case 'failed': return 'bg-danger';
      case 'deleted': return 'bg-secondary';
      default: return 'bg-warning'; // incart
    }
  };

  useEffect(() => {
    if (!sellerId) {
      setLoading(false);
      setOrders([]);
      return;
    }

    const getOrders = async () => {
      try {
        setLoading(true);
        const response = await ordersApi.getSellerOrders(sellerId);
        const ordersData = response.data || [];
        setOrders(ordersData.sort((a, b) => b.orderId - a.orderId));
      } catch (err) {
        console.log('Orders fetch error:', err);
        setOrders([]);
      } finally {
        setLoading(false);
      }
    };

    getOrders();
  }, [sellerId]);

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="view-orders-page">
      {/* Navigation */}
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/SellerHome" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2 fs-4"></i>
            <span className="fw-bold fs-4 text-success">FarmToHome</span>
          </Link>
          <div className="d-flex gap-3">
            <Link to="/SellerHome" className="btn btn-outline-primary">
              <i className="fas fa-arrow-left me-2" />
              Back to Dashboard
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Orders Table */}
      <div className="container-fluid px-4 py-4">
        <h4 className="mb-4">All Orders</h4>
        {orders.length === 0 ? (
          <div className="alert alert-info">No orders found.</div>
        ) : (
          <div className="card">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead className="table-light">
                  <tr>
                    <th>Order ID</th>
                    <th>Product</th>
                    <th>Customer</th>
                    <th>Quantity</th>
                    <th>Status</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map(order => (
                    <tr key={order.orderId}>
                      <td>{order.orderId}</td>
                      <td>{order.productName}</td>
                      <td>{order.customerName}</td>
                      <td>
                        {order.orderQuantity}
                        {order.availableStock === 0 && (
                          <span className="badge bg-danger ms-2">Out of Stock</span>
                        )}
                      </td>
                      <td>
  <div className="dropdown">
    <button
      className={`btn btn-sm badge ${getStatusBadgeClass(order.orderStatus)} dropdown-toggle`}
      type="button"
      data-bs-toggle="dropdown"
      aria-expanded="false"
      disabled={updatingStatus || (order.orderStatus?.toLowerCase() === 'delivered')}
    >
      {statusOptions.find(s => s.value.toLowerCase() === (order.orderStatus || '').toLowerCase())?.label || 'In Cart'}
    </button>

    {/* Only show dropdown menu if not Delivered */}
    {order.orderStatus?.toLowerCase() !== 'delivered' && (
      <ul className="dropdown-menu">
        {statusOptions.map((status) => (
          <li key={status.value}>
            <button
              className="dropdown-item"
              onClick={() => handleStatusUpdate(order.orderId, status.value)}
            >
              {status.label}
            </button>
          </li>
        ))}
      </ul>
    )}
  </div>
</td>

                      <td>
                        <button
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => handleViewDetails(order)}
                        >
                          <i className="fas fa-eye" />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>

      {/* Modal */}
      {showModal && selectedOrder && (
        <>
          <div className="modal show d-block" tabIndex="-1">
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Order #{selectedOrder.orderId}</h5>
                  <button className="btn-close" onClick={() => setShowModal(false)} />
                </div>
                <div className="modal-body">
                  <p><strong>Product:</strong> {selectedOrder.productName}</p>
                  <p><strong>Customer:</strong> {selectedOrder.customerName}</p>
                  <p><strong>Quantity:</strong> {selectedOrder.orderQuantity}</p>
                  {selectedOrder.availableStock === 0 && (
                    <p><span className="badge bg-danger">Out of Stock</span></p>
                  )}
                  <p><strong>Price:</strong> ₹{selectedOrder.productPrice}</p>
                  <p><strong>Total:</strong> ₹{selectedOrder.productPrice * selectedOrder.orderQuantity}</p>
                  <p><strong>Status:</strong>
                    <span className={`badge ${getStatusBadgeClass(selectedOrder.orderStatus)} ms-2`}>
                      {selectedOrder.orderStatus}
                    </span>
                  </p>
                </div>
                <div className="modal-footer">
                  <button className="btn btn-secondary" onClick={() => setShowModal(false)}>Close</button>
                </div>
              </div>
            </div>
          </div>
          <div className="modal-backdrop fade show" />
        </>
      )}
    </div>
  );
};

export default ViewOrders;
