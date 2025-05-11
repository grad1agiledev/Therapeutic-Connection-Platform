import React, { useState, useEffect } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { useAuth } from '../../ProfileManagement/UserContext';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';

const modalStyle = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: '#FFFDE7',
  border: '2px solid #FFD54F',
  boxShadow: 24,
  borderRadius: 3,
  p: 4,
};

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

  const handleSubmit = (e) => {
    e.preventDefault();
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
    fetch('http://localhost:8080/api/meetings/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(eventData),
    })
      .then(res => res.json())
      .then(savedEvent => {
        fetchMeetings(currentUser.uid);
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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
      participants: name === 'participants' ? value.split(',').map(email => email.trim()) : prev.participants,
    }));
  };

  const createEvent = (date) => {
    const formattedDate = date.toISOString().split('T')[0];
    setFormData(prev => ({
      ...prev,
      date: formattedDate,
    }));
    setShowModal(true);
  };

  return (
    <Box sx={{ maxWidth: 1100, mx: 'auto', mt: 4, p: 2, bgcolor: '#FFF8E1', borderRadius: 3, boxShadow: 2 }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h4" color="#5D4037" fontWeight={700}>Calendar</Typography>
        <Button variant="contained" sx={{ bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 600, '&:hover': { bgcolor: '#FFECB3' } }} onClick={() => createEvent(new Date())}>
          Create New Event
        </Button>
      </Stack>
      <Modal open={showModal} onClose={() => setShowModal(false)}>
        <Box sx={modalStyle}>
          <Typography variant="h6" color="#5D4037" fontWeight={700} mb={2}>Create Event</Typography>
          <Box component="form" onSubmit={handleSubmit}>
            <TextField
              label="Title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              fullWidth
              required
              sx={{ mb: 2 }}
            />
            <TextField
              label="Date"
              type="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              fullWidth
              required
              InputLabelProps={{ shrink: true }}
              sx={{ mb: 2 }}
            />
            <Stack direction="row" spacing={2} sx={{ mb: 2 }}>
              <TextField
                label="Start Time"
                type="time"
                name="startTime"
                value={formData.startTime}
                onChange={handleChange}
                required
                InputLabelProps={{ shrink: true }}
                fullWidth
              />
              <TextField
                label="End Time"
                type="time"
                name="endTime"
                value={formData.endTime}
                onChange={handleChange}
                required
                InputLabelProps={{ shrink: true }}
                fullWidth
              />
            </Stack>
            <TextField
              label="Participants"
              name="participants"
              value={formData.participants}
              onChange={handleChange}
              fullWidth
              placeholder="Enter participant emails, separated by commas"
              sx={{ mb: 2 }}
            />
            <Stack direction="row" spacing={2} justifyContent="flex-end">
              <Button type="submit" variant="contained" sx={{ bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 600 }}>
                Save
              </Button>
              <Button type="button" variant="outlined" color="error" onClick={() => setShowModal(false)}>
                Cancel
              </Button>
            </Stack>
          </Box>
        </Box>
      </Modal>
      <Box sx={{ mt: 4 }}>
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
          height="auto"
        />
      </Box>
    </Box>
  );
}

export default Calendar;
