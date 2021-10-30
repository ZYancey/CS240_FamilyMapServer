package result;

import model.Person;

/**The class definition for the attributes of a PersonResult.
 * <p> Extends the Result class.*/
public class PersonResult extends Result{
    /**Constructor for returning one Person.
     * @param p			the person to be returned*/
    public PersonResult(Person p) {
        setPerson(p);
    }

    /**Constructor for returning an array of Persons.
     * @param d			the Person array to be returned*/
    public PersonResult(Person[] d) {
        setData(d);
    }

    /**Constructor for an error PersonResult.
     * @param error		the error message*/
    public PersonResult(String error) {
        setMessage(error);
    }

    /**The Person to be returned.*/
    private Person person;

    /**The array of Persons to be returned.*/
    private Person[] data;



    /**Sets the Person to be returned.
     * @param person	the Person to be returned*/
    public void setPerson(Person person) { this.person = person; }

    /**Sets the data to be returned.
     * @param data		the Person array to be returned*/
    public void setData(Person[] data) { this.data = data; }

    /**@return the Person to return*/
    public Person getPerson() { return person; }

    /**@return the Person array data*/
    public Person[] getData() { return data; }
}