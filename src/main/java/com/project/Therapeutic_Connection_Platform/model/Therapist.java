package com.project.Therapeutic_Connection_Platform.model;

import java.util.List;

public class Therapist {
    private String id;
    private String name;
    private String specialization;
    private String location;
    private List<String> languages;
    private double rating;
    private double sessionCost;
    private String profilePicture;
    private String bio;
    private boolean isVerified;

    public Therapist() {
    }

    public Therapist(String id, String name, String specialization, String location, List<String> languages, 
                    double rating, double sessionCost, String profilePicture, String bio, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.location = location;
        this.languages = languages;
        this.rating = rating;
        this.sessionCost = sessionCost;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.isVerified = isVerified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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