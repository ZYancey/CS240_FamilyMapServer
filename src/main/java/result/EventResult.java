package result;

import model.Event;

public class EventResult extends Result{
    private Event event;
    private Event[] events;

    public EventResult(Event event){
        setEvent(event);
    }
    public EventResult(Event[] events) {setEvents(events);}
    public EventResult(String eventErr){ setMessage(eventErr);}

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public Event getEvent() {
        return event;
    }

    public Event[] getEvents() {
        return events;
    }
}
