package com.project.Therapeutic_Connection_Platform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.Therapeutic_Connection_Platform.modelEnums.VerificationState;
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

    @ElementCollection
    @CollectionTable(name = "therapist_specializations", joinColumns = @JoinColumn(name = "therapist_id"))
    @Column(name = "specialization")
    private List<String> specializations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Location location;

    @ManyToMany
    @JoinTable(
            name = "therapist_languages",
            joinColumns = @JoinColumn(name = "therapist_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    @JsonIgnoreProperties("therapists")
    private List<Language> languages;
    @Column(name = "average_rating")
    private double rating;
    @Column(name = "session_cost")
    private double sessionCost;
    @Column(name = "profile_picture_url")
    private String profilePicture;
    @Column(name = "biography", columnDefinition = "TEXT")
    private String bio;
    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "is_virtual")
    private Boolean isVirtual;

    @Column(name="licence_number")
    private String licenceNumber;

    @Column(name="licence_document")
    private String licenceDocument;

    @Enumerated(EnumType.STRING)
    @Column(name="verification_state")
    private VerificationState verificationState = VerificationState.UNVERIFIED;

    @Column(name="verification_notes")
    private String verificationNotes;


    public Therapist() {
    }

    public Therapist(Long id, User user, List<String> specializations, Location location, List<Language> languages,
                    double rating, double sessionCost, String profilePicture, String bio, boolean isVerified, String licenceNumber,String licenceDocument,
                     VerificationState verificationState,String verificationNotes) {
        this.id = id;
        this.user= user;
        this.specializations = specializations;
        this.location = location;
        this.languages = languages;
        this.rating = rating;
        this.sessionCost = sessionCost;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.isVerified = isVerified;
        this.setLicenceNumber(licenceNumber);
        this.setLicenceDocument(licenceDocument);
        this.setVerificationNotes(verificationNotes);
        this.setVerificationState(verificationState);
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

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
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

    public Boolean isVirtual() {
        return isVirtual;
    }

    public void setVirtual(Boolean virtual) {
        isVirtual = virtual;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getLicenceDocument() {
        return licenceDocument;
    }

    public void setLicenceDocument(String licenceDocument) {
        this.licenceDocument = licenceDocument;
    }

    public VerificationState getVerificationState() {
        return verificationState;
    }

    public void setVerificationState(VerificationState verificationState) {
        this.verificationState = verificationState;
    }

    public String getVerificationNotes() {
        return verificationNotes;
    }

    public void setVerificationNotes(String verificationNotes) {
        this.verificationNotes = verificationNotes;
    }
}