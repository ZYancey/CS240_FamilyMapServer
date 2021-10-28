package result;

import model.Event;
import model.Person;

public class ListResult extends Result{
    private Person[] persons;
    private Event[] events;

    public ListResult(Person[] persons, Event[] events){
        setEvents(events);
        setPersons(persons);
    }
    public ListResult(String listErr){
        setMessage(listErr);
    }


    public void setEvents(Event[] events) {
        this.events = events;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Person[] getPersons() {
        return persons;
    }
}
