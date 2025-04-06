import React, { useEffect, useRef, useState } from "react";
import { JitsiMeeting } from "@jitsi/react-sdk";
import "./VideoConference.css";

const VideoConference = ({ userId }) => {
   

    return (
        <div className="video-conference-container">
                <JitsiMeeting
                    roomName = "therapeutic-connection-platform"
                    getIFrameRef = { (iframeRef) => { iframeRef.className = 'jitsi-meeting-iframe'; } }
                /> 
        </div>
    );
};

export default VideoConference;
