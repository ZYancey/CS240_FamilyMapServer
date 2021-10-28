package result;

import model.Person;

public class PersonResult extends Result{
    private Person person;
    private Person[] persons;

    public PersonResult(Person person){
        setPerson(person);
    }
    public PersonResult(Person[] persons) {setPersons(persons);}
    public PersonResult(String personErr){ setMessage(personErr);}

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Person getPerson() {
        return person;
    }

    public Person[] getPersons() {
        return persons;
    }
}
