import React, { useEffect, useRef, useState } from "react";
import { JitsiMeeting } from "@jitsi/react-sdk";
import "./VideoConference.css";

const VideoConference = ({ meetingID }) => {
   //get meeting details from /api/meeting/deatils/{userID}
    const [meetingDetails, setMeetingDetails] = useState(null);
    useEffect(() => {
        const fetchMeetingDetails = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/meetings/${meetingID}`);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                console.log('Meeting details:', data);
                setMeetingDetails(data);
            } catch (error) {
                console.error('Error fetching meeting details:', error);
            }
        };

        fetchMeetingDetails();
    }
    , [meetingID]);

    return (
        <div className="video-conference-container">
                {(meetingDetails) ? (<JitsiMeeting
                    roomName = {meetingDetails.meetingName}
                    userInfo={
                        {
                            //TODO: get the logged in username
                            displayName: "Logged in user",
                        }
                    }
                    getIFrameRef = { (iframeRef) => { iframeRef.className = 'jitsi-meeting-iframe'; } }
                /> )
                :
                (<div className="loading">Loading...</div>)}
        </div>  
    );
};

export default VideoConference;
