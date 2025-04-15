import React from 'react';
import { Navigate } from 'react-router-dom';

const RequireAuthCustomer = ({ children }) => {
  const customerId = localStorage.getItem('customerId');

  if (!customerId) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default RequireAuthCustomer;