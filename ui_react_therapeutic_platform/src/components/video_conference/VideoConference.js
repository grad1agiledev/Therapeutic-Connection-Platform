import React, { useEffect, useRef, useState } from "react";
import { JitsiMeeting } from "@jitsi/react-sdk";
import "./VideoConference.css";

const VideoConference = ({ userId }) => {
   

    return (
        <div className="video-conference-container">
            <div className="video-conference">
                <JitsiMeeting
                    roomName = "therapeutic-connection-platform"
                /> 
            </div>
        </div>
    );
};

export default VideoConference;
