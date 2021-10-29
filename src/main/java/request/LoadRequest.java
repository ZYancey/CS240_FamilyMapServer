package request;

import model.Event;
import model.Person;
import model.User;

/**This class defines the attributes for a LoadRequest.*/
public class LoadRequest {
    /**The general constructor for a LoadRequest object.
     * @param users		the list of Users to be created.
     * @param persons	the list of Persons to be created.
     * @param events	the list of Events to be created.*/
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        setUserList(users);
        setPersonList(persons);
        setEventList(events);
    }

    /**The list of Users to be created.*/
    private User[] users;

    /**The list of Persons to be created.*/
    private Person[] persons;

    /**The list of Events to be created.*/
    private Event[] events;



    /**Sets the list of Users to be created.
     * @param users		the list of Users.*/
    public void setUserList(User[] users) { this.users = users; }

    /**Sets the list of Persons to be created.
     * @param persons	the list of Persons.*/
    public void setPersonList(Person[] persons) { this.persons = persons; }

    /**Sets the list of Events to be created.
     * @param events	the list of Events.*/
    public void setEventList(Event[] events) { this.events = events; }

    /**@return			an array of Users.*/
    public User[] getUserList() { return users; }

    /**@return			an array of Persons.*/
    public Person[] getPersonList() { return persons; }

    /**@return			an array of Events.*/
    public Event[] getEventList() { return events; }
}