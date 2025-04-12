import React, { useState, useEffect } from 'react';
import '../styles/Admin.css';
import {
    fetchSellers,
    fetchSellerById,
    fetchCustomers,
    fetchCustomerById,
    deleteSeller,
    updateProduct,
    fetchSellerOrders,
    fetchCustomerOrders
} from '../utils/api';

const Admin = () => {
    const [view, setView] = useState('home'); // home, sellers, seller-details, customers, customer-details
    const [sellers, setSellers] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [selectedSeller, setSelectedSeller] = useState(null);
    const [selectedCustomer, setSelectedCustomer] = useState(null);
    const [editingProduct, setEditingProduct] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Load initial data based on the current view
        const loadData = async () => {
            setLoading(true);
            setError(null);

            try {
                if (view === 'sellers' || view === 'home') {
                    const sellersData = await fetchSellers();
                    setSellers(sellersData);
                }

                if (view === 'customers' || view === 'home') {
                    const customersData = await fetchCustomers();
                    setCustomers(customersData);
                }
            } catch (err) {
                setError(`Failed to load data: ${err.message}`);
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        loadData();
    }, [view]);

    const handleSellerClick = async (seller) => {
        setLoading(true);
        setError(null);
        try {
            // Fetch complete seller details including products
            const sellerDetails = await fetchSellerById(seller.id || seller.sellerId);
            // Fetch seller orders
            const sellerOrders = await fetchSellerOrders(seller.id || seller.sellerId);

            setSelectedSeller({
                ...sellerDetails,
                orders: sellerOrders
            });
            setView('seller-details');
        } catch (err) {
            setError(`Failed to load seller details: ${err.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleCustomerClick = async (customer) => {
        setLoading(true);
        setError(null);
        try {
            // Fetch complete customer details
            const customerDetails = await fetchCustomerById(customer.id || customer.customerId);
            // Fetch customer orders
            const customerOrders = await fetchCustomerOrders(customer.id || customer.customerId);

            setSelectedCustomer({
                ...customerDetails,
                orders: customerOrders
            });
            setView('customer-details');
        } catch (err) {
            setError(`Failed to load customer details: ${err.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteSeller = async (sellerId) => {
        if (window.confirm('Are you sure you want to delete this seller?')) {
            setLoading(true);
            setError(null);
            try {
                await deleteSeller(sellerId);
                // Refresh sellers list
                const updatedSellers = await fetchSellers();
                setSellers(updatedSellers);
                setView('sellers');
            } catch (err) {
                setError(`Failed to delete seller: ${err.message}`);
            } finally {
                setLoading(false);
            }
        }
    };

    const handleEditProduct = (product) => {
        setEditingProduct({ ...product });
    };

    const handleSaveProduct = async () => {
        setLoading(true);
        setError(null);
        try {
            await updateProduct(editingProduct);
            // Refresh seller data
            const updatedSeller = await fetchSellerById(selectedSeller.id || selectedSeller.sellerId);
            const sellerOrders = await fetchSellerOrders(selectedSeller.id || selectedSeller.sellerId);

            setSelectedSeller({
                ...updatedSeller,
                orders: sellerOrders
            });
            setEditingProduct(null);
        } catch (err) {
            setError(`Failed to update product: ${err.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleCancelEdit = () => {
        setEditingProduct(null);
    };

    const renderHome = () => (
        <div className="admin-home">
            <h1>Hello Admin</h1>
            <div className="admin-options">
                <button className="admin-option" onClick={() => setView('sellers')}>
                    View Sellers
                </button>
                <button className="admin-option" onClick={() => setView('customers')}>
                    View Customers
                </button>
            </div>
        </div>
    );

    const renderSellers = () => (
        <div className="admin-sellers">
            <h2>Sellers</h2>
            <button className="back-button" onClick={() => setView('home')}>Back to Home</button>

            {loading ? (
                <div className="loading">Loading sellers...</div>
            ) : error ? (
                <div className="error-message">{error}</div>
            ) : (
                <div className="sellers-list">
                    {sellers.length === 0 ? (
                        <p>No sellers found.</p>
                    ) : (
                        sellers.map(seller => (
                            <div key={seller.id || seller.sellerId} className="seller-card" onClick={() => handleSellerClick(seller)}>
                                <h3>{seller.name || `${seller.sellerFirstName} ${seller.sellerLastName}`}</h3>
                                <p>Email: {seller.email || seller.sellerEmail}</p>
                                <p>Phone: {seller.phone || seller.sellerMobileNumber}</p>
                                <p>Location: {seller.sellerCity}, {seller.sellerState}</p>
                                <p>Status: {seller.sellerStatus || 'Active'}</p>
                                <p>Products: {seller.productCount || '0'}</p>
                            </div>
                        ))
                    )}
                </div>
            )}
        </div>
    );

    const renderSellerDetails = () => {
        if (loading) return <div className="loading">Loading seller details...</div>;
        if (error) return <div className="error-message">{error}</div>;
        if (!selectedSeller) return <div>No seller selected</div>;

        return (
            <div className="seller-details">
                <button className="back-button" onClick={() => setView('sellers')}>Back to Sellers</button>
                <div className="seller-header">
                    <h2>{selectedSeller.name || `${selectedSeller.sellerFirstName} ${selectedSeller.sellerLastName}`}</h2>
                    <button className="delete-seller" onClick={() => handleDeleteSeller(selectedSeller.id || selectedSeller.sellerId)}>
                        Delete Seller
                    </button>
                </div>

                <div className="seller-info">
                    <p><strong>ID:</strong> {selectedSeller.id || selectedSeller.sellerId}</p>
                    <p><strong>Email:</strong> {selectedSeller.email || selectedSeller.sellerEmail}</p>
                    <p><strong>Phone:</strong> {selectedSeller.phone || selectedSeller.sellerMobileNumber}</p>
                    <p><strong>Date of Birth:</strong> {selectedSeller.sellerDOB && new Date(selectedSeller.sellerDOB).toLocaleDateString()}</p>
                    <p><strong>Address:</strong> {selectedSeller.address || `${selectedSeller.sellerPlace}, ${selectedSeller.sellerCity}, ${selectedSeller.sellerState} - ${selectedSeller.sellerPincode}`}</p>
                    <p><strong>Status:</strong> {selectedSeller.sellerStatus || 'Active'}</p>
                </div>

                <h3>Products</h3>
                <div className="products-list">
                    {!selectedSeller.products || selectedSeller.products.length === 0 ? (
                        <p>No products found for this seller.</p>
                    ) : (
                        selectedSeller.products.map(product => (
                            <div key={product.id} className="product-card">
                                {editingProduct && editingProduct.id === product.id ? (
                                    <div className="product-edit-form">
                                        <label>Name:</label>
                                        <input
                                            value={editingProduct.name}
                                            onChange={(e) => setEditingProduct({ ...editingProduct, name: e.target.value })}
                                        />
                                        <label>Description:</label>
                                        <textarea
                                            value={editingProduct.description}
                                            onChange={(e) => setEditingProduct({ ...editingProduct, description: e.target.value })}
                                        ></textarea>
                                        <label>Price:</label>
                                        <input
                                            type="number"
                                            value={editingProduct.price}
                                            onChange={(e) => setEditingProduct({ ...editingProduct, price: parseFloat(e.target.value) })}
                                        />
                                        <div className="edit-actions">
                                            <button onClick={handleSaveProduct}>Save</button>
                                            <button onClick={handleCancelEdit}>Cancel</button>
                                        </div>
                                    </div>
                                ) : (
                                    <>
                                        <h4>{product.name}</h4>
                                        <p>{product.description}</p>
                                        <p>Price: ${product.price}</p>
                                        <p>Rating: {product.rating || 'No ratings'} {product.rating ? '⭐' : ''}</p>
                                        <button className="edit-product" onClick={() => handleEditProduct(product)}>
                                            Edit Product
                                        </button>
                                    </>
                                )}
                            </div>
                        ))
                    )}
                </div>

                <h3>Orders</h3>
                <div className="orders-list">
                    {!selectedSeller.orders || selectedSeller.orders.length === 0 ? (
                        <p>No orders found for this seller.</p>
                    ) : (
                        selectedSeller.orders.map(order => (
                            <div key={order.id || order.orderId} className="order-card">
                                <p><strong>Order ID:</strong> {order.id || order.orderId}</p>
                                <p><strong>Customer ID:</strong> {order.customerId}</p>
                                <p><strong>Customer:</strong> {order.customerName}</p>
                                <p><strong>Product ID:</strong> {order.productId}</p>
                                <p><strong>Quantity:</strong> {order.orderQuantity}</p>
                                <p><strong>Date:</strong> {order.date && new Date(order.date).toLocaleDateString()}</p>
                                <p><strong>Status:</strong> {order.status || order.orderStatus}</p>
                                <p><strong>Total:</strong> ${order.total}</p>
                            </div>
                        ))
                    )}
                </div>
            </div>
        );
    };

    const renderCustomers = () => (
        <div className="admin-customers">
            <h2>Customers</h2>
            <button className="back-button" onClick={() => setView('home')}>Back to Home</button>

            {loading ? (
                <div className="loading">Loading customers...</div>
            ) : error ? (
                <div className="error-message">{error}</div>
            ) : (
                <div className="customers-list">
                    {customers.length === 0 ? (
                        <p>No customers found.</p>
                    ) : (
                        customers.map(customer => (
                            <div key={customer.id || customer.customerId} className="customer-card" onClick={() => handleCustomerClick(customer)}>
                                <h3>{customer.name || `${customer.customerFirstName} ${customer.customerLastName}`}</h3>
                                <p>Email: {customer.email || customer.customerEmail}</p>
                                <p>Phone: {customer.phone || customer.customerPhoneNumber}</p>
                                <p>Location: {customer.customerCity}, {customer.customerState}</p>
                                <p>Status: {customer.customerIsActive ? 'Active' : 'Inactive'}</p>
                                <p>Orders: {customer.orderCount || '0'}</p>
                            </div>
                        ))
                    )}
                </div>
            )}
        </div>
    );

    const renderCustomerDetails = () => {
        if (loading) return <div className="loading">Loading customer details...</div>;
        if (error) return <div className="error-message">{error}</div>;
        if (!selectedCustomer) return <div>No customer selected</div>;

        return (
            <div className="customer-details">
                <button className="back-button" onClick={() => setView('customers')}>Back to Customers</button>
                <h2>{selectedCustomer.name || `${selectedCustomer.customerFirstName} ${selectedCustomer.customerLastName}`}</h2>

                <div className="customer-info">
                    <p><strong>ID:</strong> {selectedCustomer.id || selectedCustomer.customerId}</p>
                    <p><strong>Email:</strong> {selectedCustomer.email || selectedCustomer.customerEmail}</p>
                    <p><strong>Phone:</strong> {selectedCustomer.phone || selectedCustomer.customerPhoneNumber}</p>
                    <p><strong>Address:</strong> {selectedCustomer.address || `${selectedCustomer.customerPlace}, ${selectedCustomer.customerCity}, ${selectedCustomer.customerState} - ${selectedCustomer.customerPincode}`}</p>
                    <p><strong>Status:</strong> {selectedCustomer.customerIsActive ? 'Active' : 'Inactive'}</p>
                    <p><strong>Member Since:</strong> {selectedCustomer.memberSince ? new Date(selectedCustomer.memberSince).toLocaleDateString() : 'N/A'}</p>
                </div>

                <h3>Orders</h3>
                <div className="orders-list">
                    {!selectedCustomer.orders || selectedCustomer.orders.length === 0 ? (
                        <p>No orders found for this customer.</p>
                    ) : (
                        selectedCustomer.orders.map(order => (
                            <div key={order.id || order.orderId} className="order-card">
                                <p><strong>Order ID:</strong> {order.id || order.orderId}</p>
                                <p><strong>Product ID:</strong> {order.productId}</p>
                                <p><strong>Quantity:</strong> {order.orderQuantity}</p>
                                <p><strong>Date:</strong> {order.date && new Date(order.date).toLocaleDateString()}</p>
                                <p><strong>Status:</strong> {order.status || order.orderStatus}</p>
                                <p><strong>Total:</strong> ${order.total}</p>

                                {order.items && order.items.length > 0 && (
                                    <div className="order-items">
                                        <h4>Items</h4>
                                        <ul>
                                            {order.items.map(item => (
                                                <li key={item.id || `${order.id || order.orderId}-${item.productId}`}>
                                                    {item.name || item.productName} - ${item.price} x {item.quantity}
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                )}
                            </div>
                        ))
                    )}
                </div>
            </div>
        );
    };

    return (
        <div className="admin-container">
            {error && <div className="global-error">{error}</div>}

            {view === 'home' && renderHome()}
            {view === 'sellers' && renderSellers()}
            {view === 'seller-details' && renderSellerDetails()}
            {view === 'customers' && renderCustomers()}
            {view === 'customer-details' && renderCustomerDetails()}
        </div>
    );
};

export default Admin;