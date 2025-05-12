import React from 'react';
import { Route, BrowserRouter as Router, Routes , useLocation, useNavigate} from 'react-router-dom';
import { useEffect } from 'react';

import CartPage from "./components/CartPage";
import MyOrders from './components/MyOrders';
import MyProfile from './components/MyProfile';
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

import CustomerProfilePage from './components/CustomerProfilePage';
// import SignUp from './pages/Login/SignUp';
import CustomerHomePage from './components/CustomerHomePage'; 


import RequireAuthSeller from './components/RequireAuth';
import ProductDetails from './pages/ProductDetails';
import Profile from './pages/Profile';
import Rating from './pages/Rating';
import ViewOrders from './pages/ViewOrders';
import ViewProducts from './pages/ViewProducts';
import ViewRatings from './pages/ViewRatings';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';



// Styles
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './assets/styles.css';
import RequireAuthCustomer from './components/RequireAuthCustomer';
import RequireAuthAdmin from './components/RequireAuthAdmin';


function NavigationBlocker() {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const handleBackButton = (e) => {
      // If on home page, prevent going back to login
      if (location.pathname === '/customer-home') {
        e.preventDefault();
        // Optionally show a confirmation dialog
        if (window.confirm('Are you sure you want to logout?')) {
          // Perform logout logic
          navigate('/login');
        }
      }
    };

    window.addEventListener('popstate', handleBackButton);
    return () => window.removeEventListener('popstate', handleBackButton);
  }, [location, navigate]);

  return null;
}





function App() {
  return (
    <Router>
       <NavigationBlocker />
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
            path="/SellerHome" element={<RequireAuthSeller> <Home /> </RequireAuthSeller>
            } />
          <Route path="/edit-product/:id" element={<RequireAuthSeller><EditProduct /></RequireAuthSeller>} />
          <Route path="review/:orderId" element={<Rating />} />

          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          
          <Route path="/admin" element={
            <RequireAuthAdmin><Admin /></RequireAuthAdmin>
          } />


          {/* <Route path="/add-product" element={<AddProduct />} /> */}
          {/* <Route path="/customer-home" element={<CustomerHomePage />} /> */}
          {/* <Route path="/cart" element={<CartPage />} /> */}
          {/* <Route path="/my-orders" element={<MyOrders />} /> */}
          {/* <Route path="/myprofile" element={<MyProfile />} /> */}

          <Route path="/invoice/:orderId" element={
            <RequireAuthCustomer> <OrderInvoice /> </RequireAuthCustomer>
          } />

          <Route path="/login" element={<SignIn />} />
          <Route path="/customer-home" element={
            <RequireAuthCustomer><CustomerHomePage /></RequireAuthCustomer>
          } />
          <Route path="/cart" element={
            <RequireAuthCustomer> <CartPage /></RequireAuthCustomer>
          } />
          <Route path="/my-orders" element={
            <RequireAuthCustomer> <MyOrders /></RequireAuthCustomer>
          } />
          <Route path="/myprofile" element={
            <RequireAuthCustomer> <MyProfile /></RequireAuthCustomer>
          } />


          {/* ✅ Auth Routes */}
          <Route path="/signup" element={<SignUp />} />
          <Route path="/login" element={<SignIn />} />

          {/* Rating Route */}
          {/* <Route
            path="/rate-product/:productId" element={<Rating />}
          /> */}

          <Route path="/signup" element={<SignUp />} />
          <Route path="/login" element={<SignIn />} />
        </Routes>
        <ToastContainer
          position="top-right"
          autoClose={3000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
        />
      </div>
    </Router>
  );
}

export default App;