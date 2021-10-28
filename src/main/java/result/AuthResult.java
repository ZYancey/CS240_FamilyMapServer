package result;

import model.AuthToken;

public class AuthResult extends Result{
    private AuthToken authToken;

    public  AuthResult(AuthToken auth){
        setAuthToken(auth);
    }

    public AuthResult(String authErr){
        setMessage(authErr);
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
