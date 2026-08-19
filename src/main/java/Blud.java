import java.util.Scanner;
/**
 * Entry point for the Blud chatbot.
 */
public class Blud {
    /**
     * Mode enum to differentiate between list and simple printing
     */
    public enum Mode {
        SIMPLE,
        LIST
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
    public static void section(String header, String[] parts, String footer, Mode mode) {
        if (header != null) {
            System.out.println('\t' + header);
        }
        switch (mode) {
            case SIMPLE:
                System.out.println('\t' + String.join("\n\t", parts));
                break;
            case LIST:
                listPrint(parts, '\t');
                break;
            default:
                System.out.println("Invalid mode");
        }
        if (footer != null) {
            System.out.println('\t' + footer);
        }
    }

    /**
     * Prints out list items with ids, with prefix at the front
     *
     * @param list list of items to print out
     *
     * @param prefix prefix to append to start of each item
     */
    public static void listPrint(String[] list, char prefix) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] ==  null) {
                break;
            }
            System.out.println(prefix + Integer.toString(i + 1) + ". " + list[i]);
        }
    }
    /**
     * Starts Blud and displays its name, entry and exit greeting.
     *
     * @param args command-line arguments, which are currently unused
     */
    public static void main(String[] args) {
        // Scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        // AI-Generated String Banner
        String banner = " ____  _            _\n"
                + "| __ )| |_   _  ___| |\n"
                + "|  _ \\| | | | |/ __| |\n"
                + "| |_) | | |_| | (__|_|\n"
                + "|____/|_|\\__,_|\\___(_)\n";
        String greeting = "Hey! This is Blud, what can I do for you today?";
        String departure = "Thanks for the conversation, see you soon!";
        String breakLine = "-------------------------------";
        String userInput = "";
        String listCommand = "list";
        String exitCommand = "bye";
        String[] list = new String[100];
        int nextListId = 0;

        section(breakLine, new String[]{banner}, null, Mode.SIMPLE);
        section(null, new String[]{greeting}, null, Mode.SIMPLE);
        userInput = scanner.nextLine();
        while (!exitCommand.equals(userInput)) {
            if (listCommand.equals(userInput)) {
                section(null, list, breakLine, Mode.LIST);
            } else {
                list[nextListId++] = userInput;
                section(null, new String[]{("added: " + userInput)}, breakLine, Mode.SIMPLE);
            }
            userInput = scanner.nextLine();
        }
        section(null, new String[]{departure}, breakLine, Mode.SIMPLE);
    }
}
