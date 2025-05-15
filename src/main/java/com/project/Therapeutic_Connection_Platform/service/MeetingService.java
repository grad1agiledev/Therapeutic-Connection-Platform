package com.project.Therapeutic_Connection_Platform.service;

import com.project.Therapeutic_Connection_Platform.jpaRepos.MeetingRepository;
import com.project.Therapeutic_Connection_Platform.model.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    @Autowired
    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    // Get all meetings for a specific user ID
    public List<Meeting> getMeetings(String userId) {
        return meetingRepository.findByUserId(userId);
    }

    // Get a specific meeting by its ID
    public Meeting getMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId).orElse(null);
    }

    // Save a meeting, ensuring valid start and end times
    public Meeting saveMeeting(Meeting meeting) {
        if (meeting.getStartTime() == null || meeting.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time must be provided");
        }
        if (meeting.getStartTime().isAfter(meeting.getEndTime())) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        return meetingRepository.save(meeting);
    }

    // Get all meetings
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }
    public boolean deleteMeeting(Long meetingId) {
        if (!meetingRepository.existsById(meetingId)) {
            return false;
        }
        meetingRepository.deleteById(meetingId);
        return true;
    }
    
}
