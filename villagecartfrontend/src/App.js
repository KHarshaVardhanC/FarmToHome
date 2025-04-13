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
import SignIn from './pages/Login/SignIn';
import SignUp from './pages/Login/SignUp';
import OrderInvoice from './pages/OrderInvoice';
import CartPage from "./components/CartPage";
import MyOrders from './components/MyOrders';
import MyProfile from './components/MyProfile';

import CustomerProfilePage from './components/CustomerProfilePage';
// import SignUp from './pages/Login/SignUp';
import CustomerHomePage from './components/CustomerHomePage'; 
import Admin from './pages/Admin';
// import Rating from './pages/Rating';
import Rating from './pages/Rating'; // Import the Rating component
// import SignIn from './pages/Login/SignIn';
// import SignUp from './pages/Login/SignUp';
import EditProduct from './pages/EditProduct';


// Styles
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './assets/styles.css';


function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<MainHome />} />
          <Route path="/add-product" element={<AddProduct />} />
          <Route path="/view-products" element={<ViewProducts />} />
          <Route path="/view-orders" element={<ViewOrders />} />
          <Route path="/view-ratings" element={<ViewRatings />} />
          <Route path="/product/:id" element={<ProductDetails />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/SellerHome" element={<Home />} />
          <Route path="/edit-product/:id" element={<EditProduct />} />
          <Route path="/products/:productId/rate" element={<Rating />} />

          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/add-product" element={<AddProduct />} />
          <Route path="/admin" element={<Admin />} />

          <Route path="/invoice/:orderId" element={<OrderInvoice />} />
         
          <Route path="/login" element={<SignIn />} />
          <Route path="/customer-home" element={<CustomerHomePage />} /> 
          <Route path="/cart" element={<CartPage />} />
          <Route path="/my-orders" element={<MyOrders />} />
          <Route path="/customerprofile" element={<CustomerProfilePage />}/>
          

          {/* ✅ Auth Routes */}
        <Route path="/signup" element={<SignUp />} /> 
        <Route path="/login" element={<SignIn/>}/>

          {/* Rating Route */}
          {/* <Route
            path="/rate-product/:productId" element={<Rating />}
          /> */}

          <Route path="/signup" element={<SignUp />} />
          <Route path="/login" element={<SignIn />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;