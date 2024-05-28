package space.neptuxo.exception;

public class SQLConstraintException extends RuntimeException {

    public SQLConstraintException(Exception e) {
        super(e);
    }


}
