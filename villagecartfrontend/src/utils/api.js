import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor for handling errors
api.interceptors.request.use(
  (config) => {
    // Remove Content-Type for FormData requests
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type'];
    }
    // You can add auth headers here when you implement authentication
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor for handling errors
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// Utility function for fetch API error handling
// This is missing in the original code but referenced by other functions
const fetchWithErrorHandling = async (url, options = {}) => {
  try {
    const defaultOptions = {
      headers: {
        'Content-Type': 'application/json',
      },
    };

    const response = await fetch(url, { ...defaultOptions, ...options });

    if (!response.ok) {
      throw new Error(`Error: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error(`API Error: ${error.message}`);
    throw error;
  }
};

// Seller API endpoints
export const sellerApi = {
  login: (email, password) => api.post('/seller/login', { email, password }),
  getProfile: (sellerId) => api.get(`/seller/${sellerId}`),
  updateProfile: (sellerId, data) => api.put(`/seller/${sellerId}`, data),
};

// Product API endpoints
export const productApi = {
  addProduct: (productData) => {
    const config = {
      headers: {
        'Content-Type': productData instanceof FormData ? 'multipart/form-data' : 'application/json'
      }
    };
    return api.post('/product', productData, config);
  },
  getProducts: (sellerId) => api.get(`/product/${sellerId}`),
  updateProduct: (productId, productData) => api.put(`/product/${productId}`, productData),
  deleteProduct: (productId) => api.delete(`/product/${productId}`),
  getProduct: (productId) => api.get(`/${productId}`),
};

// Orders API endpoints
export const ordersApi = {
  getSellerOrders: async (sellerId) => {
    try {
      const response = await api.get(`/order/seller/${sellerId}`);
      return response;
    } catch (error) {
      // If it's a 404 or 500 for "no orders", return empty array
      if (error.response && (error.response.status === 404 || error.response.status === 500)) {
        return { data: [] }; // return empty orders safely
      }
      throw error; // other errors should still bubble up
    }
  },

  getOrderDetails: (orderId) => api.get(`/order/${orderId}`),

  updateOrderStatus: (orderId, status) =>
    // api.post(`/order/${orderId}/status`, status),
    api.put(`/order/order/${orderId}/${status}`),
};

// Ratings API endpoints
export const ratingsApi = {
  getProductRatings: (productId) => api.get(`/rating/product/${productId}`),
  // getSellerRatings: (sellerId) => api.get(`/rating/seller/${sellerId}`),
  deleteRating: (ratingId) => api.delete(`/rating/${ratingId}`)
};

export const customerApi = {
  getCustomer: (customerId) => api.get(`/customer/${customerId}`),
};

export async function getAllProducts() {
  const res = await axios.get(`${API_BASE_URL}/products`); // Fixed BASE_URL to API_BASE_URL
  return res.data;
}

export const getCategoryProducts = async (category) => {
  try {
    const response = await fetch(`http://localhost:8080/products/${category}`);

    if (!response.ok) {
      throw new Error('Failed to fetch category products');
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching category products:', error);
    throw error;
  }
};

export const getProductById = async (productId) => {
  const response = await axios.get(`/api/${productId}`);
  return response.data;
};

export const getOrderInvoice = async (orderId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/order/invoice/${orderId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching order invoice:', error);

    // Handle specific error cases
    if (error.response) {
      // The request was made and server responded with a status code
      if (error.response.status === 404) {
        throw new Error('Invoice not found. Please check the order ID and try again.');
      } else if (error.response.status === 403) {
        throw new Error('You do not have permission to view this invoice.');
      } else {
        throw new Error(`Server error: ${error.response.data.message || 'Unknown error occurred'}`);
      }
    } else if (error.request) {
      // The request was made but no response was received
      throw new Error('No response from server. Please check your internet connection.');
    } else {
      // Something happened in setting up the request
      throw new Error('Failed to send request. Please try again later.');
    }
  }
};

export const fetchProductsBySellerId = async (sellerId) => {
  const response = await fetch(`/product/${sellerId}`);
  if (!response.ok) {
    throw new Error('Failed to fetch products');
  }
  return response.json();
};

// SELLER API FUNCTIONS
// Remove this duplicate function - keeping the one that uses fetchWithErrorHandling
export const fetchSellers = async () => {
  try {
    const response = await api.get('/seller');
    console.log('Fetched sellers:', response.data); // Debug log
    return response.data;
  } catch (error) {
    console.error('Error fetching sellers:', error);
    throw error;
  }
};

export const fetchSellerById = async (sellerId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/seller/${sellerId}`);
};

export const createSeller = async (sellerData) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/seller`, {
    method: 'POST',
    body: JSON.stringify(sellerData)
  });
};

export const updateSeller = async (sellerId, sellerData) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/seller/${sellerId}`, {
    method: 'PUT',
    body: JSON.stringify(sellerData)
  });
};

export const deleteSeller = async (sellerId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/seller/${sellerId}`, {
    method: 'DELETE'
  });
};

export const updateSellerStatus = async (sellerId, status) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/seller/${sellerId}/${status}`, {
    method: 'PUT'
  });
};

export const loginSeller = async (credentials) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/seller/login`, {
    method: 'POST',
    body: JSON.stringify(credentials)
  });
};

// CUSTOMER API FUNCTIONS
export const fetchCustomers = async () => {
  return fetchWithErrorHandling(`${API_BASE_URL}/customer/`);
};

export const fetchCustomerById = async (customerId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/customer/${customerId}`);
};

export const createCustomer = async (customerData) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/customer`, {
    method: 'POST',
    body: JSON.stringify(customerData)
  });
};

export const updateCustomer = async (customerId, customerData) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/customer/${customerId}`, {
    method: 'PUT',
    body: JSON.stringify(customerData)
  });
};

export const loginCustomer = async (credentials) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/customer/login`, {
    method: 'POST',
    body: JSON.stringify(credentials)
  });
};

// PRODUCT API FUNCTIONS
export const fetchProducts = async () => {
  return fetchWithErrorHandling(`${API_BASE_URL}/products`);
};

// export const fetchProductById2 = async (productId) => {
//   return fetchWithErrorHandling(`${API_BASE_URL}/product2/${productId}`);
// };

export const fetchProductByName = async (productName) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/product1/${productName}`);
};

export const fetchProductsByCategory = async (category) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/products/${category}`);
};

export const fetchSellerProducts = async (sellerId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/product/${sellerId}`);
};

export const createProduct = async (productData) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/product`, {
    method: 'POST',
    body: JSON.stringify(productData)
  });
};

export const updateProduct = async (product) => {
  try {
    const response = await api.put(`/product/${product.productId}`, product);
    console.log('Product update response:', response.data); // Debug log
    return response.data;
  } catch (error) {
    console.error('Error updating product:', error);
    throw error;
  }
};

export const deleteProduct = async (productId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/product/${productId}`, {
    method: 'DELETE'
  });
};

// ORDER API FUNCTIONS
export const fetchOrders = async () => {
  return fetchWithErrorHandling(`${API_BASE_URL}/order`);
};

export const fetchSellerOrders = async (sellerId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/order/seller/${sellerId}`);
};

export const fetchCustomerOrders = async (customerId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/order/customer/${customerId}`);
};

export const fetchOrderInvoice = async (orderId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/order/invoice/${orderId}`);
};

export const fetchCustomerCartOrders = async (customerId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/order/orders/incart/${customerId}`);
};

export const createOrder = async (orderData) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/order/add`, {
    method: 'POST',
    body: JSON.stringify(orderData)
  });
};

export const updateOrderStatus = async (orderId, status) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/order/order/${orderId}/${status}`, {
    method: 'PUT'
  });
};

export const deleteOrder = async (orderId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/order/delete/${orderId}`, {
    method: 'DELETE'
  });
};

// RATING API FUNCTIONS
export const fetchRatings = async () => {
  return fetchWithErrorHandling(`${API_BASE_URL}/rating`);
};

export const fetchRatingById = async (ratingId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/rating/${ratingId}`);
};

export const fetchProductRatings = async (productId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/rating/product/${productId}`);
};

export const fetchCustomerRatings = async (customerId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/rating/customer/${customerId}`);
};

export const createRating = async (ratingData) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/rating`, {
    method: 'POST',
    body: JSON.stringify(ratingData)
  });
};

export const deleteRating = async (ratingId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/rating/${ratingId}`, {
    method: 'DELETE'
  });
};
export const fetchProductById = async (productId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/product/${productId}`);
};


// } catch (err) {
//   setError(`Failed to update product: ${err.message}`);
// } finally {
//   setLoading(false);
// }

// Combine all functions into a single object for the default export
const apiUtils = {
  // Seller functions
  fetchSellers,
  fetchSellerById,
  createSeller,
  updateSeller,
  deleteSeller,
  updateSellerStatus,
  loginSeller,

  // Customer functions
  fetchCustomers,
  fetchCustomerById,
  createCustomer,
  updateCustomer,
  loginCustomer,

  // Product functions
  fetchProducts,
  fetchProductById,
  fetchProductByName,
  fetchProductsByCategory,
  fetchSellerProducts,
  createProduct,
  updateProduct,
  deleteProduct,


  // Order functions
  fetchOrders,
  fetchSellerOrders,
  fetchCustomerOrders,
  fetchOrderInvoice,
  fetchCustomerCartOrders,
  createOrder,
  updateOrderStatus,
  deleteOrder,

  // Rating functions
  fetchRatings,
  fetchRatingById,
  fetchProductRatings,
  fetchCustomerRatings,
  createRating,
  deleteRating,

  // The axios instance
  api
};

// Choose one default export
export default apiUtils;