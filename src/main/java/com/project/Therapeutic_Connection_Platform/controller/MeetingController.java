package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.model.Meeting;
import com.project.Therapeutic_Connection_Platform.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin(origins = "*") // Allow access from all sources during development
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping("/details/{userId}")
    public ResponseEntity<List<Meeting>> getMeetingDetails(@PathVariable Long userId) {
        List<Meeting> meetings = meetingService.getMeetings(userId);
        return ResponseEntity.ok(meetings);
    }
}
