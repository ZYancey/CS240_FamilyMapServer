package model;

public class AuthToken{
    private String authtoken;
    private String username;
    private String personID;
    private Boolean success;

    public AuthToken(String token, String username, String personID) {
        this.authtoken = token;
        this.username = username;
        this.personID = personID;
        success = true;
    }

    public void setAuthTokenID(String authToken) { this.authtoken = authToken; }
    public void setUsername(String userName) { this.username = userName; }
    public void setPersonID(String personID) { this.personID = personID; }
    public void setSuccess(Boolean success) { this.success = success; }

    public String getAuthToken() { return authtoken; }
    public String getUserName() { return username; }
    public String getPersonID() { return personID; }
    public Boolean getSuccess(){ return success; }

}
