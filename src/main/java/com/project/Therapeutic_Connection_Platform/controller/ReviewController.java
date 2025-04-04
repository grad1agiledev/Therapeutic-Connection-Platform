package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.model.Review;
import com.project.Therapeutic_Connection_Platform.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Review>> getPendingReviews() {
        return ResponseEntity.ok(reviewService.getPendingReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Review review = reviewService.getReviewById(id);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Review> approveReview(
            @PathVariable String id,
            @RequestBody(required = false) String adminComment) {
        Review review = reviewService.approveReview(id, adminComment);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Review> rejectReview(
            @PathVariable String id,
            @RequestBody(required = false) String adminComment) {
        Review review = reviewService.rejectReview(id, adminComment);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/therapist/{therapistId}")
    public ResponseEntity<List<Review>> getReviewsByTherapistId(@PathVariable String therapistId) {
        return ResponseEntity.ok(reviewService.getReviewsByTherapistId(therapistId));
    }

    @GetMapping("/therapist/{therapistId}/approved")
    public ResponseEntity<List<Review>> getApprovedReviewsByTherapistId(@PathVariable String therapistId) {
        return ResponseEntity.ok(reviewService.getApprovedReviewsByTherapistId(therapistId));
    }
} 