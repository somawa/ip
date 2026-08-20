public class DeadlineException extends RuntimeException{
    public DeadlineException() {
        super("Missing description of deadline task");
    }
    public DeadlineException(String message) {
        super(message);
    }
}
