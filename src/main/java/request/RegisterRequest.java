package request;

public class RegisterRequest {
    private final String username;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private String gender;
    private final String personID;


    public RegisterRequest(String username,
                           String password,
                           String email,
                           String firstName,
                           String lastName,
                           String gender,
                           String personID){
        this.username = username;
        this.password = password;
        this.email    = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender   = gender;
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getGender() {
        return gender;
    }
    public String getPersonID() {
        return personID;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

}
