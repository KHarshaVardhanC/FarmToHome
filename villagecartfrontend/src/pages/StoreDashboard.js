import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { orderApi } from '../utils/api';
import ProfileDropdown from '../components/ProfileDropdown';
import '../styles/store.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const StoreDashboard = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selectedOrder, setSelectedOrder] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [updatingStatus, setUpdatingStatus] = useState(false);
    const [filter, setFilter] = useState('all');
    const [error, setError] = useState(null);

    const statusOptions = [
        { label: 'Delivered to Customer', value: 'DeliveredToCustomer' }
    ];

    const fetchOrders = async () => {
        try {
            setLoading(true);
            setError(null);

            const response = await orderApi.getStoreOrders();

            console.log('Orders API Response:', response);

            let ordersData = [];

            if (response.data) {
                if (Array.isArray(response.data)) {
                    ordersData = response.data;
                } else if (typeof response.data === 'object' && response.data !== null) {
                    try {
                        const extractProducts = () => {
                            const allProducts = [];

                            const findProducts = (obj, currentSeller = null) => {
                                if (!obj) return;

                                if (obj.products && Array.isArray(obj.products)) {
                                    const sellerName = obj.sellerFirstName && obj.sellerLastName ?
                                        `${obj.sellerFirstName} ${obj.sellerLastName}` : 'Unknown Seller';

                                    obj.products.forEach(product => {
                                        if (product && product.productId) {
                                            allProducts.push({
                                                orderId: product.productId,
                                                productName: product.productName || `Product ${product.productId}`,
                                                customerName: product.customerName || sellerName,
                                                orderQuantity: product.productQuantity || 1,
                                                productPrice: product.productPrice || 0,
                                                orderStatus: product.orderStatus || 'Ordered',
                                                paymentStatus: product.paymentStatus || 'Pending',
                                                orderDate: product.orderDate || new Date().toISOString().split('T')[0]
                                            });
                                        }
                                    });
                                }

                                if (Array.isArray(obj)) {
                                    obj.forEach(item => findProducts(item));
                                    return;
                                }

                                if (typeof obj === 'object' && obj !== null) {
                                    Object.keys(obj).forEach(key => {
                                        const isSeller = key === 'seller' ||
                                            (obj.sellerId && obj.sellerEmail && (obj.sellerFirstName || obj.sellerLastName));

                                        if (isSeller) {
                                            findProducts(obj[key], obj);
                                        } else {
                                            findProducts(obj[key], currentSeller);
                                        }
                                    });
                                }
                            };

                            findProducts(response.data);
                            return allProducts;
                        };

                        ordersData = extractProducts();
                        console.log('Extracted Orders:', ordersData);
                    } catch (parseError) {
                        console.error('Error while parsing orders:', parseError);
                        ordersData = [];
                    }
                }
            }

            setOrders(ordersData);

            if (ordersData.length === 0) {
                setError('No orders found. Either there are no orders in the system or the data structure has changed.');
            }
        } catch (err) {
            console.error('Orders Fetch Error:', {
                message: err.message,
                response: err.response?.data,
                status: err.response?.status
            });
            setError('Failed to load orders. Please try again.');
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
        const currentOrder = orders.find(o => o.orderId === orderId);

        // Check if order status is already in a final state
        if (currentOrder &&
            ['refunded', 'exchanged', 'deliveredtocustomer'].includes(currentOrder.orderStatus?.toLowerCase())) {
            window.alert(`❌ Order is already ${currentOrder.orderStatus}. Status cannot be changed.`);
            return;
        }

        try {
            setUpdatingStatus(true);
            await orderApi.updateOrderStatus(orderId, newStatus);
            await fetchOrders();

            if (selectedOrder?.orderId === orderId) {
                setSelectedOrder(prev => ({ ...prev, orderStatus: newStatus }));
            }

            window.alert(`✅ Order status updated to ${newStatus}`);
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

    const getStatusBadgeClass = (status) => {
        switch ((status || '').toLowerCase()) {
            case 'ordered': return 'bg-info';
            case 'deliveredtostore': return 'bg-warning';
            case 'deliveredtocustomer': return 'bg-success';
            case 'refunded': return 'bg-danger';
            case 'exchanged': return 'bg-primary';
            default: return 'bg-secondary';
        }
    };

    const getPaymentStatusBadgeClass = (status) => {
        switch ((status || '').toLowerCase()) {
            case 'paid': return 'bg-success';
            case 'pending': return 'bg-warning';
            case 'failed': return 'bg-danger';
            case 'refunded': return 'bg-info';
            default: return 'bg-secondary';
        }
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
        <div className="store-dashboard-page">
            <nav className="navbar navbar-light bg-white shadow-sm">
                <div className="container-fluid px-4 py-2 d-flex justify-content-between align-items-center">
                    <Link to="/StoreHome" className="text-decoration-none d-flex align-items-center">
                        <i className="fas fa-store text-success me-2 fs-4"></i>
                        <span className="fw-bold fs-4 text-success">Store Orders Dashboard</span>
                    </Link>
                    <div className="d-flex gap-3">
                        <Link to="/StoreHome" className="btn btn-outline-primary">
                            <i className="fas fa-arrow-left me-2" />
                            Back to Dashboard
                        </Link>
                        <ProfileDropdown />
                    </div>
                </div>
            </nav>

            <div className="container-fluid px-4 py-4">
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h4>All Orders</h4>
                    <div className="d-flex align-items-center">
                        <label className="me-2">Filter by Status:</label>
                        <select
                            className="form-select form-select-sm"
                            style={{ width: 'auto' }}
                            value={filter}
                            onChange={(e) => setFilter(e.target.value)}
                        >
                            <option value="all">All Orders</option>
                            <option value="ordered">Ordered</option>
                            <option value="deliveredtostore">Delivered to Store</option>
                            <option value="deliveredtocustomer">Delivered to Customer</option>
                            <option value="refunded">Refunded</option>
                            <option value="exchanged">Exchanged</option>
                        </select>
                        <button
                            className="btn btn-sm btn-outline-secondary ms-2"
                            onClick={fetchOrders}
                        >
                            <i className="fas fa-sync-alt"></i>
                        </button>
                    </div>
                </div>

                {error ? (
                    <div className="alert alert-danger">
                        <i className="fas fa-exclamation-triangle me-2"></i>
                        {error}
                    </div>
                ) : orders.length === 0 ? (
                    <div className="alert alert-info">
                        <i className="fas fa-info-circle me-2"></i>
                        No orders found in the system.
                    </div>
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
                                        <th>Order Status</th>
                                        <th>Payment Status</th>
                                        <th>Date</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {orders
                                        .filter(order => {
                                            if (filter === 'all') return true;
                                            return order.orderStatus?.toLowerCase() === filter.toLowerCase();
                                        })
                                        .map(order => (
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
                                                            data-bs-toggle="dropdown"
                                                            data-bs-auto-close="true"
                                                            aria-expanded="false"
                                                            disabled={
                                                                updatingStatus ||
                                                                !['deliveredtostore'].includes(order.orderStatus?.toLowerCase())
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
                                                    <span className={`badge ${getPaymentStatusBadgeClass(order.paymentStatus)}`}>
                                                        {order.paymentStatus || 'Pending'}
                                                    </span>
                                                </td>
                                                <td>{new Date(order.orderDate).toLocaleDateString()}</td>
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
                                    <div className="alert alert-light border">
                                        <h6>Order Details</h6>
                                        <p><strong>Product:</strong> {selectedOrder.productName}</p>
                                        <p><strong>Customer:</strong> {selectedOrder.customerName}</p>
                                        <p><strong>Quantity:</strong> {selectedOrder.orderQuantity}</p>
                                        <p><strong>Price:</strong> ₹{selectedOrder.productPrice}</p>
                                        <p><strong>Total:</strong> ₹{selectedOrder.productPrice * selectedOrder.orderQuantity}</p>
                                        <p><strong>Order Date:</strong> {new Date(selectedOrder.orderDate).toLocaleDateString()}</p>
                                    </div>

                                    <div className="mt-3">
                                        <h6>Status Information</h6>
                                        <div className="d-flex justify-content-between mb-2">
                                            <div>
                                                <strong>Order Status:</strong>
                                                <span className={`badge ${getStatusBadgeClass(selectedOrder.orderStatus)} ms-2`}>
                                                    {selectedOrder.orderStatus}
                                                </span>
                                            </div>
                                            <div>
                                                <strong>Payment Status:</strong>
                                                <span className={`badge ${getPaymentStatusBadgeClass(selectedOrder.paymentStatus)} ms-2`}>
                                                    {selectedOrder.paymentStatus || 'Pending'}
                                                </span>
                                            </div>
                                        </div>
                                    </div>

                                    {selectedOrder.orderStatus?.toLowerCase() === 'deliveredtostore' && (
                                        <div className="alert alert-warning mt-3">
                                            <p className="mb-0">This order is ready to be delivered to the customer.</p>
                                            <button
                                                className="btn btn-success mt-2"
                                                onClick={() => {
                                                    handleStatusUpdate(selectedOrder.orderId, 'DeliveredToCustomer');
                                                    setShowModal(false);
                                                }}
                                                disabled={updatingStatus}
                                            >
                                                {updatingStatus ? 'Processing...' : 'Mark as Delivered to Customer'}
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
        </div>
    );
};

export default StoreDashboard;