package learn.field_agent.data;

public class RanOutOfSpaceDataException extends DataAccessException{
    public RanOutOfSpaceDataException(){
        super("Ran out of space while attempting operation.", null);
    }
}
