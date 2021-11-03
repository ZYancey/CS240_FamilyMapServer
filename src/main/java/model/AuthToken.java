package model;

public class AuthToken{

    public AuthToken(String authtoken, String userName, String personID) {
        setAuthToken(authtoken);
        setUserName(userName);
        setPersonID(personID);
        Boolean success = true;
    }



    private String authtoken;
    private String username;
    private String personID;

    public void setAuthToken(String authtoken) { this.authtoken = authtoken; }
    public void setUserName(String username) { this.username = username; }
    public void setPersonID(String personID) { this.personID = personID;}

    public String getAuthTokenID() { return authtoken; }
    public String getUserName() { return username; }
    public String getPersonID() { return personID; }


}
