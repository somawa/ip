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

    private static Task delete(List<Task> taskList, String[] splitInput) {
        Task removedTask;
        if (splitInput.length < 2) {
            throw new DeletionException();
        } else {
            try {
                int seq = Integer.parseInt(splitInput[1]);
                removedTask = taskList.get(seq - 1);
                taskList.remove(seq - 1);

            } catch (NumberFormatException e) {
                throw new DeletionException("Require an integer number to delete");
            } catch (IndexOutOfBoundsException e) {
                if (!taskList.isEmpty()) {
                    throw new DeletionException(String.format("Require a valid integer from %s to %s to delete from", 1, taskList.size()));
                } else {
                    throw new DeletionException("task list is empty, nothing to delete");
                }
            }
        }
        return removedTask;
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
        String taskListPreface = "Here are the tasks in your list:";
        String userInput = "";
        String todoType = "todo";
        String deadlineType = "deadline";
        String eventType = "event";
        String listCommand = "list";
        String markCommand = "mark";
        String unmarkCommand = "unmark";
        String deleteCommand = "delete";
        String exitCommand = "bye";
        List<Task> taskList = new ArrayList<>();

        List<String> startupList = new ArrayList<>(Arrays.asList(banner.split("\n")));
        startupList.add(greeting);

        sectionString(breakLine, startupList, null, Mode.SIMPLE);
        userInput = scanner.nextLine();
        sectionString(null, List.of(), breakLine, Mode.SIMPLE);
        while (!exitCommand.equals(userInput)) {
            if (listCommand.equals(userInput)) {
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
                } else if (deleteCommand.equals(splitInput[0])) {
                    try {
                        Task deletedTask = delete(taskList, splitInput);
                        sectionString(
                                null,
                                Arrays.asList(
                                        "Task removed successfully:",
                                        deletedTask.toString()
                                ),
                                breakLine,
                                Mode.SIMPLE
                        );
                    } catch (DeletionException e) {
                        sectionString(null, Arrays.asList(e.getMessage()), breakLine, Mode.SIMPLE);
                    }
                } else {
                    Task newTask;
                    String[] parts = userInput.split(" /");
                    String taskType = parts[0].split(" ")[0];
                    try {
                        if (todoType.equals(taskType)) {
                            newTask = new ToDo(parts);
                        } else if (deadlineType.equals(taskType)) {
                            newTask = new Deadline(parts);
                        } else if (eventType.equals(taskType)) {
                            newTask = new Event(parts);
                        } else {
                            throw new TaskTypeException(
                                    String.format(
                                            "invalid task type %s, please use one of todo, event or deadline task types",
                                            taskType));
                        }
                        taskList.add(newTask);
                        sectionString(
                                null,
                                Arrays.asList(
                                        String.format(
                                                "added: %s",
                                                newTask),
                                        String.format(
                                                "Now you have %d tasks in the list",
                                                taskList.size()
                                        )
                                ),
                                breakLine,
                                Mode.SIMPLE
                        );
                    } catch (RuntimeException e) {
                        sectionString(null, Arrays.asList(e.getMessage()), breakLine, Mode.SIMPLE);
                    }
                }
            }
            userInput = scanner.nextLine();
        }
        sectionString(null, Arrays.asList(departure), breakLine, Mode.SIMPLE);
    }
}
