import React, { useState } from 'react';
import './TherapistCard.css';

const TherapistCard = ({ therapist, onViewProfile }) => {
  const [showDetails, setShowDetails] = useState(false);

  const toggleDetails = () => {
    setShowDetails(!showDetails);
  };

  const handleViewProfile = () => {
    if (onViewProfile) {
      onViewProfile(therapist.id);
    }
  };

  return (
    <div className="therapist-card">
      <div className="therapist-card-header">
        <img 
          src={therapist.profilePicture} 
          alt={`${therapist.name} profile photo`} 
          className="therapist-image"
        />
        <div className="therapist-basic-info">
          <h2 className="therapist-name">{therapist.user.fullName}</h2>
          <p className="therapist-specialization">{therapist.specialization}</p>
          <div className="therapist-rating">
            <span className="star">★</span> {therapist.rating.toFixed(1)}
          </div>
          {therapist.verified && (
            <div className="verified-badge">
              <span className="verified-icon">✓</span> Verified
            </div>
          )}
        </div>
      </div>

      <div className="therapist-info">
        <div className="therapist-location">
          <strong>Location:</strong> {therapist.location.name},{therapist.location.country}
        </div>
        <div className="therapist-languages">
          <strong>Languages:</strong> {therapist.languages && therapist.languages.length > 0 ? therapist.languages.join(', ') : 'Not specified'}
        </div>

        <div className="therapist-session-cost">
          <strong>Session Cost:</strong> ${therapist.sessionCost.toFixed(2)}
        </div>
      </div>

      {showDetails ? (
        <div className="therapist-details">
          <h3>About</h3>
          <p className="therapist-bio">{therapist.bio}</p>
          <button 
            className="details-button" 
            onClick={toggleDetails}
          >
            Show Less
          </button>
        </div>
      ) : (
        <button 
          className="details-button" 
          onClick={toggleDetails}
        >
          Show More
        </button>
      )}
      
      <div className="therapist-actions">
        <button 
          className="action-button view-profile"
          onClick={handleViewProfile}
        >
          View Profile
        </button>
        <button className="action-button book-appointment" disabled>
          Book Appointment (Login Required)
        </button>
      </div>
    </div>
  );
};

export default TherapistCard; 