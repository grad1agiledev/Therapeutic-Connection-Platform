import React , { useState } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TherapistList from './components/therapist/TherapistList';
import Register from './Registration/Register'
import Login from './Login/Login';
import ReviewManagement from './components/admin/ReviewManagement';

function App() {

//const [showRegister, setShowRegister] = useState(false);

  return (
  <Router>
    <div className="app-container">
      <header className="app-header">
        <h1>Therapeutic Connection Platform</h1>
        <nav className="app-nav">
          <Link to="/" className="nav-button">Home</Link>
          <Link to="/therapists" className="nav-button">Therapists</Link>
          <Link to="/about" className="nav-button">About Us</Link>
          <Link to="/login" className="nav-button login">Login</Link>
          <Link to="/register" className="nav-button register">Register</Link>
          <Link to="/admin/reviews" className="nav-button">Review Management</Link>
        </nav>
      </header>

     <main className="app-main">
               <Routes>
                 <Route path="/" element={<TherapistList />} />
                 <Route path="/register" element={<Register />} />
                 <Route path="/login" element={<Login />} />
                 <Route path="/admin/reviews" element={<ReviewManagement />} />
               </Routes>
             </main>

      
      <footer className="app-footer">
        <p>&copy; 2025 Therapeutic Connection Platform. All rights reserved.</p>
      </footer>
    </div>

</Router>

  );
}

export default App;
