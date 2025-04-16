import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './TherapistDetail.css';
import { Link } from 'react-router-dom';
import ApprovedReviews from './ApprovedReviews';
//import Register from '././Registration/Register';

const TherapistDetail = ({ therapistId, onBack }) => {
  const [therapist, setTherapist] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTherapistDetail = async () => {
      try {
        setLoading(true);
        const response = await axios.get(`http://localhost:8080/api/therapists/${therapistId}`);
        setTherapist(response.data);
        setLoading(false);
      } catch (err) {
        setError('An error occurred while loading therapist information. Please try again later.');
        setLoading(false);
        console.error('Therapist detail loading error:', err);
      }
    };

    fetchTherapistDetail();
  }, [therapistId]);

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  if (!therapist) {
    return <div className="error">Therapist not found.</div>;
  }

  return (
    <div className="therapist-detail-container">
      <button className="back-button" onClick={onBack}>
        ← Back to Therapist List
      </button>
      
      <div className="therapist-detail-header">
        <img 
          src={therapist.profilePicture} 
          alt={`${therapist.name} profile photo`} 
          className="therapist-detail-image"
        />
        <div className="therapist-detail-info">
          <h1 className="therapist-detail-name">{therapist.name}</h1>
          <p className="therapist-detail-specialization">{therapist.specialization}</p>
          
          <div className="therapist-detail-meta">
            <div className="therapist-detail-rating">
              <span className="star">★</span> {therapist.rating.toFixed(1)}
            </div>
            
            {therapist.isVerified && (
              <div className="verified-badge">
                <span className="verified-icon">✓</span> Verified
              </div>
            )}
          </div>
        </div>
      </div>
      
      <div className="therapist-detail-section">
        <h2>General Information</h2>
        <div className="therapist-detail-grid">
          <div className="detail-item">
            <h3>Location</h3>
            <p>{therapist.location}</p>
          </div>
          
          <div className="detail-item">
            <h3>Languages</h3>
            <p>{therapist.languages.join(', ')}</p>
          </div>
          
          <div className="detail-item">
            <h3>Session Cost</h3>
            <p>${therapist.sessionCost.toFixed(2)}</p>
          </div>
        </div>
      </div>
      
      <div className="therapist-detail-section">
        <h2>About</h2>
        <p className="therapist-detail-bio">{therapist.bio}</p>
      </div>

      <ApprovedReviews therapistId={therapistId} />
      
      <div className="therapist-detail-actions">
        <button className="detail-action-button book-appointment" disabled>
          Book Appointment (Login Required)
        </button>
        <p className="login-prompt">
         To book an appointment, please <Link to="/login">login</Link> or <Link to="/register">register</Link>.
        </p>
      </div>
    </div>
  );
};

export default TherapistDetail; 