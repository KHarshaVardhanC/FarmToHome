import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ordersApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const ViewOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [updatingStatus, setUpdatingStatus] = useState(false);
  
  // Replace with actual seller ID from authentication
  const sellerId = 2;

  const handleViewDetails = (order) => {
    setSelectedOrder(order);
    setShowModal(true);
  };

  const handleStatusUpdate = async (orderId, newStatus) => {
    try {
      setUpdatingStatus(true);
      await ordersApi.updateOrderStatus(orderId, newStatus);
      
      // Update the orders list with the new status
      setOrders(orders.map(order => 
        order.orderId === orderId 
          ? { ...order, orderStatus: newStatus }
          : order
      ));

      // Update selected order if in modal
      if (selectedOrder && selectedOrder.orderId === orderId) {
        setSelectedOrder({ ...selectedOrder, orderStatus: newStatus });
      }
    } catch (err) {
      console.error('Error updating order status:', err);
      setError('Failed to update order status. Please try again.');
    } finally {
      setUpdatingStatus(false);
    }
  };

  const getStatusBadgeClass = (status) => {
    switch (status?.toLowerCase()) {
      case 'in cart':
        return 'bg-warning';
      case 'ordered':
        return 'bg-info';
      case 'success':
        return 'bg-success';
      case 'failed':
        return 'bg-danger';
      case 'deleted':
        return 'bg-secondary';
      default:
        return 'bg-warning';
    }
  };

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        const response = await ordersApi.getSellerOrders(sellerId);
        // Sort orders by orderId in descending order (most recent first)
        const sortedOrders = response.data.sort((a, b) => b.orderId - a.orderId);
        setOrders(sortedOrders);
      } catch (err) {
        console.error('Error fetching orders:', err);
        setError('Failed to load orders. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
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

  if (error) {
    return (
      <div className="alert alert-danger m-4" role="alert">
        {error}
      </div>
    );
  }

  return (
    <div className="view-orders-page">
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
              <i className="fas fa-arrow-left me-2"></i>Back to Dashboard
            </Link>
            <ProfileDropdown />
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container-fluid px-4 py-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h4 className="mb-0">All Orders</h4>
        </div>

        {orders.length === 0 ? (
          <div className="alert alert-info">No orders found.</div>
        ) : (
          <div className="card">
            <div className="table-responsive">
              <table className="table table-hover mb-0">
                <thead>
                  <tr>
                    <th>Order ID</th>
                    <th>Product Name</th>
                    <th>Customer Name</th>
                    <th>Quantity</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map((order) => (
                    <tr key={order.orderId}>
                      <td>{order.orderId}</td>
                      <td>{order.productName}</td>
                      <td>{order.customerName}</td>
                      <td>{order.orderQuantity}</td>
                      <td>
                        <div className="dropdown">
                          <button 
                            className={`btn btn-sm badge ${getStatusBadgeClass(order.orderStatus)} dropdown-toggle`}
                            type="button"
                            id={`statusDropdown${order.orderId}`}
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                            disabled={updatingStatus}
                          >
                            {order.orderStatus || 'In Cart'}
                          </button>
                          <ul className="dropdown-menu" aria-labelledby={`statusDropdown${order.orderId}`}>
                            <li><button className="dropdown-item" onClick={() => handleStatusUpdate(order.orderId, 'In Cart')}>In Cart</button></li>
                            <li><button className="dropdown-item" onClick={() => handleStatusUpdate(order.orderId, 'Ordered')}>Ordered</button></li>
                            <li><button className="dropdown-item" onClick={() => handleStatusUpdate(order.orderId, 'Success')}>Success</button></li>
                            <li><button className="dropdown-item" onClick={() => handleStatusUpdate(order.orderId, 'Failed')}>Failed</button></li>
                          </ul>
                        </div>
                      </td>
                      <td>
                        <button
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => handleViewDetails(order)}
                        >
                          <i className="fas fa-eye"></i>
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

      {/* Order Details Modal */}
      {showModal && selectedOrder && (
        <div className="modal show d-block" tabIndex="-1">
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Order Details</h5>
                <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
              </div>
              <div className="modal-body">
                <div className="mb-3">
                  <strong>Order ID:</strong> {selectedOrder.orderId}
                </div>
                <div className="mb-3">
                  <strong>Product:</strong> {selectedOrder.productName}
                </div>
                <div className="mb-3">
                  <strong>Customer:</strong> {selectedOrder.customerName}
                </div>
                <div className="mb-3">
                  <strong>Quantity:</strong> {selectedOrder.orderQuantity}
                </div>
                <div className="mb-3">
                  <strong>Price:</strong> ₹{selectedOrder.productPrice}
                </div>
                <div className="mb-3">
                  <strong>Total:</strong> ₹{selectedOrder.productPrice * selectedOrder.orderQuantity}
                </div>
                <div className="mb-3">
                  <strong>Status:</strong> 
                  <div className="dropdown d-inline-block ms-2">
                    <button 
                      className={`btn btn-sm badge ${getStatusBadgeClass(selectedOrder.orderStatus)} dropdown-toggle`}
                      type="button"
                      id="modalStatusDropdown"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                      disabled={updatingStatus}
                    >
                      {selectedOrder.orderStatus || 'In Cart'}
                    </button>
                    <ul className="dropdown-menu" aria-labelledby="modalStatusDropdown">
                      <li><button className="dropdown-item" onClick={() => handleStatusUpdate(selectedOrder.orderId, 'In Cart')}>In Cart</button></li>
                      <li><button className="dropdown-item" onClick={() => handleStatusUpdate(selectedOrder.orderId, 'Ordered')}>Ordered</button></li>
                      <li><button className="dropdown-item" onClick={() => handleStatusUpdate(selectedOrder.orderId, 'Success')}>Success</button></li>
                      <li><button className="dropdown-item" onClick={() => handleStatusUpdate(selectedOrder.orderId, 'Failed')}>Failed</button></li>
                    </ul>
                  </div>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>
                  Close
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Modal Backdrop */}
      {showModal && (
        <div className="modal-backdrop show"></div>
      )}
    </div>
  );
};

export default ViewOrders; 