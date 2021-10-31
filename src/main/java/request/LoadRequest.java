package request;

import model.Event;
import model.Person;
import model.User;

public class LoadRequest {
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        setUserList(users);
        setPersonList(persons);
        setEventList(events);
    }

    private User[] users;
    private Person[] persons;
    private Event[] events;


    public void setUserList(User[] users) { this.users = users; }
    public void setPersonList(Person[] persons) { this.persons = persons; }
    public void setEventList(Event[] events) { this.events = events; }

    public User[] getUserList() { return users; }
    public Person[] getPersonList() { return persons; }
    public Event[] getEventList() { return events; }
}