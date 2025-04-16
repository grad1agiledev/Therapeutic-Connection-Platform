// src/ProfileManagement/NavBar.js
import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from './ProfileManagement/UserContext';

function NavBar() {
  const { currentUser } = useAuth();
 return (
    <nav className="app-nav">
      <Link to="/" className="nav-button">Home</Link>
      <Link to="/therapists" className="nav-button">Therapists</Link>
      <Link to="/about" className="nav-button">About Us</Link>

      {}
      <Link to="/login" className="nav-button login">Login</Link>
      <Link to="/register" className="nav-button register">Register</Link>

      {/* Only showing when logged in */}
      {currentUser && (
        <>
          <Link to="/profile" className="nav-button">Profile</Link>
          <Link to="/admin/reviews" className="nav-button">Review Management</Link>
        </>
      )}
    </nav>
  );
}

export default NavBar;
