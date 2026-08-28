import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists the current task list to the project's data directory.
 */
public class Storage {
//    private static final Path TASK_FILE = Path.of("data", "blud.txt");
//    private static final Path parentDir = TASK_FILE.getParent();
    private Path taskFile;
    private Path parentDir;

    private static Task mapTaskFromString(String line) {
        String[] parts = line.split(" \\| ");
        String taskInput = parts[0];
        TaskType taskType = TaskType.stringToTaskType(taskInput);
        boolean isDone = parts[1].equals("1");
        String mainDescription = parts[2];
        Task newTask;

        switch(taskType) {
            case T:
                newTask = new ToDo(new String[] { String.format("TODO %s", mainDescription) } );
                break;
            case D:
                String deadline = parts[3];
                newTask = new Deadline(new String[] {
                        String.format("DEADLINE %s", mainDescription),
                        String.format("by %s", deadline)
                } );
                break;
            case E:
                String startDate = parts[3];
                String endDate = parts[4];
                newTask = new Event(new String[] {
                        String.format("EVENT %s", mainDescription),
                        String.format("from %s", startDate),
                        String.format("to %s", endDate)
                } );
                break;
            default:
                throw new TaskTypeException(
                        String.format(
                                "invalid task type %s, please use one of todo, event or deadline task types",
                                taskType));
        }
        if (isDone) {
            newTask.mark();
        }
        return newTask;
    }

    public Storage(String filePath) {
        this.taskFile = Path.of(filePath);
        this.parentDir = this.taskFile.getParent();
    }

    private Boolean handlePath() throws IOException {
        // 1. Create parent directories if they don't exist
        if (this.parentDir != null && !Files.exists(this.parentDir)) {
            Files.createDirectories(this.parentDir);
        }
        // 2. Create the file if it doesn't exist
        if (!Files.exists(this.taskFile)) {
            Files.createFile(this.taskFile);
            System.out.println("Created missing file: " + this.taskFile.getFileName());
            return false;
        } else {
            System.out.println("File already exists. No action taken.");
            return true;
        }
    }

    /**
     * Writes all tasks to disk, replacing the previous snapshot.
     *
     * @param taskList current tasks in their display order
     */
    public void saveTasks(TaskList taskList) {
        try {
//            Files.createDirectories(TASK_FILE.getParent());
            this.handlePath();
            Files.write(this.taskFile, taskList.getTaskList().stream()
                    .map(Task::toStorageString)
                    .toList());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save tasks", e);
        }
    }

    private enum TaskType {
        T,
        D,
        E;

        public static TaskType stringToTaskType(String taskInput) {
            if (taskInput == null) return null;
            try {
                // Trim whitespace and convert to uppercase to match enum style
                return TaskType.valueOf(taskInput.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new TaskTypeException(
                        String.format("invalid task type %s, please use one of todo, event or deadline task types",
                                taskInput));
            }
        }

    }

    public List<Task> loadTasks() {
        try {
            Boolean fileExists = this.handlePath();
            if (fileExists) {
                return new ArrayList<>(
                        Files.readAllLines(this.taskFile)
                                .stream()
                                .map(Storage::mapTaskFromString) // or implementation logic
                                .toList()
                );
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            // Handle file reading errors
            throw new IllegalStateException("Unable to load tasks", e);
        }
    }
}
