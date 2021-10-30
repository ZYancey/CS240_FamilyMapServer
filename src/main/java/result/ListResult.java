package result;

import model.Event;
import model.Person;

public class ListResult extends Result {
    /**Constructor for returning an array of Persons and an array of Events.
     * @param p			the Person array to be returned
     * @param e			the Event array to be returned*/
    public ListResult(Person[] p, Event[] e) {
        setPersonList(p);
        setEventList(e);
    }

    /**Constructor for an error PersonResult.
     * @param error		the error message*/
    public ListResult(String error) {
        setMessage(error);
    }

    /**The array of Persons to be returned.*/
    private Person[] personList;

    /**The array of Events to be returned.*/
    private Event[] eventList;



    /**Sets the array of Persons to be returned.
     * @param pList		the Person array to be returned*/
    public void setPersonList(Person[] pList) { this.personList = pList; }

    /**Sets the array of Events to be returned.
     * @param eList		the Event array to be returned*/
    public void setEventList(Event[] eList) { this.eventList = eList; }

    /**@return the Person array data*/
    public Person[] getPersonList() { return personList; }

    /**@return the Event array data*/
    public Event[] getEventList() { return eventList; }
}