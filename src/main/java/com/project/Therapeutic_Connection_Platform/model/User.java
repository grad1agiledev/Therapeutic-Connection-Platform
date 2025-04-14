package com.project.Therapeutic_Connection_Platform.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {


    /*
    for the firebase relability
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;


    @Column(name = "firebase_uid", nullable = false, unique = true)
    private String firebaseUid;

    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "email_address", nullable = false, unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phone;
    @Column(name = "optional_address")
    private String address;
    @Column(name="role",nullable = false)
    private String role;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    public User() {this.dateCreated = LocalDateTime.now();}

    public User(Long id,String uid, String fullName ,String email, String phone, String address, String role,LocalDateTime dateCreated)
    {
        this.id= id;
        this.firebaseUid = uid;
        this.fullName= fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.dateCreated = dateCreated;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() { return firebaseUid; }
    public void setUid(String uid) { this.firebaseUid= uid; }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
