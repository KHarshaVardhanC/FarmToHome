import React from "react";

function MyProfile() {
  const customerDetails = JSON.parse(localStorage.getItem("customerDetails"));

  if (!customerDetails) {
    return <p>No customer details available.</p>;
  }

  return (
    <div className="profile-page">
      <h1>My Profile</h1>
      <p>Name: {customerDetails.name}</p>
      <p>Email: {customerDetails.email}</p>
      <p>Phone: {customerDetails.phone}</p>
      <p>Address: {customerDetails.address}</p>
    </div>
  );
}

export default MyProfile;