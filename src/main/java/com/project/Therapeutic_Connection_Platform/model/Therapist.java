package com.project.Therapeutic_Connection_Platform.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "therapists")
public class Therapist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false, unique = true)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "area_of_specialization")
    private String specialization;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;
    @ElementCollection
    @CollectionTable(name = "therapist_languages", joinColumns = @JoinColumn(name = "therapist_id"))
    @Column(name = "language")
    private List<String> languages;
    @Column(name = "average_rating")
    private double rating;
    @Column(name = "session_cost")
    private double sessionCost;
    @Column(name = "profile_picture_url")
    private String profilePicture;
    @Column(name = "biography", columnDefinition = "TEXT")
    private String bio;
    @Column(name = "is_verified")
    private boolean isVerified;

    public Therapist() {
    }

    public Therapist(Long id, User user, String specialization, Location location, List<String> languages,
                    double rating, double sessionCost, String profilePicture, String bio, boolean isVerified) {
        this.id = id;
        this.user= user;
        this.specialization = specialization;
        this.location = location;
        this.languages = languages;
        this.rating = rating;
        this.sessionCost = sessionCost;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.isVerified = isVerified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getSessionCost() {
        return sessionCost;
    }

    public void setSessionCost(double sessionCost) {
        this.sessionCost = sessionCost;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
} 