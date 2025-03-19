import React, { useEffect, useRef, useState } from "react";
import "./VideoConference.css";

const VideoConference = ({ userId }) => {
    const jitsiContainerRef = useRef(null);
    const [meetingData, setMeetingData] = useState(null);
    const jitsiApiRef = useRef(null);

    useEffect(() => {
        const fetchMeetingDetails = async () => {
            try {
                const response = await fetch(`/meeting?userId=${userId}`);
                if (!response.ok) throw new Error("Failed to fetch meeting details");

                const meetings = await response.json();
                if (meetings.length > 0) {
                    setMeetingData(meetings[0]); // Assuming one meeting per user at a time
                }
            } catch (error) {
                console.error("Error fetching meeting details:", error);
            }
        };

        fetchMeetingDetails();
    }, [userId]);

    useEffect(() => {
        if (jitsiContainerRef.current) {
            // Load Jitsi API script dynamically
            const script = document.createElement("script");
            script.src = "https://meet.jit.si/external_api.js";
            script.async = true;
            script.onload = initializeJitsi;
            document.body.appendChild(script);
        }
    },);

    const initializeJitsi = () => {
        if (jitsiApiRef.current) return;

        const domain = "meet.jit.si";
        const options = {
            roomName: "threapy-app-test-randomsentencefortest175478",
            meetingPassword: "1234",
            width: "100%",
            height: "100%",
            parentNode: jitsiContainerRef.current,
            userInfo: {
                displayName: "User " + userId,
            },
        };

        jitsiApiRef.current = new window.JitsiMeetExternalAPI(domain, options);
        
        // Wait until the user becomes a moderator to set the password
        jitsiApiRef.current.addEventListener("participantRoleChanged", function (event) {
            if (event.role === "moderator") {
                jitsiApiRef.current.executeCommand("password", meetingData.meetingPassword);
            }
        });

        // If the meeting is password-protected, enter the password when prompted
        jitsiApiRef.current.addEventListener("passwordRequired", function () {
            jitsiApiRef.current.executeCommand("password", meetingData.meetingPassword);
        });
    };

    return (
        <div className="video-conference-container">
            <h2>Video Conference</h2>
            {(
                <div ref={jitsiContainerRef} className="video-conference-iframe" />
            )
               }
        </div>
    );
};

export default VideoConference;
