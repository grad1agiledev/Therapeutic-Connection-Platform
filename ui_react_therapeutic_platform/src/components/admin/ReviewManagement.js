import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import Alert from '@mui/material/Alert';

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
            const response = await axios.get('https://therapeutic-connection-platform-1.onrender.com/api/reviews/pending');
            setReviews(response.data);
            setLoading(false);
        } catch (err) {
            setError('An error occurred while loading reviews.');
            setLoading(false);
        }
    };

    const handleApprove = async (reviewId) => {
        try {
            await axios.post(`https://therapeutic-connection-platform-1.onrender.com/api/reviews/${reviewId}/approve`, adminComment);
            setAdminComment('');
            fetchPendingReviews();
        } catch (err) {
            setError('An error occurred while approving the review.');
        }
    };

    const handleReject = async (reviewId) => {
        try {
            await axios.post(`https://therapeutic-connection-platform-1.onrender.com/api/reviews/${reviewId}/reject`, adminComment);
            setAdminComment('');
            fetchPendingReviews();
        } catch (err) {
            setError('An error occurred while rejecting the review.');
        }
    };

    if (loading) {
        return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 8 }}><CircularProgress /></Box>;
    }

    if (error) {
        return <Alert severity="error">{error}</Alert>;
    }

    return (
        <Box sx={{ maxWidth: 800, mx: 'auto', mt: 6 }}>
            <Typography variant="h4" color="#5D4037" fontWeight={700} gutterBottom>Review Management</Typography>
            <Typography variant="subtitle1" color="text.secondary" mb={3}>Pending Reviews</Typography>

            {reviews.length === 0 ? (
                <Alert severity="info">There are no pending reviews.</Alert>
            ) : (
                <Box>
                    {reviews.map(review => (
                        <Card key={review.id} sx={{ mb: 3, boxShadow: 2, borderRadius: 2 }}>
                            <CardContent>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                                    <Typography variant="subtitle2" color="text.secondary">
                                        Rating: {review.rating}/5
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">
                                        {new Date(review.createdAt).toLocaleDateString()}
                                    </Typography>
                                </Box>
                                <Typography variant="body1" sx={{ mb: 2 }}>{review.comment}</Typography>
                                <TextField
                                    label="Admin comment (optional)"
                                    value={adminComment}
                                    onChange={(e) => setAdminComment(e.target.value)}
                                    fullWidth
                                    multiline
                                    minRows={2}
                                    sx={{ mb: 2 }}
                                />
                                <Box sx={{ display: 'flex', gap: 2 }}>
                                    <Button
                                        variant="contained"
                                        color="success"
                                        onClick={() => handleApprove(review.id)}
                                        sx={{ fontWeight: 600 }}
                                    >
                                        Approve
                                    </Button>
                                    <Button
                                        variant="contained"
                                        color="error"
                                        onClick={() => handleReject(review.id)}
                                        sx={{ fontWeight: 600 }}
                                    >
                                        Reject
                                    </Button>
                                </Box>
                            </CardContent>
                        </Card>
                    ))}
                </Box>
            )}
        </Box>
    );
};

export default ReviewManagement; 