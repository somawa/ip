import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public static void sectionString(String header, List<String> parts, String footer, Mode mode) {
        if (header != null) {
            System.out.println('\t' + header);
        }
        switch (mode) {
            case SIMPLE:
                System.out.println('\t' + String.join("\n\t", parts));
                break;
//            case LIST:
//                listPrint(parts, '\t');
//                break;
            default:
                System.out.println("Invalid mode");
        }
        if (footer != null) {
            System.out.println('\t' + footer);
        }
    }

    public static void sectionTask(String header, List<Task> tasks, String footer) {
        if (header != null) {
            System.out.println('\t' + header);
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.print('\t' + Integer.toString(i + 1) + ". ");
            System.out.println(tasks.get(i));
        }
        if (footer != null) {
            System.out.println('\t' + footer);
        }
    }

//    /**
//     * Prints out list items with ids, with prefix at the front
//     *
//     * @param list list of items to print out
//     *
//     * @param prefix prefix to append to start of each item
//     */
//    public static void listPrint(List<String> list, char prefix) {
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(prefix + Integer.toString(i + 1) + ". " + list.get(i));
//        }
//    }
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
        String taskListPreface = "Here are the tasks in your list:";
        String userInput = "";
        String listCommand = "list";
        String markCommand = "mark";
        String unmarkCommand = "unmark";
        String exitCommand = "bye";
//        List<String> list = new ArrayList<>();
        List<Task> taskList = new ArrayList<>();

        List<String> startupList = new ArrayList<>(Arrays.asList(banner.split("\n")));
        startupList.add(greeting);

        sectionString(breakLine, startupList, null, Mode.SIMPLE);
        userInput = scanner.nextLine();
        while (!exitCommand.equals(userInput)) {
            if (listCommand.equals(userInput)) {
//                sectionString(null, list, breakLine, Mode.LIST);
                sectionTask(taskListPreface, taskList, breakLine);
            } else {
                String[] splitInput = userInput.split(" ");
                if ((markCommand.equals(splitInput[0]) || unmarkCommand.equals(splitInput[0]))
                        && splitInput.length == 2) {
                    int id = Integer.parseInt(splitInput[1]) - 1;
                    String response = "";
                    if (markCommand.equals(splitInput[0])) {
                        taskList.get(id).mark();
                        response = "Nice! I've marked this task as done:";
                    } else {
                        taskList.get(id).unmark();
                        response = "OK, I've marked this task as not done yet:";
                    }
                    sectionString(null, Arrays.asList(response, taskList.get(id).toString()), breakLine, Mode.SIMPLE);
                } else {
//                    list.add(userInput);
                    taskList.add(new Task(userInput));
                    sectionString(null, Arrays.asList(("added: " + userInput)), breakLine, Mode.SIMPLE);
                }
            }
            userInput = scanner.nextLine();
        }
        sectionString(null, Arrays.asList(departure), breakLine, Mode.SIMPLE);
    }
}
