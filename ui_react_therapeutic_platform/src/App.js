import React, {useState} from 'react';
import './App.css';
import TherapistList from './components/therapist/TherapistList';
import VideoConference from './components/video_conference/VideoConference';

function App() {

  const [isVideoConferenceActive, setIsVideoConferenceActive] = useState(false);
  const isDevelopment = process.env.REACT_APP_ENV === 'development';
  //TODO: Replace this with the actual user ID of the logged in user
  const meetingID = "1";
  
  const handleStartVideoConference = () => {
    // Toggle video conference state
    setIsVideoConferenceActive(!isVideoConferenceActive);
  };
  
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
          {/* TODO: Remove this button after real implementation */}
          {isDevelopment && (
          <button className="nav-button" onClick={handleStartVideoConference}>Start Video Conference</button>
          )}
        </nav>
      </header>
      
      <main className="app-main">
      {isVideoConferenceActive ? (
          <VideoConference meetingID={meetingID} />
        ) : (
          <TherapistList />
        )}
      </main>
      
      <footer className="app-footer">
        <p>&copy; 2025 Therapeutic Connection Platform. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default App;
