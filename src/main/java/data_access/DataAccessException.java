package data_access;

public class DataAccessException extends Exception {
    DataAccessException(String message)
    {
        super(message);
    }

    DataAccessException()
    {
        super();
    }
}
