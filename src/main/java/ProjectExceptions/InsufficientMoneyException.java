package ProjectExceptions;

public class InsufficientMoneyException extends Exception {
    public InsufficientMoneyException(String message) {
        super(message);
    }

    public InsufficientMoneyException() {
    }
}
