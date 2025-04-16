import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FaStar } from 'react-icons/fa';
import './ApprovedReviews.css';

const ApprovedReviews = ({ therapistId }) => {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchApprovedReviews = async () => {
      try {
        setLoading(true);
        const response = await axios.get(`http://localhost:8080/api/reviews/therapist/${therapistId}?status=APPROVED`);
        setReviews(response.data);
        setLoading(false);
      } catch (err) {
        setError('An error occurred while loading reviews.');
        setLoading(false);
        console.error('Reviews loading error:', err);
      }
    };

    fetchApprovedReviews();
  }, [therapistId]);

  if (loading) {
    return <div className="loading">Loading reviews...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  if (reviews.length === 0) {
    return <div className="no-reviews">No approved reviews yet.</div>;
  }

  return (
    <div className="approved-reviews">
      <h3>Patient Reviews</h3>
      <div className="reviews-list">
        {reviews.map(review => (
          <div key={review.id} className="review-card">
            <div className="review-header">
              <div className="review-rating">
                {[...Array(5)].map((_, index) => (
                  <FaStar
                    key={index}
                    className="star"
                    color={index < review.rating ? "#ffc107" : "#e4e5e9"}
                    size={20}
                  />
                ))}
              </div>
              <div className="review-date">
                {new Date(review.createdAt).toLocaleDateString()}
              </div>
            </div>
            
            <div className="review-content">
              <p>{review.review}</p>
            </div>
            
            <div className="review-patient">
              <span className="patient-name">
                {review.patientName || 'Anonymous'}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ApprovedReviews; 