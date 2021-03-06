package result;

import model.AuthToken;

public class AuthResult extends Result{
    private AuthToken authToken;

    public AuthResult(AuthToken authToken) {
    setAuthToken(authToken);
}
    public AuthResult(String error) {
        setMessage(error);
    }


    public void setAuthToken(AuthToken authToken) { this.authToken = authToken; }
    public AuthToken getAuthToken() { return authToken; }
}
