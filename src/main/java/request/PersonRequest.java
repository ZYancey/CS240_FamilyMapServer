package request;

public class PersonRequest {
    public PersonRequest(String auth, String person) {
        setAuthTokenID(auth);
        setPersonID(person);
    }

    private String AuthTokenID;
    private String personID;

    public void setAuthTokenID(String authTokenID) { AuthTokenID = authTokenID; }
    public void setPersonID(String personID) { this.personID = personID; }

    public String getAuthTokenID() { return AuthTokenID; }
    public String getPersonID() { return personID; }
}