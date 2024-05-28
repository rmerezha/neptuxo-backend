package space.neptuxo.exception;

public class ActiveOrderExistsException extends RuntimeException {

    public ActiveOrderExistsException(Exception e) {
        super(e);
    }

    public ActiveOrderExistsException() {
    }

}
