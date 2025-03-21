import React, { useState } from 'react';
import axios from 'axios';
import { FaStar } from 'react-icons/fa';
import './TherapistReview.css';

const TherapistReview = ({ therapistId, sessionId, onReviewSubmitted }) => {
  const [rating, setRating] = useState(0);
  const [hover, setHover] = useState(0);
  const [review, setReview] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (rating === 0) {
      setError('Please select a rating');
      return;
    }

    if (review.trim().length < 10) {
      setError('Please write a review with at least 10 characters');
      return;
    }

    setIsSubmitting(true);
    setError('');

    try {
      await axios.post('http://localhost:8080/api/reviews', {
        therapistId,
        sessionId,
        rating,
        review: review.trim(),
      });

      setSuccess('Thank you for your review!');
      setRating(0);
      setReview('');
      if (onReviewSubmitted) {
        onReviewSubmitted();
      }
    } catch (err) {
      setError(err.response?.data?.message || 'An error occurred while submitting your review');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="therapist-review">
      <h3>Rate Your Session</h3>
      
      <div className="star-rating">
        {[...Array(5)].map((_, index) => {
          const ratingValue = index + 1;
          
          return (
            <label key={index}>
              <input
                type="radio"
                name="rating"
                value={ratingValue}
                onClick={() => setRating(ratingValue)}
              />
              <FaStar
                className="star"
                color={ratingValue <= (hover || rating) ? "#ffc107" : "#e4e5e9"}
                size={30}
                onMouseEnter={() => setHover(ratingValue)}
                onMouseLeave={() => setHover(0)}
              />
            </label>
          );
        })}
      </div>
      
      <form onSubmit={handleSubmit}>
        <textarea
          placeholder="Share your experience with this therapist..."
          value={review}
          onChange={(e) => setReview(e.target.value)}
          rows={4}
          required
        />
        
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
        
        <button 
          type="submit" 
          disabled={isSubmitting}
          className={isSubmitting ? 'submitting' : ''}
        >
          {isSubmitting ? 'Submitting...' : 'Submit Review'}
        </button>
      </form>
    </div>
  );
};

export default TherapistReview; 