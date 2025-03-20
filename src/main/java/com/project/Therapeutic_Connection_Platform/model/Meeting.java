package com.project.Therapeutic_Connection_Platform.model;
import java.util.List;

public class Meeting {
    private Long id;
    private String meetingName;
    private String meetingPassword;
    private List<Long> userIds; // List of user IDs associated with the meeting

    public Meeting(Long id, String meetingName, String meetingPassword, List<Long> userIds) {
        this.id = id;
        this.meetingName = meetingName;
        this.meetingPassword = meetingPassword;
        this.userIds = userIds;
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingPassword() {
        return meetingPassword;
    }   

    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public void addUser(Long userId) {
        this.userIds.add(userId);
    }

    public void removeUser(Long userId) {
        this.userIds.remove(userId);
    }

    public boolean containsUser(Long userId) {
        return this.userIds.contains(userId);
    }

}
