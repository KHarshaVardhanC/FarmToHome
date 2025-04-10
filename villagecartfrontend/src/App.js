import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Home from './pages/Home';
import AddProduct from './pages/AddProduct';
import ViewProducts from './pages/ViewProducts';
import ProductDetails from './pages/ProductDetails';
import Profile from './pages/Profile';
import ViewOrders from './pages/ViewOrders';
import ViewRatings from './pages/ViewRatings';
import About from './pages/About';
import Contact from './pages/Contact';
import MainHome from './pages/MainHome';
// import SignIn from './pages/Login/SignIn';
// import SignUp from './pages/Login/SignUp';

// Styles
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './assets/styles.css';
import { i } from 'framer-motion/client';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<MainHome/>} />
          <Route path="/add-product" element={<AddProduct />} />
          <Route path="/view-products" element={<ViewProducts />} />
          <Route path="/view-orders" element={<ViewOrders />} />
          <Route path="/view-ratings" element={<ViewRatings />} />
          <Route path="/product/:id" element={<ProductDetails />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/SellerHome" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/add-product" element={<AddProduct />} />

          {/* ✅ Auth Routes */}
          {/* <Route path="/login" element={<SignIn />} />
          <Route path="/register" element={<SignUp />} /> */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
