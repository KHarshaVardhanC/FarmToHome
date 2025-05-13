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
  const [showReportModal, setShowReportModal] = useState(false);
  const [selectedReportOrder, setSelectedReportOrder] = useState(null);
 const [openDropdownId, setOpenDropdownId] = useState(null);

  const sellerId = localStorage.getItem('sellerId');

  const statusOptions = [
    { label: 'Delivered to Store', value: 'DeliveredToStore' }
  ];

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const response = await ordersApi.getSellerOrders(sellerId);
      const ordersData = response.data || [];
      setOrders(ordersData);
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

  const handleViewReport = (order) => {
    setSelectedReportOrder(order);
    setShowReportModal(true);
  };

  const handleStatusUpdate = async (orderId, newStatus) => {
    const currentOrder = orders.find(o => o.orderId === orderId);

    if (currentOrder &&
      ['refunded', 'exchanged'].includes(currentOrder.orderStatus?.toLowerCase())) {
      window.alert(`❌ Order is already ${currentOrder.orderStatus}. Status cannot be changed.`);
      return;
    }

    try {
      setUpdatingStatus(true);
      await ordersApi.updateOrderStatus(orderId, newStatus);
      await fetchOrders();

      if (selectedOrder?.orderId === orderId) {
        setSelectedOrder(prev => ({ ...prev, orderStatus: newStatus }));
      }

      if (selectedReportOrder?.orderId === orderId) {
        setSelectedReportOrder(prev => ({ ...prev, orderStatus: newStatus }));
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

      window.alert('⚠️ ' + errorMessage);
    } finally {
      setUpdatingStatus(false);
    }
  };

  const handleRefund = (orderId) => {
    handleStatusUpdate(orderId, 'Refunded');
  };

  const handleExchange = (orderId) => {
    handleStatusUpdate(orderId, 'Exchanged');
  };

  const getStatusBadgeClass = (status) => {
    switch ((status || '').toLowerCase()) {
      case 'ordered': return 'bg-info';
      case 'deliveredtostore': return 'bg-success';
      case 'refunded': return 'bg-warning';
      case 'exchanged': return 'bg-primary';
      case 'failed': return 'bg-danger';
      default: return 'bg-secondary';
    }
  };

  const hasReport = (order) => {
    return order.reportReason && order.reportReason.trim() !== '';
  };

  useEffect(() => {
    if (!sellerId) {
      setLoading(false);
      setOrders([]);
      return;
    }
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

  return (
    <div className="view-orders-page">
      {/* Navigation */}
      <nav className="navbar navbar-light bg-white shadow-sm">
        <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
          <Link to="/SellerHome" className="text-decoration-none d-flex align-items-center">
            <i className="fas fa-leaf text-success me-2 fs-4"></i>
            <span className="fw-bold fs-4 text-success">Village Cart</span>
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
          <div className="card position-relative">
            <div className="table-responsive">
              <table className="table table-hover">
                <thead className="table-light">
                  <tr>
                    <th>Order ID</th>
                    <th>Product</th>
                    <th>Customer</th>
                    <th>Quantity</th>
                    <th>Status</th>
                    <th>Reports</th>
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
<td style={{ position: 'relative' }}>
  <button
    className={`btn btn-sm badge ${getStatusBadgeClass(order.orderStatus)}`}
    onClick={() =>
      setOpenDropdownId(openDropdownId === order.orderId ? null : order.orderId)
    }
    disabled={
      updatingStatus ||
      ['refunded', 'exchanged', 'deliveredtostore'].includes(order.orderStatus?.toLowerCase())
    }
  >
    {order.orderStatus || 'Ordered'}
  </button>

  {openDropdownId === order.orderId &&
    order.orderStatus?.toLowerCase() === 'ordered' && (
      <ul
        className="dropdown-menu show"
        style={{ position: 'absolute', top: '100%', zIndex: 999 }}
      >
        {statusOptions.map((status) => (
          <li key={status.value}>
            <button
              className="dropdown-item"
              onClick={() => {
                handleStatusUpdate(order.orderId, status.value);
                setOpenDropdownId(null);
              }}
            >
              {status.label}
            </button>
          </li>
        ))}
      </ul>
    )}
</td>


                      <td>
                        {hasReport(order) ? (
                          <button
                            className="btn btn-sm btn-warning"
                            onClick={() => handleViewReport(order)}
                          >
                            <i className="fas fa-exclamation-triangle" /> View Report
                          </button>
                        ) : (
                          <span className="text-muted">No reports</span>
                        )}
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

      {/* Order Details Modal */}
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
                  {hasReport(selectedOrder) && (
                    <div className="alert alert-warning mt-3">
                      <p className="mb-1"><strong>Report Issue:</strong> {selectedOrder.reportReason}</p>
                      <button
                        className="btn btn-sm btn-warning mt-2"
                        onClick={() => {
                          setShowModal(false);
                          handleViewReport(selectedOrder);
                        }}
                      >
                        <i className="fas fa-exclamation-triangle me-1"></i> View Full Report
                      </button>
                    </div>
                  )}
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

      {/* Order Report Modal */}
      {showReportModal && selectedReportOrder && (
        <>
          <div className="modal show d-block" tabIndex="-1">
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header bg-warning text-dark">
                  <h5 className="modal-title">
                    <i className="fas fa-exclamation-triangle me-2"></i>
                    Report for Order #{selectedReportOrder.orderId}
                  </h5>
                  <button className="btn-close" onClick={() => setShowReportModal(false)} />
                </div>
                <div className="modal-body">
                  <div className="alert alert-light border">
                    <h6>Order Details</h6>
                    <p><strong>Product:</strong> {selectedReportOrder.productName}</p>
                    <p><strong>Customer:</strong> {selectedReportOrder.customerName}</p>
                    <p><strong>Order Status:</strong>
                      <span className={`badge ${getStatusBadgeClass(selectedReportOrder.orderStatus)} ms-2`}>
                        {selectedReportOrder.orderStatus}
                      </span>
                    </p>
                  </div>

                  <div className="mt-4">
                    <h6>Issue Reported</h6>
                    <div className="p-3 border rounded bg-light">
                      <p className="mb-3">{selectedReportOrder.reportReason}</p>
                    </div>
                  </div>

                  {selectedReportOrder.orderReportImageUrl && (
                    <div className="mt-4">
                      <h6>Report Image</h6>
                      <div className="text-center">
                        <img
                          src={selectedReportOrder.orderReportImageUrl}
                          alt="Report Image"
                          className="img-fluid border rounded"
                          style={{ maxHeight: '200px' }}
                        />
                      </div>
                    </div>
                  )}

                  {!['refunded', 'exchanged'].includes(selectedReportOrder.orderStatus?.toLowerCase()) && (
                    <div className="alert alert-info mt-4">
                      <p className="mb-0">Please select an action to resolve this customer issue.</p>
                    </div>
                  )}
                </div>
                <div className="modal-footer">
                  {['refunded', 'exchanged'].includes(selectedReportOrder.orderStatus?.toLowerCase()) ? (
                    <div className="w-100 text-center">
                      <div className={`alert alert-${selectedReportOrder.orderStatus?.toLowerCase() === 'refunded' ? 'warning' : 'primary'} mb-0`}>
                        This order has been {selectedReportOrder.orderStatus?.toLowerCase()}
                      </div>
                    </div>
                  ) : (
                    <>
                      <button className="btn btn-secondary" onClick={() => setShowReportModal(false)}>Close</button>
                      <button
                        className="btn btn-warning"
                        onClick={() => handleRefund(selectedReportOrder.orderId)}
                        disabled={updatingStatus}
                      >
                        {updatingStatus ? 'Processing...' : 'Issue Refund'}
                      </button>
                      <button
                        className="btn btn-primary"
                        onClick={() => handleExchange(selectedReportOrder.orderId)}
                        disabled={updatingStatus}
                      >
                        {updatingStatus ? 'Processing...' : 'Offer Exchange'}
                      </button>
                    </>
                  )}
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