import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import AddProduct from './pages/AddProduct';
import ViewProducts from './pages/ViewProducts';
import ProductDetails from './pages/ProductDetails';
import Profile from './pages/Profile';
import ViewOrders from './pages/ViewOrders';
import ViewRatings from './pages/ViewRatings';

// Import styles
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './assets/styles.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/add-product" element={<AddProduct />} />
          <Route path="/view-products" element={<ViewProducts />} />
          <Route path="/view-orders" element={<ViewOrders />} />
          <Route path="/view-ratings" element={<ViewRatings />} />
          <Route path="/product/:id" element={<ProductDetails />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
