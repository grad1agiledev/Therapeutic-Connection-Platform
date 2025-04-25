import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction'; // for drag/drop
import "./Calendar.css";

function Calendar({ userId }) {
  return (
    <div className="calendar-container">
    <FullCalendar
      plugins={[dayGridPlugin, interactionPlugin]}
      initialView="dayGridMonth"
      editable={true}
      selectable={true}
      events={`/api/users/${userId}/events`} // fetch user-specific events

      height='100%'
      width='100%'
    />
    </div>
  );
};

export default Calendar;
