import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
/**
 * Entry point for the Blud chatbot.
 */
public class Blud {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    public Blud(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.taskList = new TaskList(this.storage.loadTasks());
    }

    public void displayNewTask(Task newTask, String header, String footer) {
        this.ui.sectionString(
                header,
                Arrays.asList(
                        String.format(
                                "added: %s",
                                newTask),
                        String.format(
                                "Now you have %d tasks in the list",
                                this.taskList.getSize()
                        )
                ),
                footer,
                Ui.Mode.SIMPLE
        );
    }

    /**
     * Starts Blud and displays its name, entry and exit greeting.
     *
     */
    public void run() {
        List<Task> tasks = this.storage.loadTasks();
        TaskList taskList = new TaskList(tasks);
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
//        List<Task> taskList = new ArrayList<>();
        Task newTask;

        List<String> startupList = new ArrayList<>(Arrays.asList(banner.split("\n")));
        startupList.add(greeting);

        this.ui.sectionString(breakLine, startupList, null, Ui.Mode.SIMPLE);
        userInput = scanner.nextLine();
        this.ui.sectionString(null, List.of(), breakLine, Ui.Mode.SIMPLE);
        while (!exitCommand.equals(userInput)) {
            String[] splitInput = userInput.split(" ");
            String[] parts = userInput.split(" /");
            String taskType = splitInput[0];
            //if (listCommand.equals(userInput)) {
            try {
                Ui.Command inputCommand = Ui.Command.stringToCommand(taskType);
                switch (inputCommand) {
                    case LIST:
                        this.ui.sectionTask(taskListPreface, taskList, breakLine);
                        break;
                    case MARK:
                        //} else {
    //                String[] splitInput = userInput.split(" ");
                        //if ((markCommand.equals(splitInput[0]) || unmarkCommand.equals(splitInput[0]))
                        //&& splitInput.length == 2) {
                        int idMark = Integer.parseInt(splitInput[1]) - 1;
                        String responseMark = "";
                        //if (markCommand.equals(splitInput[0])) {
                        taskList.markTask(idMark);
                        this.storage.saveTasks(taskList);
                        responseMark = "Nice! I've marked this task as done:";
                        this.ui.sectionString(null, Arrays.asList(responseMark, taskList.getTask(idMark).toString()), breakLine, Ui.Mode.SIMPLE);
                        break;
                        //} else {
                    case UNMARK:
                        int idUnmark = Integer.parseInt(splitInput[1]) - 1;
                        String responseUnmark = "";
                        taskList.getTask(idUnmark).unmark();
                        this.storage.saveTasks(taskList);
                        responseUnmark = "OK, I've marked this task as not done yet:";
                        this.ui.sectionString(null, Arrays.asList(responseUnmark, taskList.getTask(idUnmark).toString()), breakLine, Ui.Mode.SIMPLE);
                        break;
                        //} else if (deleteCommand.equals(splitInput[0])) {
                    case DELETE:
                        try {
                            Task deletedTask = taskList.delete(splitInput);
                            this.storage.saveTasks(taskList);
                            this.ui.sectionString(
                                    null,
                                    Arrays.asList(
                                            "Task removed successfully:",
                                            deletedTask.toString()
                                    ),
                                    breakLine,
                                    Ui.Mode.SIMPLE
                            );
                        } catch (DeletionException e) {
                            this.ui.sectionString(null, Arrays.asList(e.getMessage()), breakLine, Ui.Mode.SIMPLE);
                        }
                        break;
                        //} else {
    //                    Task newTask;
    //                    String[] parts = userInput.split(" /");
    //                    String taskType = parts[0].split(" ")[0];
                    case TODO:
                        System.out.println("HERERERERE");
    //                    try {
    //                        if (todoType.equals(taskType)) {
                        newTask = new ToDo(parts);
                        taskList.addTask(newTask, null, breakLine);
                        this.displayNewTask(newTask, null, breakLine);
                        this.storage.saveTasks(taskList);
                        break;
    //                        } else if (deadlineType.equals(taskType)) {
                    case DEADLINE:
                        newTask = new Deadline(parts);
                        taskList.addTask(newTask, null, breakLine);
                        this.displayNewTask(newTask, null, breakLine);
                        this.storage.saveTasks(taskList);
                        break;
    //                        } else if (eventType.equals(taskType)) {
                    case EVENT:
                        newTask = new Event(parts);
                        taskList.addTask( newTask, null, breakLine);
                        this.displayNewTask(newTask, null, breakLine);
                        this.storage.saveTasks(taskList);
                        break;
    //                        } else {
                    default:
                        throw new TaskTypeException(
                                String.format(
                                        "invalid task type %s, please use one of todo, event or deadline task types",
                                        taskType));
    //                        }
                }

            } catch (RuntimeException e) {
                this.ui.sectionString(null, Arrays.asList(e.getMessage()), breakLine, Ui.Mode.SIMPLE);
            }
            userInput = scanner.nextLine();
        }
        this.ui.sectionString(null, Arrays.asList(departure), breakLine, Ui.Mode.SIMPLE);
    }

    public static void main(String[] args) {
        new Blud("data/blud.txt").run();
    }
}
