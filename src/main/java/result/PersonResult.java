package result;

import model.Person;

public class PersonResult extends Result{
    private Person person;
    private Person[] data;

    public PersonResult(Person person) {
        setPerson(person);
    }
    public PersonResult(Person[] data) {
        setData(data);
    }
    public PersonResult(String error) {
        setMessage(error);
    }


    public void setPerson(Person person) { this.person = person; }
    public Person getPerson() { return person; }

    public void setData(Person[] data) { this.data = data; }
    public Person[] getData() { return data; }
}