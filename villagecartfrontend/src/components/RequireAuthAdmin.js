import React from 'react';
import { Navigate } from 'react-router-dom';

const RequireAuthAdmin = ({ children }) => {
  const adminId = localStorage.getItem('adminId');

  console.log(adminId);

  if (!adminId) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default RequireAuthAdmin;