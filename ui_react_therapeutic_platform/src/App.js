import React , { useState } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import TherapistList from './components/therapist/TherapistList';
import Register from './Registration/Register'
import { Link } from 'react-router-dom';

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
        </nav>
      </header>




     <main className="app-main">
               <Routes>
                 <Route path="/" element={<TherapistList />} />
                 <Route path="/register" element={<Register />} />
               </Routes>
             </main>

      
      <footer className="app-footer">
        <p>&copy; 2023 Therapeutic Connection Platform. All rights reserved.</p>
      </footer>
    </div>

</Router>

  );
}

export default App;
