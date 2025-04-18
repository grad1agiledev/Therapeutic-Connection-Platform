package com.project.Therapeutic_Connection_Platform.service;

import org.springframework.stereotype.Service;
import com.project.Therapeutic_Connection_Platform.model.Meeting;
import java.util.Arrays;
import java.util.List;

@Service
public class MeetingService {

    //TODO: get the data from the database
    public List<Meeting> getMeetings(Long userId) {
        //return mock meeting list for now
        List<Meeting> meetingList = Arrays.asList(new Meeting(1L, Arrays.asList(1L, 2L, 3L)),
                new Meeting(2L, Arrays.asList(1L, 2L, 3L)),
                new Meeting(3L, Arrays.asList(1L, 2L, 3L)));
        return meetingList;
    }

    public Meeting getMeeting(Long meetingId) {
        //return mock meeting for now
        Meeting meeting = new Meeting(1L, Arrays.asList(1L, 2L, 3L));
        return meeting;
    }
}