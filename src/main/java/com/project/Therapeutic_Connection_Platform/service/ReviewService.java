package com.project.Therapeutic_Connection_Platform.service;

import com.project.Therapeutic_Connection_Platform.model.Review;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final List<Review> reviews = new ArrayList<>();

    public Review createReview(Review review) {
        reviews.add(review);
        return review;
    }

    public List<Review> getAllReviews() {
        return new ArrayList<>(reviews);
    }

    public List<Review> getPendingReviews() {
        return reviews.stream()
                .filter(review -> review.getStatus() == Review.ReviewStatus.PENDING)
                .collect(Collectors.toList());
    }

    public Review getReviewById(String id) {
        return reviews.stream()
                .filter(review -> review.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Review approveReview(String id, String adminComment) {
        Review review = getReviewById(id);
        if (review != null) {
            review.setStatus(Review.ReviewStatus.APPROVED);
            review.setAdminComment(adminComment);
        }
        return review;
    }

    public Review rejectReview(String id, String adminComment) {
        Review review = getReviewById(id);
        if (review != null) {
            review.setStatus(Review.ReviewStatus.REJECTED);
            review.setAdminComment(adminComment);
        }
        return review;
    }

    public List<Review> getReviewsByTherapistId(String therapistId) {
        return reviews.stream()
                .filter(review -> review.getTherapistId().equals(therapistId))
                .collect(Collectors.toList());
    }

    public List<Review> getApprovedReviewsByTherapistId(String therapistId) {
        return reviews.stream()
                .filter(review -> review.getTherapistId().equals(therapistId))
                .filter(review -> review.getStatus() == Review.ReviewStatus.APPROVED)
                .collect(Collectors.toList());
    }
} 