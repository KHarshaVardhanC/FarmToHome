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
const fetchWithErrorHandling = async (url, options = {}) => {
  try {
    const defaultOptions = {
      headers: {
        'Content-Type': 'application/json'
      }
    };

    const response = await fetch(url, { ...defaultOptions, ...options });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'API request failed');
    }

    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
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
  const response = await axios.get(`/api/product/${productId}`);
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

export const submitRating = async (ratingData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/rating`, ratingData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error submitting rating:', error);
    throw error.response?.data || error;
  }
};

export const getProductRatings = async (productId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/ratings/product/${productId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching product ratings:', error);
    throw error.response?.data || error;
  }
};

export const getUserRatings = async (customerId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/ratings/customer/${customerId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching user ratings:', error);
    throw error.response?.data || error;
  }
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
// export const submitRating = async (ratingData) => {
//   try {
//     const response = await api.post('/rating', ratingData);
//     return response.data;
//   } catch (error) {
//     console.error('Rating submission error:', error.response?.data || error.message);
//     throw new Error(error.response?.data?.message || 'Failed to submit rating');
//   }
// };

/**
 * Get existing rating for a product by a customer
 * @param {number} customerId - Customer ID
 * @param {number} productId - Product ID
 * @returns {Promise} - Promise with the existing rating data
 */
export const getExistingRating = async (customerId, productId) => {
  try {
    const response = await fetch(
      `${API_BASE_URL}/ratings?customerId=${customerId}&productId=${productId}`,
      {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        }
      }
    );

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to fetch rating');
    }

    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
};

export const deleteRating = async (ratingId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/rating/${ratingId}`, {
    method: 'DELETE'
  });
};
export const fetchProductById = async (productId) => {
  return fetchWithErrorHandling(`${API_BASE_URL}/product/${productId}`);
};

// Move this to a separate utility file (e.g., orderUtils.js)
export const addRateButton = (orderId, productId, customerId, productInfo) => {
  return {
    label: 'Rate',
    onClick: (navigate) => {
      navigate(`/rate-product/${productId}`, {
        state: {
          customerId,
          productInfo
        }
      });
    }
  };
};

// Get products by category
export const adminApi = {
  // Seller management
  fetchSellers: async () => {
    try {
      const response = await api.get('/seller');
      return Array.isArray(response.data) ? response.data : [];
    } catch (error) {
      console.error('Error fetching sellers:', error);
      return []; // Return empty array on error
    }
  },

  fetchSellerById: async (sellerId) => {
    try {
      const response = await api.get(`/seller/${sellerId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching seller ${sellerId}:`, error);
      throw new Error(error.response?.data?.message || 'Failed to fetch seller details');
    }
  },

  deleteSeller: async (sellerId) => {
    try {
      const response = await api.delete(`/seller/${sellerId}`);
      return response.data;
    } catch (error) {
      console.error(`Error deleting seller ${sellerId}:`, error);
      throw new Error(error.response?.data?.message || 'Failed to delete seller');
    }
  },

  updateSellerStatus: async (sellerId, status) => {
    try {
      const response = await api.put(`/seller/${sellerId}/status`, { status });
      return response.data;
    } catch (error) {
      console.error(`Error updating seller ${sellerId} status:`, error);
      throw new Error(error.response?.data?.message || 'Failed to update seller status');
    }
  },

  fetchSellerProducts: async (sellerId) => {
    try {
      const response = await api.get(`/product/${sellerId}`);
      return Array.isArray(response.data) ? response.data : [];
    } catch (error) {
      console.error(`Error fetching seller ${sellerId} products:`, error);
      return []; // Return empty array on error
    }
  },

  // Customer management
  fetchCustomers: async () => {
    try {
      const response = await api.get('/customer/');
      return Array.isArray(response.data) ? response.data : [];
    } catch (error) {
      console.error('Error fetching customers:', error);
      return []; // Return empty array on error
    }
  },

  fetchCustomerById: async (customerId) => {
    try {
      const response = await api.get(`/customer/${customerId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching customer ${customerId}:`, error);
      throw new Error(error.response?.data?.message || 'Failed to fetch customer details');
    }
  },

  // Product management
  fetchProducts: async () => {
    try {
      const response = await api.get('/products');
      return Array.isArray(response.data) ? response.data : [];
    } catch (error) {
      console.error('Error fetching products:', error);
      return []; // Return empty array on error
    }
  },

  updateProduct: async (productId, productData) => {
    try {
      const response = await api.put(`/product/${productId}`, productData);
      return response.data;
    } catch (error) {
      console.error(`Error updating product ${productId}:`, error);
      throw new Error(error.response?.data?.message || 'Failed to update product');
    }
  },

  deleteProduct: async (productId) => {
    try {
      const response = await api.delete(`/product/${productId}`);
      return response.data;
    } catch (error) {
      console.error(`Error deleting product ${productId}:`, error);
      throw new Error(error.response?.data?.message || 'Failed to delete product');
    }
  }
};
// In your utils/api.js file

export const fetchProductReviews = async (productId) => {
  try {
    const response = await fetch(`${API_BASE_URL}/rating/product/${productId}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    console.log('Fetched ratings:', data);
    return data;
  } catch (error) {
    console.error('Error fetching product ratings:', error);
    throw error;
  }
};

export const {
  fetchSellers,
  fetchSellerById,
  deleteSeller,
  updateSellerStatus,
  fetchSellerProducts,
  fetchCustomers,
  fetchCustomerById,
  fetchProducts,
  updateProduct,
  deleteProduct
} = adminApi;
export default api;

// Changes done