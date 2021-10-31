package request;

public class LoginRequest {
    public LoginRequest(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    private String username;
    private String password;

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}