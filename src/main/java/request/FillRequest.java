package request;

public class FillRequest {
    public FillRequest(String userName, int generations) {
        setUsername(userName);
        setGenerations(generations);
    }

    private String userName;
    private int generations;
    public void setUsername(String userName) { this.userName = userName; }
    public void setGenerations(int generations) { this.generations = generations; }
    public String getUsername() { return userName; }
    public int getGenerations() { return generations; }
}