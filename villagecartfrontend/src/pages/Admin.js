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
    fetchCustomerOrders,
    updateSellerStatus,
    deleteProduct,
    fetchSellerProducts,
    fetchProducts
} from '../utils/api';

const Admin = () => {
    const [view, setView] = useState('home'); // home, products, sellers, seller-details, customers, customer-details
    const [sellers, setSellers] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [products, setProducts] = useState([]);
    const [selectedSeller, setSelectedSeller] = useState(null);
    const [selectedCustomer, setSelectedCustomer] = useState(null);
    const [editingProduct, setEditingProduct] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Helper function to get seller name from ID
    const getSellerNameById = (sellerId) => {
        const seller = sellers.find(s => (s.id || s.sellerId) === sellerId);
        return seller ? (seller.name || `${seller.sellerFirstName} ${seller.sellerLastName}`) : 'Unknown Seller';
    };

    // Helper function to calculate order total
    const calculateOrderTotal = (order) => {
        return (order.orderQuantity || 0) * (order.productPrice || 0);
    };

    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            setError(null);

            try {
                if (view === 'sellers' || view === 'home') {
                    try {
                        const sellersData = await fetchSellers();
                        setSellers(sellersData || []);
                    } catch (err) {
                        console.error('Error fetching sellers:', err);
                        setError(err.message || 'Failed to load sellers');
                    }
                }

                if (view === 'customers' || view === 'home') {
                    try {
                        const customersData = await fetchCustomers();
                        setCustomers(customersData || []);
                    } catch (err) {
                        console.error('Error fetching customers:', err);
                        setError(prev => prev ? `${prev}; Failed to load customers` : 'Failed to load customers');
                    }
                }

                if (view === 'products' || view === 'home') {
                    try {
                        const productsData = await fetchProducts();
                        setProducts(productsData || []);
                    } catch (err) {
                        console.error('Error fetching products:', err);
                        setError(prev => prev ? `${prev}; Failed to load products` : 'Failed to load products');
                    }
                }
            } catch (err) {
                console.error('General error:', err);
                setError('Failed to load data - please check your connection');
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
            // Get sellerId consistently
            const sellerId = seller.id || seller.sellerId;
            console.log('Fetching details for seller:', sellerId);

            // Fetch seller details first
            const sellerDetails = await fetchSellerById(sellerId);

            // Try to fetch orders and products separately with error handling
            let sellerOrders = [];
            let sellerProducts = [];

            try {
                sellerOrders = await fetchSellerOrders(sellerId);
            } catch (orderErr) {
                console.error('Error fetching seller orders:', orderErr);
                // Proceed without orders rather than failing the whole operation
            }

            try {
                sellerProducts = await fetchSellerProducts(sellerId);
            } catch (productErr) {
                console.error('Error fetching seller products:', productErr);
                // Proceed without products rather than failing the whole operation
            }

            setSelectedSeller({
                ...sellerDetails,
                orders: sellerOrders || [],
                products: sellerProducts || []
            });
            setView('seller-details');
        } catch (err) {
            console.error('Error in handleSellerClick:', err);
            setError(`Failed to load seller details: ${err.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleCustomerClick = async (customer) => {
        setLoading(true);
        setError(null);
        try {
            // Get customerId consistently
            const customerId = customer.id || customer.customerId;

            // Fetch customer details and orders in parallel
            const [customerDetails, customerOrders] = await Promise.all([
                fetchCustomerById(customerId),
                fetchCustomerOrders(customerId)
            ]);

            // Enhance orders with product details
            const ordersWithProducts = await Promise.all(
                customerOrders.map(async (order) => {
                    const product = order.product || {};

                    return {
                        ...order,
                        product
                    };
                })
            );

            setSelectedCustomer({
                ...customerDetails,
                orders: ordersWithProducts || []
            });
            setView('customer-details');
        } catch (err) {
            console.error('Error in handleCustomerClick:', err);
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
            // Ensure we have all required fields
            if (!editingProduct.productName || !editingProduct.productPrice || !editingProduct.productQuantity) {
                throw new Error('Please fill in all required fields');
            }

            // Format the data
            const formattedProduct = {
                ...editingProduct,
                productPrice: parseFloat(editingProduct.productPrice),
                productQuantity: parseFloat(editingProduct.productQuantity)
            };

            console.log('Updating product:', formattedProduct);

            // Update the product
            await updateProduct(formattedProduct);

            if (view === 'products') {
                // Refresh all products if we're in the products view
                const productsData = await fetchProducts();
                setProducts(productsData);
            } else if (view === 'seller-details' && selectedSeller) {
                // Refresh seller products if we're in seller details
                const sellerProducts = await fetchSellerProducts(selectedSeller.sellerId);
                setSelectedSeller(prev => ({
                    ...prev,
                    products: sellerProducts || []
                }));
            }

            setEditingProduct(null);
            // Show success message
            setError('Product updated successfully!');
            setTimeout(() => setError(null), 3000); // Clear success message after 3 seconds
        } catch (err) {
            console.error('Error updating product:', err);
            setError(`Failed to update product: ${err.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteProduct = async (productId) => {
        if (window.confirm('Are you sure you want to delete this product?')) {
            setLoading(true);
            setError(null);
            try {
                await deleteProduct(productId);

                // Refresh products data
                const productsData = await fetchProducts();
                setProducts(productsData);
            } catch (err) {
                setError(`Failed to delete product: ${err.message}`);
            } finally {
                setLoading(false);
            }
        }
    };

    const handleCancelEdit = () => {
        setEditingProduct(null);
    };

    const renderHome = () => (
        <div className="admin-home">
            <h1>Hello Admin</h1>
            <div className="admin-options">
                <button className="admin-option" onClick={() => setView('products')}>
                    View All Products
                </button>
                <button className="admin-option" onClick={() => setView('sellers')}>
                    View Sellers
                </button>
                <button className="admin-option" onClick={() => setView('customers')}>
                    View Customers
                </button>
            </div>
        </div>
    );

    const renderProducts = () => {
        if (loading) return <div className="loading">Loading products...</div>;
        if (error) return <div className="error-message">{error}</div>;

        return (
            <div className="admin-products">
                <h2>All Products</h2>
                <button className="back-button" onClick={() => setView('home')}>Back to Home</button>

                {editingProduct ? (
                    <div className="edit-product-form">
                        <h3>Edit Product</h3>
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            handleSaveProduct();
                        }}>
                            <div className="form-group">
                                <label>Product Name:</label>
                                <input
                                    type="text"
                                    value={editingProduct.productName}
                                    onChange={(e) => setEditingProduct({
                                        ...editingProduct,
                                        productName: e.target.value
                                    })}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label>Price:</label>
                                <input
                                    type="number"
                                    value={editingProduct.productPrice}
                                    onChange={(e) => setEditingProduct({
                                        ...editingProduct,
                                        productPrice: parseFloat(e.target.value)
                                    })}
                                    required
                                    min="0"
                                    step="0.01"
                                />
                            </div>
                            <div className="form-group">
                                <label>Quantity:</label>
                                <input
                                    type="number"
                                    value={editingProduct.productQuantity}
                                    onChange={(e) => setEditingProduct({
                                        ...editingProduct,
                                        productQuantity: parseFloat(e.target.value)
                                    })}
                                    required
                                    min="0"
                                    step="0.01"
                                />
                            </div>
                            <div className="form-group">
                                <label>Quantity Type:</label>
                                <select
                                    value={editingProduct.productQuantityType || ''}
                                    onChange={(e) => setEditingProduct({
                                        ...editingProduct,
                                        productQuantityType: e.target.value
                                    })}
                                    required
                                >
                                    <option value="">Select Type</option>
                                    <option value="kg">Kilograms (kg)</option>
                                    <option value="g">Grams (g)</option>
                                    <option value="l">Liters (L)</option>
                                    <option value="ml">Milliliters (ml)</option>
                                    <option value="pcs">Pieces</option>
                                    <option value="dozen">Dozen</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Description:</label>
                                <textarea
                                    value={editingProduct.productDescription}
                                    onChange={(e) => setEditingProduct({
                                        ...editingProduct,
                                        productDescription: e.target.value
                                    })}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label>Category:</label>
                                <select
                                    value={editingProduct.productCategory}
                                    onChange={(e) => setEditingProduct({
                                        ...editingProduct,
                                        productCategory: e.target.value
                                    })}
                                    required
                                >
                                    <option value="">Select Category</option>
                                    <option value="VEGETABLES">Vegetables</option>
                                    <option value="FRUITS">Fruits</option>
                                    <option value="DAIRY">Dairy</option>
                                    <option value="GRAINS">Grains</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Image URL:</label>
                                <input
                                    type="text"
                                    value={editingProduct.ImageUrl || ''}
                                    onChange={(e) => setEditingProduct({
                                        ...editingProduct,
                                        ImageUrl: e.target.value
                                    })}
                                />
                            </div>
                            <div className="form-actions">
                                <button type="submit" className="save-button">
                                    Save Changes
                                </button>
                                <button
                                    type="button"
                                    className="cancel-button"
                                    onClick={handleCancelEdit}
                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                    </div>
                ) : (
                    <div className="products-list">
                        {!products || products.length === 0 ? (
                            <p>No products found.</p>
                        ) : (
                            products.map(product => (
                                <div key={product.productId} className="product-card">
                                    <h4>{product.productName}</h4>
                                    {product.imageUrl && (
                                        <img
                                            src={product.imageUrl}
                                            alt={product.productName}
                                            className="product-image"
                                        />
                                    )}
                                    <p><strong>Seller:</strong> {getSellerNameById(product.sellerId)}</p>
                                    <p>{product.productDescription}</p>
                                    <p><strong>Price:</strong> Rs.{product.productPrice}</p>
                                    <p><strong>Category:</strong> {product.productCategory}</p>
                                    <p><strong>Quantity:</strong> {product.productQuantity} {product.productQuantityType}</p>

                                    {product.productRatingValue > 0 && (
                                        <div className="product-rating">
                                            <p><strong>Rating:</strong> {product.productRatingValue.toFixed(1)}/5
                                                ({product.productRatingCount} review{product.productRatingCount !== 1 ? 's' : ''})</p>
                                            <div className="rating-stars">
                                                {[...Array(5)].map((_, i) => (
                                                    <span
                                                        key={i}
                                                        className={`star ${i < Math.round(product.productRatingValue) ? 'filled' : ''}`}
                                                    >
                                                        ★
                                                    </span>
                                                ))}
                                            </div>
                                        </div>
                                    )}

                                    <div className="product-actions">
                                        <button
                                            className="edit-product"
                                            onClick={() => handleEditProduct(product)}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            className="delete-product"
                                            onClick={() => handleDeleteProduct(product.productId)}
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                )}
            </div>
        );
    };

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
                    {!sellers || sellers.length === 0 ? (
                        <p>No sellers found.</p>
                    ) : (
                        sellers.map(seller => {
                            // Find products for this seller to get accurate count
                            const sellerProducts = products.filter(p =>
                                p.sellerId === (seller.id || seller.sellerId)
                            );

                            return (
                                <div
                                    key={seller.id || seller.sellerId}
                                    className="seller-card"
                                    onClick={() => handleSellerClick(seller)}
                                >
                                    <h3>{seller.name || `${seller.sellerFirstName} ${seller.sellerLastName}`}</h3>
                                    <p>Email: {seller.email || seller.sellerEmail}</p>
                                    <p>Phone: {seller.phone || seller.sellerMobileNumber}</p>
                                    <p>Location: {seller.sellerCity || 'Unknown'}, {seller.sellerState || 'Unknown'}</p>
                                    <p>Status: {seller.sellerStatus || 'Active'}</p>
                                    <p>Products: {sellerProducts.length}</p>
                                </div>
                            );
                        })
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
                    <div className="seller-actions">
                        <button className="delete-seller" onClick={() => handleDeleteSeller(selectedSeller.id || selectedSeller.sellerId)}>
                            Delete Seller
                        </button>
                    </div>
                </div>

                <div className="seller-info">
                    <p><strong>ID:</strong> {selectedSeller.id || selectedSeller.sellerId}</p>
                    <p><strong>Email:</strong> {selectedSeller.email || selectedSeller.sellerEmail}</p>
                    <p><strong>Phone:</strong> {selectedSeller.phone || selectedSeller.sellerMobileNumber}</p>
                    <p><strong>Address:</strong> {selectedSeller.address ||
                        (selectedSeller.sellerPlace ?
                            `${selectedSeller.sellerPlace}, ${selectedSeller.sellerCity || ''}, ${selectedSeller.sellerState || ''} - ${selectedSeller.sellerPincode || ''}`
                            : 'Address not available')}</p>
                    <p><strong>Status:</strong> {selectedSeller.sellerStatus || 'Active'}</p>
                </div>

                <h3>Products ({selectedSeller.products?.length || 0})</h3>
                <div className="products-list">
                    {!selectedSeller.products || selectedSeller.products.length === 0 ? (
                        <p>No products found for this seller.</p>
                    ) : (
                        selectedSeller.products.map(product => (
                            <div key={product.productId} className="product-card">
                                <h4>{product.productName}</h4>
                                {product.imageUrl && (
                                    <img
                                        src={product.imageUrl}
                                        alt={product.productName}
                                        className="product-image"
                                    />
                                )}
                                <p>{product.productDescription}</p>
                                <p><strong>Price:</strong> Rs.{product.productPrice}</p>
                                <p><strong>Category:</strong> {product.productCategory}</p>
                                <p><strong>Quantity:</strong> {product.productQuantity} {product.productQuantityType}</p>

                                {product.productRatingValue > 0 && (
                                    <div className="product-rating">
                                        <p><strong>Rating:</strong> {product.productRatingValue.toFixed(1)}/5
                                            ({product.productRatingCount} review{product.productRatingCount !== 1 ? 's' : ''})</p>
                                    </div>
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
                        selectedSeller.orders.map(order => {
                            // Find product details for this order if available
                            const product = products.find(p => p.productId === order.productId) || {};
                            const total = calculateOrderTotal(order);

                            return (
                                <div key={order.id || order.orderId} className="order-card">
                                    <p><strong>Order ID:</strong> {order.id || order.orderId}</p>
                                    <p><strong>Customer:</strong> {order.customerName}</p>

                                    <div className="order-product">
                                    <>


                            <p><strong>Product:</strong> {order.productName || 'Unknown Product'}</p>
                            <p><strong>Price:</strong> Rs.{order.productPrice}</p>
                            </ >

                                    </div>

                                    <p><strong>Quantity:</strong> {order.orderQuantity}</p>
                                    <p><strong>Status:</strong> {order.status || order.orderStatus}</p>
                                    <p><strong>Total:</strong> Rs.{calculateOrderTotal(order)}</p>
                                </div>
                            );
                        })
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
                    {!customers || customers.length === 0 ? (
                        <p>No customers found.</p>
                    ) : (
                        customers.map(customer => {
                            // We would ideally fetch accurate order counts here
                            // For now, we can estimate based on any orders data we might have
                            const customerOrders = customer.orders || [];

                            return (
                                <div
                                    key={customer.id || customer.customerId}
                                    className="customer-card"
                                    onClick={() => handleCustomerClick(customer)}
                                >
                                    <h3>{customer.name || `${customer.customerFirstName} ${customer.customerLastName}`}</h3>
                                    <p>Email: {customer.email || customer.customerEmail}</p>
                                    <p>Phone: {customer.phone || customer.customerPhoneNumber}</p>
                                    <p>Location: {customer.customerCity || 'Unknown'}, {customer.customerState || 'Unknown'}</p>
                                    <p>Status: {customer.customerIsActive ? 'Active' : 'Inactive'}</p>
                                    <p>Orders: {customerOrders.length || '0'}</p>
                                </div>
                            );
                        })
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
                    <p><strong>Address:</strong> {selectedCustomer.address ||
                        (selectedCustomer.customerPlace ?
                            `${selectedCustomer.customerPlace}, ${selectedCustomer.customerCity || ''}, ${selectedCustomer.customerState || ''} - ${selectedCustomer.customerPincode || ''}`
                            : 'Address not available')}</p>
                    <p><strong>Status:</strong> {selectedCustomer.customerIsActive ? 'Active' : 'Inactive'}</p>
                </div>

                <h3>Orders ({selectedCustomer.orders?.length || 0})</h3>
                <div className="orders-list">
                    {!selectedCustomer.orders || selectedCustomer.orders.length === 0 ? (
                        <p>No orders found for this customer.</p>
                    ) : (
                        selectedCustomer.orders.map(order => {
                            // Find product details for this order if available
                            const product = products.find(p => p.productId === order.productId) || {};
                            const total = calculateOrderTotal(order);

                            return (
                                <div key={order.id || order.orderId} className="order-card">
                                    <p><strong>Order ID:</strong> {order.id || order.orderId}</p>

                                    <div className="order-product">
                                        {product.imageUrl && (
                                            <img
                                                src={product.imageUrl}
                                                alt={product.productName}
                                                className="product-image-small"
                                            />
                                        )}
                                        <p><strong>Product:</strong> {order.productName || 'Unknown Product'}</p>


                                    </div>

                                    <p><strong>Quantity:</strong> {order.orderQuantity}</p>
                                    <p><strong>Status:</strong> {order.status || order.orderStatus}</p>
                                    <p><strong>Total:</strong> Rs.{total}</p>

                                    {order.items && order.items.length > 0 && (
                                        <div className="order-items">
                                            <h4>Items</h4>
                                            <ul>
                                                {order.items.map(item => (
                                                    <li key={item.id || `${order.id || order.orderId}-${item.productId}`}>
                                                        {item.name || item.productName} - Rs.{item.price} x {item.quantity}
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>
                                    )}
                                </div>
                            );
                        })
                    )}
                </div>
            </div>
        );
    };

    return (
        <div className="admin-container">
            {error && <div className="global-error">{error}</div>}

            {view === 'home' && renderHome()}
            {view === 'products' && renderProducts()}
            {view === 'sellers' && renderSellers()}
            {view === 'seller-details' && renderSellerDetails()}
            {view === 'customers' && renderCustomers()}
            {view === 'customer-details' && renderCustomerDetails()}
        </div>
    );
};

export default Admin;