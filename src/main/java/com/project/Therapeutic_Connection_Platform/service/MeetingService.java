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
        List<Meeting> meetingList = Arrays.asList(new Meeting(1L, "Group Therapy 1", "1234", Arrays.asList(1L, 2L, 3L)),
                new Meeting(2L, "Group Therapy 2", "1234", Arrays.asList(1L, 2L, 3L)),
                new Meeting(3L, "Group Therapy 3", "1234", Arrays.asList(1L, 2L, 3L)));
        return meetingList;
    }
}