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
    api.post(`/order/${orderId}/status`, status),
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



// Get products by category


export default api;