package learn.field_agent.data;

public class DataAccessException extends Exception{

    public DataAccessException(String message, Throwable inner){
        super(message, inner);
    }
}
