package result;

public class Result {
    private String message;

    public Result() {
    }

    public Result(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
