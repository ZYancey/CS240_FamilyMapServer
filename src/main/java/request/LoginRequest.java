package request;

/**This class defines the attributes for a LoginRequest object.*/
public class LoginRequest {
    /**The general constructor for a LoginRequest.
     * @param userName	the userName for the request.
     * @param password	the password for the request.*/
    public LoginRequest(String userName, String password) {
        setUsername(userName);
        setPassword(password);
    }

    /**The userName of the person making the request.*/
    private String userName;

    /**The password of the person making the request.*/
    private String password;



    /**Sets the userName for the request.
     * @param userName 	the desired userName for the request.*/
    public void setUsername(String userName) { this.userName = userName; }

    /**Sets the password for the request.
     * @param password	the desired password for the request.*/
    public void setPassword(String password) { this.password = password; }

    /**@return 			the userName that is attached to the request.*/
    public String getUsername() { return userName; }

    /**@return 			the password that is attached to the request.*/
    public String getPassword() { return password; }
}