package request;

public class EventRequest {
    public EventRequest(String auth, String event) {
        setAuthTokenID(auth);
        setEventID(event);
    }

    private String AuthTokenID;
    private String eventID;

    public void setAuthTokenID(String authTokenID) { AuthTokenID = authTokenID; }
    public void setEventID(String eventID) { this.eventID = eventID; }

    public String getAuthTokenID() { return AuthTokenID; }
    public String getEventID() { return eventID; }
}