import React from 'react';
import { Navigate } from 'react-router-dom';

const RequireAuth = ({ children }) => {
  const sellerId = localStorage.getItem('sellerId');

  if (!sellerId) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default RequireAuth;
