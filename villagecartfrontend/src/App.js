import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import About from './pages/About';
import AddProduct from './pages/AddProduct';
import Admin from './pages/Admin';
import Contact from './pages/Contact';
import EditProduct from './pages/EditProduct';
import Home from './pages/Home';
import SignIn from './pages/Login/SignIn';
import SignUp from './pages/Login/SignUp';
import MainHome from './pages/MainHome';
import OrderInvoice from './pages/OrderInvoice';
import CartPage from "./components/CartPage";
import MyOrders from './components/MyOrders';
import MyProfile from './components/MyProfile';

import CustomerProfilePage from './components/CustomerProfilePage';
// import SignUp from './pages/Login/SignUp';
import CustomerHomePage from './components/CustomerHomePage'; 

import ProductDetails from './pages/ProductDetails';
import Profile from './pages/Profile';
import Rating from './pages/Rating';
import ViewOrders from './pages/ViewOrders';
import ViewProducts from './pages/ViewProducts';
import ViewRatings from './pages/ViewRatings';
import RequireAuthSeller from './components/RequireAuth';


// Styles
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './assets/styles.css';


function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<MainHome />} />
          <Route path="/add-product" element={
            <RequireAuthSeller>
              <AddProduct />
            </RequireAuthSeller>
          } />
          <Route path="/view-products" element={
            <RequireAuthSeller>
              <ViewProducts />
            </RequireAuthSeller>
          } />
          <Route path="/view-orders" element={
            <RequireAuthSeller>
              <ViewOrders />
            </RequireAuthSeller>
          } />
          <Route path="/view-ratings" element={
            <RequireAuthSeller> <ViewRatings />           </RequireAuthSeller>
          } />
          <Route path="/product/:id" element={
            <RequireAuthSeller>  <ProductDetails />          </RequireAuthSeller>
          } />
          <Route path="/profile" element={
            <RequireAuthSeller> <Profile />           </RequireAuthSeller>
          } />
          <Route
            path="/SellerHome"
            element={
              <RequireAuthSeller>
                <Home />
              </RequireAuthSeller>
            }
          />
          <Route path="/edit-product/:id" element={<EditProduct />} />
          <Route path="review/:orderId" element={<Rating />} />

          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/add-product" element={<AddProduct />} />
          <Route path="/admin" element={<Admin />} />

          <Route path="/invoice/:orderId" element={<OrderInvoice />} />
         
          <Route path="/login" element={<SignIn />} />
          <Route path="/customer-home" element={<CustomerHomePage />} /> 
          <Route path="/cart" element={<CartPage />} />
          <Route path="/my-orders" element={<MyOrders />} />
          <Route path="/myprofile" element={<MyProfile />}/>
          

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