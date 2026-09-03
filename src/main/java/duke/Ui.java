package duke;

import java.util.List;

/** Formats user-interface sections for the console application. */
public class Ui {
    /** Selects the layout used to print a section. */
    public enum Mode {
        SIMPLE,
        LIST
    }

    /** Represents the commands accepted by Blud. */
    public enum Command {
        TODO,
        DEADLINE,
        EVENT,
        MARK,
        UNMARK,
        LIST,
        DELETE,
        BYE;

        /** Converts a user-entered command name to its command enum value. */
        public static Command stringToCommand(String commandInput) {
            if (commandInput == null) {
                return null;
            }
            try {
                // Trim whitespace and convert to uppercase to match enum style
                return Command.valueOf(commandInput.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new TaskTypeException(
                        String.format("invalid task type %s, please use one of todo, event or deadline task types",
                                commandInput));
            }
        }

    }
    /** Prints a formatted section made from a header, body parts, and footer. */
    public void sectionString(String header, List<String> parts, String footer, Mode mode) {
        if (header != null) {
            System.out.println('\t' + header);
        }
        switch (mode) {
            case SIMPLE:
                System.out.println('\t' + String.join("\n\t", parts));
                break;
            default:
                System.out.println("Invalid mode");
        }
        if (footer != null) {
            System.out.println('\t' + footer);
        }
    }

    /** Prints a formatted section containing the tasks in the supplied list. */
    public void sectionTask(String header, TaskList taskList, String footer) {
        if (header != null) {
            System.out.println('\t' + header);
        }
        for (int i = 0; i < taskList.getSize(); i++) {
            System.out.print('\t' + Integer.toString(i + 1) + ". ");
            System.out.println(taskList.getTask(i));
        }
        if (footer != null) {
            System.out.println('\t' + footer);
        }
    }
}
