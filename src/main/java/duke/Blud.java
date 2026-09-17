package duke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


/**
 * The Blud chatbot and its JavaFX user interface.
 *
 * <p>The command-processing method is shared by the graphical interface and
 * can also be used by other interfaces without depending on JavaFX controls.</p>
 */
public class Blud {
    private static final String DEFAULT_FILE_PATH = "data/blud.txt";
    private static final String BREAK_LINE = "-------------------------------";
    private static final String TASK_LIST_PREFACE = "Here are the tasks in your list:";
    private static final String DEPARTURE = "Thanks for the conversation, see you soon!";

    private final Storage storage;
    private final TaskList taskList;

    /** Creates a Blud instance using the default storage file. */
    public Blud() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates a Blud instance using the specified task storage file.
     * @param filePath path to the task storage file.
     */
    public Blud(String filePath) {
        this.storage = new Storage(filePath);
        this.taskList = new TaskList(this.storage.loadTasks());
    }

    /**
     * Processes one chatbot command and returns the response for display.
     * @param userInput command entered by the user.
     * @return a human-readable response without UI-specific formatting.
     */
    public String processCommand(String userInput) {
        return processCommandWithStatus(userInput).response();
    }

    /** Processes a command while retaining whether the response represents an error. */
    public CommandResult processCommandWithStatus(String userInput) {
        try {
            CommandValidator.validate(userInput);
            if ("bye".equals(userInput)) {
                return new CommandResult(DEPARTURE, false);
            }

            String[] splitInput = userInput.split(" ");
            Ui.Command inputCommand = Ui.Command.stringToCommand(splitInput[0]);
            CommandValidator.validateCommand(inputCommand, userInput);
            String[] parts = CommandValidator.splitTaskParts(userInput);
            switch (inputCommand) {
                case LIST:
                    return new CommandResult(formatTaskList(), false);
                case FIND:
                    return new CommandResult(new Find(userInput).execute(taskList), false);
                case MARK:
                    int idMark = CommandValidator.parseTaskIndex(splitInput[1]);
                    taskList.markTask(idMark);
                    storage.saveTasks(taskList);
                    String markResponse = "Nice! I've marked this task as done:\n" + taskList.getTask(idMark);
                    return new CommandResult(markResponse, false);
                case UNMARK:
                    int idUnmark = CommandValidator.parseTaskIndex(splitInput[1]);
                    taskList.getTaskForCommandIndex(idUnmark).unmark();
                    storage.saveTasks(taskList);
                    String unmarkResponse = "OK, I've marked this task as not done yet:\n"
                            + taskList.getTask(idUnmark);
                    return new CommandResult(unmarkResponse, false);
                case DELETE:
                    Task deletedTask = taskList.delete(splitInput);
                    storage.saveTasks(taskList);
                    return new CommandResult("Task removed successfully:\n" + deletedTask, false);
                case SORT:
                    return new CommandResult(sortTasks(splitInput), false);
                case TODO:
                    return new CommandResult(addTask(new ToDo(parts)), false);
                case DEADLINE:
                    return new CommandResult(addTask(new Deadline(parts)), false);
                case EVENT:
                    return new CommandResult(addTask(new Event(parts)), false);
                default:
                    throw new TaskTypeException("invalid task type " + splitInput[0]
                            + ", please use one of todo, event or deadline task types");
            }
        } catch (RuntimeException e) {
            String message = e.getMessage() == null ? "Unable to process command." : e.getMessage();
            return new CommandResult(message, true);
        }
    }

    /** Represents a chatbot response and whether it should be visually highlighted as an error. */
    public record CommandResult(String response, boolean isError) {
    }

    /** Adds a task, persists the updated list, and creates its response. */
    private String addTask(Task newTask) {
        assert newTask != null : "Command parsing must produce a valid task";
        taskList.addTask(newTask);
        storage.saveTasks(taskList);
        return "added: " + newTask + "\nNow you have " + taskList.getSize() + " tasks in the list";
    }

    /** Sorts the current task list according to a user-specified criterion. */
    private String sortTasks(String[] splitInput) {
        if (splitInput.length < 2) {
            throw new CommandFormatException(
                    "Please specify a sort criterion: deadline, event, or status.");
        }
        if (splitInput.length > 3) {
            throw new CommandFormatException(
                    "Invalid sort command. Usage: sort <deadline|event|status> [asc|desc].");
        }

        TaskList.SortCriterion criterion = parseSortCriterion(splitInput[1]);
        TaskList.SortDirection direction = splitInput.length == 3
                ? parseSortDirection(splitInput[2])
                : TaskList.SortDirection.ASCENDING;
        if (taskList.getSize() == 0) {
            return "There are no tasks to sort.";
        }

        taskList.sort(criterion, direction);
        return String.format(
                "Tasks sorted by %s in %s order.",
                getSortCriterionDisplayName(criterion),
                direction == TaskList.SortDirection.ASCENDING ? "ascending" : "descending");
    }

    /** Parses a sort criterion and returns its task-list representation. */
    private TaskList.SortCriterion parseSortCriterion(String criterion) {
        return switch (criterion.toLowerCase(Locale.ROOT)) {
            case "deadline" -> TaskList.SortCriterion.DEADLINE;
            case "event" -> TaskList.SortCriterion.EVENT;
            case "status" -> TaskList.SortCriterion.STATUS;
            default -> throw new CommandFormatException(
                    "Invalid sort criterion. Please use deadline, event, or status.");
        };
    }

    /** Parses a sort direction and returns its task-list representation. */
    private TaskList.SortDirection parseSortDirection(String direction) {
        return switch (direction.toLowerCase(Locale.ROOT)) {
            case "asc" -> TaskList.SortDirection.ASCENDING;
            case "desc" -> TaskList.SortDirection.DESCENDING;
            default -> throw new CommandFormatException(
                    "Invalid sort direction. Please use asc or desc.");
        };
    }

    /** Returns the user-facing name for a sort criterion. */
    private String getSortCriterionDisplayName(TaskList.SortCriterion criterion) {
        return switch (criterion) {
            case DEADLINE -> "deadline";
            case EVENT -> "event start date";
            case STATUS -> "completion status";
        };
    }

    /** Formats all currently stored tasks for the chat transcript. */
    private String formatTaskList() {
        StringBuilder response = new StringBuilder(TASK_LIST_PREFACE);
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            assert task != null : "Every task in the list must be displayable";
            response.append("\n").append(i + 1).append(". ").append(task);
        }
        return response.toString();
    }

    /** Retains the original command-line interface for direct invocation. */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String banner = " ____  _            _\n"
                + "| __ )| |_   _  ___| |\n"
                + "|  _ \\| | | | |/ __| |\n"
                + "| |_) | | |_| | (__|_|\n"
                + "|____/|_|\\__,_|\\___(_)\n";
        String greeting = "Hey! This is Blud, what can I do for you today?";
        List<String> startupList = new ArrayList<>(Arrays.asList(banner.split("\n")));
        startupList.add(greeting);
        Ui ui = new Ui();
        ui.sectionString(BREAK_LINE, startupList, null, Ui.Mode.SIMPLE);
        String userInput = scanner.nextLine();
        ui.sectionString(null, List.of(), BREAK_LINE, Ui.Mode.SIMPLE);
        while (!"bye".equals(userInput)) {
            String response = processCommand(userInput);
            ui.sectionString(null, Arrays.asList(response.split("\n")), BREAK_LINE, Ui.Mode.SIMPLE);
            userInput = scanner.nextLine();
        }
        ui.sectionString(null, List.of(DEPARTURE), BREAK_LINE, Ui.Mode.SIMPLE);
    }

    /** Console-compatible entry point retained for direct invocation. */
    public static void main(String[] args) {
        new Blud().run();
    }
}
