import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { JitsiMeeting } from "@jitsi/react-sdk";
import { useAuth } from '../../ProfileManagement/UserContext';
import "./VideoConference.css";

const VideoConference = () => {
   //get meeting details from /api/meeting/deatils/{userID}
    const { meetingID } = useParams(); // Get meetingID from URL
    const { currentUser } = useAuth();

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
        console.log(currentUser.displayName)
        fetchMeetingDetails();
    }
    , [meetingID]);

    return (
        <div className="video-conference-container">
                {(meetingDetails && currentUser) ? (<JitsiMeeting
                    roomName = {meetingDetails.meetingName + "-" +  meetingDetails.id}
                    userInfo={
                        {
                            //TODO: get the logged in username
                            displayName: currentUser.displayName || "Guest",
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
