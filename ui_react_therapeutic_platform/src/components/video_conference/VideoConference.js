import React, { useRef ,useState } from "react";
import "./VideoConference.css";

const VideoConference = () => {

    return (
        <div className="video-conference-container">
            <h2>Video Conference</h2>
            <iframe
                src="https://meet.jit.si/threapy-app-test-randomsentencefortest175478"
                allow="camera; microphone; fullscreen; display-capture"
                style={{ height: "100%", width: "100%" }}
                className="video-conference-iframe"
            />
        </div>
    );
};

export default VideoConference;
