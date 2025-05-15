package com.project.Therapeutic_Connection_Platform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ElementCollection
    @CollectionTable(name = "meeting_user_ids", joinColumns = @JoinColumn(name = "meeting_id"))
    @Column(name = "user_id")
    private List<String> participantIds = new ArrayList<>();

    public Meeting() {}

    public Meeting(String meetingName, LocalDateTime startTime, LocalDateTime endTime, List<String> participantIds) {
        this.meetingName = meetingName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participantIds = participantIds;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public void addUserId(String userId) {
        this.participantIds.add(userId);
    }

    public void removeUserId(String userId) {
        this.participantIds.remove(userId);
    }

    public boolean containsUserId(String userId) {
        return this.participantIds.contains(userId);
    }
}
