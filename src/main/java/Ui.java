import java.util.List;

public class Ui {
    /**
     * Mode enum to differentiate between list and simple printing
     */
    public enum Mode {
        SIMPLE,
        LIST
    }
    public enum Command {
        TODO,
        DEADLINE,
        EVENT,
        MARK,
        UNMARK,
        LIST,
        DELETE,
        BYE;

        public static Command stringToCommand(String commandInput) {
            if (commandInput == null) return null;
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
    /**
     * Chains together a chat section using the input string array
     * Prints the chained section
     *
     * @param header header to add as the first line of the section
     * @param parts correspond to input string array in order of chaining
     * @param footer footer to add at the last line of the section
     * @param mode Mode enum for section formatting
     */
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
