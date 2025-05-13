import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import ProfileDropdown from '../components/ProfileDropdown';
import '../styles/store.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const Store = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selectedOrder, setSelectedOrder] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [showReportModal, setShowReportModal] = useState(false);
    const [selectedReportOrder, setSelectedReportOrder] = useState(null);

    // API base URL from environment variables
    const API_BASE_URL = process.env.REACT_APP_BACKEND_URL;

    // Status options for orders delivered to store
    const statusOptions = [
        { label: 'Delivered', value: 'Delivered' }
    ];

    const fetchOrders = async () => {
        try {
            setLoading(true);
            // Fetch all orders that need to be processed by the store
            const response = await axios.get(`${API_BASE_URL}/order`);
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

    const updateOrderStatus = async (orderId, newStatus) => {
        const currentOrder = orders.find(o => o.orderId === orderId);

        // Existing validation checks
        if (currentOrder &&
            ['refunded', 'exchanged'].includes(currentOrder.orderStatus?.toLowerCase())) {
            window.alert(`❌ Order is already ${currentOrder.orderStatus}. Status cannot be changed.`);
            return false;
        }

        if (currentOrder &&
            currentOrder.orderStatus?.toLowerCase() !== 'deliveredtostore') {
            window.alert('❌ Status can only be changed after order is delivered to store.');
            return false;
        }

        try {
            // Update the API endpoint to match the backend specification
            const response = await axios.put(
                `${API_BASE_URL}/order/order/${orderId}/${newStatus}`,
                {},  // Empty body since status is in URL
                {
                    headers: { 'Content-Type': 'application/json' }
                }
            );

            if (response.status === 200) {
                await fetchOrders();

                // Update UI states
                if (selectedOrder?.orderId === orderId) {
                    setSelectedOrder(prev => ({ ...prev, orderStatus: newStatus }));
                }
                if (selectedReportOrder?.orderId === orderId) {
                    setSelectedReportOrder(prev => ({ ...prev, orderStatus: newStatus }));
                }
                return true;
            }
        } catch (err) {
            console.error('Error updating order status:', err);
            window.alert('⚠️ Failed to update order status. Please try again.');
            return false;
        }
    };

    const getStatusBadgeClass = (status) => {
        switch ((status || '').toLowerCase()) {
            case 'ordered': return 'bg-info';
            case 'deliveredtostore': return 'bg-primary';
            case 'delivered': return 'bg-success';
            case 'refunded': return 'bg-warning';
            case 'exchanged': return 'bg-secondary';
            case 'failed': return 'bg-danger';
            default: return 'bg-secondary';
        }
    };

    const hasReport = (order) => {
        return order.reportReason && order.reportReason.trim() !== '';
    };

    useEffect(() => {
        fetchOrders();
    }, []);

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
        <div className="store-page">
            {/* Navigation */}
            <nav className="navbar navbar-light bg-white shadow-sm">
                <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
                    <Link to="/StoreHome" className="text-decoration-none d-flex align-items-center">
                        <i className="fas fa-store text-primary me-2 fs-4"></i>
                        <span className="fw-bold fs-4 text-primary">Village Cart Store</span>
                    </Link>
                    <div className="d-flex gap-3">
                        <Link to="/login" className="btn btn-outline-primary">
                            <i className="fas fa-arrow-left me-2" />
                            Back
                        </Link>
                        <ProfileDropdown />
                    </div>
                </div>
            </nav>

            {/* Orders Table */}
            <div className="container-fluid px-4 py-4">
                <h4 className="mb-4">Store Orders</h4>
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
                                        <th>Seller</th>
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
                                            <td>{order.sellerName}</td>
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
                                                        data-bs-auto-close="true"
                                                        aria-expanded="false"
                                                        disabled={
                                                            ['refunded', 'exchanged', 'delivered'].includes(order.orderStatus?.toLowerCase()) ||
                                                            order.orderStatus?.toLowerCase() !== 'deliveredtostore'
                                                        }
                                                    >
                                                        {order.orderStatus || 'Ordered'}
                                                    </button>
                                                    {order.orderStatus?.toLowerCase() === 'deliveredtostore' && (
                                                        <ul className="dropdown-menu" data-bs-popper="static">
                                                            {statusOptions.map((status) => (
                                                                <li key={status.value}>
                                                                    <button
                                                                        className="dropdown-item"
                                                                        onClick={() => updateOrderStatus(order.orderId, status.value)}
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
                                    <p><strong>Seller:</strong> {selectedOrder.sellerName || 'N/A'}</p>
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
                                        <p><strong>Seller:</strong> {selectedReportOrder.sellerName || 'N/A'}</p>
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

                                    {hasReport(selectedReportOrder) && !['refunded', 'exchanged'].includes(selectedReportOrder.orderStatus?.toLowerCase()) && (
                                        <div className="alert alert-info mt-4">
                                            <p className="mb-0">This order has a reported issue. Only the seller can take refund or exchange actions.</p>
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
                                        <button className="btn btn-secondary" onClick={() => setShowReportModal(false)}>Close</button>
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

export default Store;