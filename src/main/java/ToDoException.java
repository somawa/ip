public class ToDoException extends RuntimeException{
    public ToDoException() {
        super("Missing description of todo task");
    }
    public ToDoException(String message) {
        super(message);
    }
}
