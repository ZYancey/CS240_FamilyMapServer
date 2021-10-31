package result;

import model.Event;
import model.Person;

public class ListResult extends Result {
    private Person[] personList;
    private Event[] eventList;

    public ListResult(Person[] personList, Event[] eventList) {
        setPersonList(personList);
        setEventList(eventList);
    }

    public void setPersonList(Person[] personList) { this.personList = personList; }
    public void setEventList(Event[] eventList) { this.eventList = eventList; }

    public Person[] getPersonList() { return personList; }
    public Event[] getEventList() { return eventList; }
}