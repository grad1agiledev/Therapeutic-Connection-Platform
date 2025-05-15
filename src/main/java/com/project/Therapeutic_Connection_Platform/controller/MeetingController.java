package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.model.Meeting;
import com.project.Therapeutic_Connection_Platform.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    // Get all meetings involving a specific user
    @GetMapping("/details/{userId}")
    public ResponseEntity<List<Meeting>> getMeetingDetails(@PathVariable String userId) {
        List<Meeting> meetings = meetingService.getMeetings(userId);
        return ResponseEntity.ok(meetings);
    }

    // Get a specific meeting by ID
    @GetMapping("/{meetingId}")
    public ResponseEntity<Meeting> getMeeting(@PathVariable Long meetingId) {
        Meeting meeting = meetingService.getMeeting(meetingId);
        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meeting);
    }

    // Create a new meeting (expects JSON with startTime and endTime in ISO-8601 format)
    @PostMapping("/create")
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) {
        System.err.println("Creating meeting: " + meeting.getStartTime() + " to " + meeting.getEndTime());
        if (meeting.getStartTime() == null || meeting.getEndTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        Meeting savedMeeting = meetingService.saveMeeting(meeting);
        return ResponseEntity.ok(savedMeeting);
    }

    //list all meetings
    @GetMapping("/list")
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(meetings);
    }

    // Delete a specific meeting by ID
@GetMapping("/delete/{meetingId}")
public ResponseEntity<Void> deleteMeeting(@PathVariable Long meetingId) {
    boolean deleted = meetingService.deleteMeeting(meetingId);
    if (!deleted) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
}

}
