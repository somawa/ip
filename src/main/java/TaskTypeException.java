public class TaskTypeException extends RuntimeException{
    public TaskTypeException() {
        super("Please specify one of the message types todo, event or deadline");
    }
    public TaskTypeException(String message) {
        super(message);
    }
}
