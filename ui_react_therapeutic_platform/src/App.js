import React , { useState } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TherapistList from './components/therapist/TherapistList';
import Register from './Registration/Register'
import Login from './Login/Login';
import ReviewManagement from './components/admin/ReviewManagement';
import Profile from './ProfileManagement/Profile';
import NavBar from './NavBar';
import { AuthProvider } from './ProfileManagement/UserContext';
import VerificationBoard from './components/admin/VerificationBoard';
import { useAuth }        from './ProfileManagement/UserContext';

function AboutUs() {
  return (
    <div>
      <h2>About Us</h2>
      <p>This is the Therapeutic Connection Platform.</p>
    </div>
  );
}

function AdminRoute({ children }) {
  const { userRole } = useAuth();
  return userRole === 'admin' ? children : <p>403 – admins only</p>;
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="app-container">
          <header className="app-header">
            <h1>Therapeutic Connection Platform</h1>
            <NavBar />
          </header>

          <main className="app-main">
            <Routes>
              <Route path="/" element={<TherapistList />} />
              <Route path="/therapists" element={<TherapistList />} />
              <Route path="/about" element={<AboutUs />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/admin/reviews" element={<ReviewManagement />} />
              <Route path="/profile" element={<Profile />} />
              <Route  path="/admin/verification" element={  <AdminRoute> <VerificationBoard /> </AdminRoute>         }
              />

            </Routes>
          </main>

          <footer className="app-footer">
            <p>&copy; 2025 Therapeutic Connection Platform. All rights reserved.</p>
          </footer>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;