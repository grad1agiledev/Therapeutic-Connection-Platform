import React, { useState, useEffect } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import "./Calendar.css";
import { useAuth } from '../../ProfileManagement/UserContext'; // adjust the path if different

function Calendar() {
  const { currentUser } = useAuth();
  const [events, setEvents] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    title: '',
    date: '',
    startTime: '',
    endTime: '',
    participants: [],
  });

  // Fetch user meetings when currentUser is available
    useEffect(() => {
      if (!currentUser?.uid) return;

      fetchMeetings(currentUser.uid)
    }, [currentUser]);

  const fetchMeetings = (userId) => {
    fetch(`http://localhost:8080/api/meetings/details/${userId}`)
      .then(res => res.json())
      .then(meetings => {
        const calendarEvents = meetings.map(meeting => ({
          id: meeting.id,
          title: meeting.meetingName,
          start: new Date(meeting.startTime).toISOString(),
          end: new Date(meeting.endTime).toISOString(),
          allDay: false,
        }));
        setEvents(calendarEvents);
      })
      .catch(error => console.error("Error loading meetings:", error));
  };

  // Handle form submission to add event to backend
  const handleSubmit = (e) => {
    e.preventDefault();


    // Create participants list

    const participantIds = Array.from(new Set([
      ...formData.participants,
      currentUser.uid
    ]));
    
    const eventData = {
          meetingName: formData.title,
          startTime: `${formData.date}T${formData.startTime}`,
          endTime: `${formData.date}T${formData.endTime}`,
          participantIds: participantIds,
        };

    console.log("Event data to be sent:", eventData);
  
    // Send the new event to the backend to be saved
    fetch('http://localhost:8080/api/meetings/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(eventData),
    })
      .then(res => res.json())
      .then(savedEvent => {
        fetchMeetings(currentUser.uid); // Refresh the events list
        setShowModal(false);
        setFormData({
          title: '',
          date: '',
          startTime: '',
          endTime: '',
          participants: [],
        });
      })
      .catch(err => console.error("Error creating event:", err));
  };
  

  // Handle modal form field changes
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
      participants: name === 'participants' ? value.split(',').map(email => email.trim()) : prev.participants,
    }));
  };

  // Handle selecting a date on the calendar
  const createEvent = (date) => {
    const formattedDate = date.toISOString().split('T')[0];
    setFormData(prev => ({
      ...prev,
      date: formattedDate,
    }));
    setShowModal(true);
  };

  return (
    <div className="calendar-container">
      <button onClick={() => createEvent(new Date())}>Create Event</button>

      {showModal && (
        <div className="modal">
          <form className="modal-content" onSubmit={handleSubmit}>
            <h2>Create Event</h2>

            <label>Title:</label>
            <input name="title" value={formData.title} onChange={handleChange} required />

                <label>Date (YYYY-MM-DD):</label>
                <input type="date" name="date" value={formData.date} onChange={handleChange} required />

            <label>Start Time:</label>
            <input type="time" name="startTime" value={formData.startTime} onChange={handleChange} required />

            <label>End Time:</label>
            <input type="time" name="endTime" value={formData.endTime} onChange={handleChange} required />

            <label>Participants:</label>
            <input
              type="text"
              name="participants"
              value={formData.participants}
              onChange={handleChange}
              placeholder="Enter participant emails, separated by commas"
            />

            <button type="submit">Save</button>
            <button type="button" onClick={() => setShowModal(false)}>Cancel</button>
          </form>
        </div>
      )}

      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin]}
        headerToolbar={{
          left: 'dayGridMonth,dayGridWeek,dayGridDay',
          center: 'title',
          right: 'prev,next today',
        }}
        initialView="dayGridMonth"
        editable={true}
        selectable={true}
        events={events}
        select={(info) => createEvent(new Date(info.startStr))}
        height="100%"
        width="100%"
      />
    </div>
  );
}

export default Calendar;
