package request;

/**This class defines the attributes for a LoginRequest object.*/
public class LoginRequest {
    public LoginRequest(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /**The userName of the person making the request.*/
    private String username;

    /**The password of the person making the request.*/
    private String password;


    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    /**@return 			the userName that is attached to the request.*/
    public String getUsername() { return username; }

    /**@return 			the password that is attached to the request.*/
    public String getPassword() { return password; }
}