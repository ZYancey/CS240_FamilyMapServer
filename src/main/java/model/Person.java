package model;

public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

public Person(String firstName,
              String lastName,
              String gender,
              String personID,
              String spouseID,
              String fatherID,
              String motherID,
              String associatedUsername) {
    this.personID = personID;
    this.associatedUsername = associatedUsername;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender   = gender;
    this.fatherID = fatherID;
    this.motherID = motherID;
    this.spouseID = spouseID;
}


    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }


    public String getUsername() {
        return associatedUsername;
    }
    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }
    public void setMotherID(String motherID) { this.motherID = motherID; }

    public String getSpouseID() {
        return spouseID;
    }
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof model.Person) {
            model.Person oPerson = (model.Person) obj;
            return  oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getUsername().equals(getUsername()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    oPerson.getFatherID().equals(getFatherID()) &&
                    oPerson.getMotherID().equals(getMotherID()) &&
                    oPerson.getSpouseID().equals(getSpouseID());
        } else {
            return false;
        }
    }
}
