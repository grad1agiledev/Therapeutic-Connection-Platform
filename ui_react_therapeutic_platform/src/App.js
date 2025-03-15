import React from 'react';
import './App.css';
import TherapistList from './components/therapist/TherapistList';

function App() {
  return (
    <div className="app-container">
      <header className="app-header">
        <h1>Therapeutic Connection Platform</h1>
        <nav className="app-nav">
          <button className="nav-button">Home</button>
          <button className="nav-button active">Therapists</button>
          <button className="nav-button">About Us</button>
          <button className="nav-button login">Login</button>
          <button className="nav-button register">Register</button>
        </nav>
      </header>
      
      <main className="app-main">
        <TherapistList />
      </main>
      
      <footer className="app-footer">
        <p>&copy; 2023 Therapeutic Connection Platform. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default App;
