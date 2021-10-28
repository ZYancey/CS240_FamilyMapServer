package result;

public class Result {
    public Result(){}

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Result(String message){
        setMessage(message);
    }

}
