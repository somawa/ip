package duke;

/** Signals that a command does not follow the supported command format. */
public class CommandFormatException extends RuntimeException {
    /** Creates a command-format exception with a user-facing explanation. */
    public CommandFormatException(String message) {
        super(message);
    }
}
