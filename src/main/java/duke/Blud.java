package duke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Blud chatbot and its JavaFX user interface.
 *
 * <p>The command-processing method is shared by the graphical interface and
 * can also be used by other interfaces without depending on JavaFX controls.</p>
 */
public class Blud extends Application {
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
     * Starts the JavaFX window and wires the controls to the chatbot.
     * @param stage the primary JavaFX window.
     */
    @Override
    public void start(Stage stage) {
        Label title = new Label("Blud");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextArea conversation = new TextArea();
        conversation.setEditable(false);
        conversation.setWrapText(true);
        conversation.setPrefRowCount(18);
        conversation.setText("Hey! This is Blud, what can I do for you today?\n\n"
                + "Type a command such as 'list', 'todo buy milk', or 'bye'.");

        TextField commandInput = new TextField();
        commandInput.setPromptText("Enter a command...");
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);

        Runnable sendCommand = () -> {
            String command = commandInput.getText().trim();
            if (command.isEmpty()) {
                return;
            }
            conversation.appendText("\n\nYou: " + command + "\nBlud: ");
            conversation.appendText(processCommand(command));
            commandInput.clear();
            if ("bye".equals(command)) {
                commandInput.setDisable(true);
                sendButton.setDisable(true);
            }
        };
        sendButton.setOnAction(event -> sendCommand.run());
        commandInput.setOnAction(event -> sendCommand.run());

        HBox inputRow = new HBox(10, commandInput, sendButton);
        HBox.setHgrow(commandInput, Priority.ALWAYS);
        VBox content = new VBox(10, title, conversation, inputRow);
        content.setPadding(new Insets(15));
        VBox.setVgrow(conversation, Priority.ALWAYS);

        BorderPane root = new BorderPane(content);
        Scene scene = new Scene(root, 620, 480);
        stage.setTitle("Blud Chatbot");
        stage.setMinWidth(450);
        stage.setMinHeight(350);
        stage.setScene(scene);
        stage.show();
        commandInput.requestFocus();
    }

    /**
     * Processes one chatbot command and returns the response for display.
     * @param userInput command entered by the user.
     * @return a human-readable response without UI-specific formatting.
     */
    public String processCommand(String userInput) {
        if (userInput == null || userInput.isBlank()) {
            return "Please enter a command.";
        }
        if ("bye".equals(userInput.trim())) {
            return DEPARTURE;
        }

        String[] splitInput = userInput.trim().split("\\s+");
        String[] parts = userInput.trim().split(" /", -1);
        try {
            Ui.Command inputCommand = Ui.Command.stringToCommand(splitInput[0]);
            switch (inputCommand) {
                case LIST:
                    return formatTaskList();
                case FIND:
                    return new Find(userInput).execute(taskList);
                case MARK:
                    int idMark = Integer.parseInt(splitInput[1]) - 1;
                    taskList.markTask(idMark);
                    storage.saveTasks(taskList);
                    return "Nice! I've marked this task as done:\n" + taskList.getTask(idMark);
                case UNMARK:
                    int idUnmark = Integer.parseInt(splitInput[1]) - 1;
                    taskList.getTask(idUnmark).unmark();
                    storage.saveTasks(taskList);
                    return "OK, I've marked this task as not done yet:\n" + taskList.getTask(idUnmark);
                case DELETE:
                    Task deletedTask = taskList.delete(splitInput);
                    storage.saveTasks(taskList);
                    return "Task removed successfully:\n" + deletedTask;
                case TODO:
                    return addTask(new ToDo(parts));
                case DEADLINE:
                    return addTask(new Deadline(parts));
                case EVENT:
                    return addTask(new Event(parts));
                default:
                    throw new TaskTypeException("invalid task type " + splitInput[0]
                            + ", please use one of todo, event or deadline task types");
            }
        } catch (RuntimeException e) {
            return e.getMessage() == null ? "Unable to process command." : e.getMessage();
        }
    }

    /** Adds a task, persists the updated list, and creates its response. */
    private String addTask(Task newTask) {
        taskList.addTask(newTask);
        storage.saveTasks(taskList);
        return "added: " + newTask + "\nNow you have " + taskList.getSize() + " tasks in the list";
    }

    /** Formats all currently stored tasks for the chat transcript. */
    private String formatTaskList() {
        StringBuilder response = new StringBuilder(TASK_LIST_PREFACE);
        for (int i = 0; i < taskList.getSize(); i++) {
            response.append("\n").append(i + 1).append(". ").append(taskList.getTask(i));
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
