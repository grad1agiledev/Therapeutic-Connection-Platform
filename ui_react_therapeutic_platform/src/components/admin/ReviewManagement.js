import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './ReviewManagement.css';

const ReviewManagement = () => {
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [adminComment, setAdminComment] = useState('');

    useEffect(() => {
        fetchPendingReviews();
    }, []);

    const fetchPendingReviews = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/reviews/pending');
            setReviews(response.data);
            setLoading(false);
        } catch (err) {
            setError('Yorumlar yüklenirken bir hata oluştu.');
            setLoading(false);
        }
    };

    const handleApprove = async (reviewId) => {
        try {
            await axios.post(`http://localhost:8080/api/reviews/${reviewId}/approve`, adminComment);
            setAdminComment('');
            fetchPendingReviews();
        } catch (err) {
            setError('Yorum onaylanırken bir hata oluştu.');
        }
    };

    const handleReject = async (reviewId) => {
        try {
            await axios.post(`http://localhost:8080/api/reviews/${reviewId}/reject`, adminComment);
            setAdminComment('');
            fetchPendingReviews();
        } catch (err) {
            setError('Yorum reddedilirken bir hata oluştu.');
        }
    };

    if (loading) {
        return <div className="loading">Yükleniyor...</div>;
    }

    if (error) {
        return <div className="error">{error}</div>;
    }

    return (
        <div className="review-management">
            <h1>Yorum Yönetimi</h1>
            <p className="subtitle">Onay Bekleyen Yorumlar</p>

            {reviews.length === 0 ? (
                <div className="no-reviews">Onay bekleyen yorum bulunmamaktadır.</div>
            ) : (
                <div className="reviews-list">
                    {reviews.map(review => (
                        <div key={review.id} className="review-card">
                            <div className="review-header">
                                <div className="review-meta">
                                    <span className="rating">Puan: {review.rating}/5</span>
                                    <span className="date">
                                        {new Date(review.createdAt).toLocaleDateString()}
                                    </span>
                                </div>
                            </div>
                            
                            <div className="review-content">
                                <p>{review.comment}</p>
                            </div>

                            <div className="review-actions">
                                <textarea
                                    placeholder="Yönetici yorumu (opsiyonel)"
                                    value={adminComment}
                                    onChange={(e) => setAdminComment(e.target.value)}
                                    className="admin-comment"
                                />
                                
                                <div className="action-buttons">
                                    <button
                                        className="approve-button"
                                        onClick={() => handleApprove(review.id)}
                                    >
                                        Onayla
                                    </button>
                                    <button
                                        className="reject-button"
                                        onClick={() => handleReject(review.id)}
                                    >
                                        Reddet
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ReviewManagement; 