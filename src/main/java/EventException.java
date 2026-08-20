public class EventException extends RuntimeException{
    public EventException() {
        super("Missing description of event task");
    }
    public EventException(String message) {
        super(message);
    }
}
