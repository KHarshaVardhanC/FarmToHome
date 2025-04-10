import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Link, useNavigate } from 'react-router-dom';
import './App.css';
import CustomerHomePage from './components/CustomerHomePage';
import CartPage from './components/CartPage';
import MyOrdersPage from './components/MyOrdersPage';
import ProfileModal from './components/ProfileModal';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<CustomerHomePage />} />
        <Route path="/cart" element={<CartPage />} />
        <Route path="/my-orders" element={<MyOrdersPage />} />
      </Routes>
    </Router>
  );
}

export default App;