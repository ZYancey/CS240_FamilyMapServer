package result;

import model.Event;

public class EventResult extends Result{
    private Event event;
    private Event[] data;

    public EventResult(Event e) {
        setEvent(e);
    }
    public EventResult(Event[] d) {
        setData(d);
    }
    public EventResult(String error) {
        setMessage(error);
    }


    public void setEvent(Event event) { this.event = event; }
    public Event getEvent() { return event; }

    public void setData(Event[] data) { this.data = data; }
    public Event[] getData() { return data; }
}