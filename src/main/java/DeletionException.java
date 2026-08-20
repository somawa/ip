public class DeletionException extends RuntimeException{
    public DeletionException() {
        super("Require an integer number to delete");
    }
    public DeletionException(String message) {
        super(message);
    }
}
