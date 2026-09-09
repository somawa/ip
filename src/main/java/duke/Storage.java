package duke;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Persists the current task list to the project's data directory. */
public class Storage {
    private Path taskFile;
    private Path parentDir;

    /**
     * Creates storage backed by the specified file.
     */
    public Storage(String filePath) {
        this.taskFile = Path.of(filePath);
        this.parentDir = this.taskFile.getParent();
    }

    /** Reconstructs a task from one line of the storage format. */
    private static Task mapTaskFromString(String line) {
        String[] parts = line.split(" \\| ");
        String taskInput = parts[0];
        TaskType taskType = TaskType.stringToTaskType(taskInput);
        boolean isDone = parts[1].equals("1");
        String mainDescription = parts[2];
        Task newTask;

        switch (taskType) {
            case T:
                newTask = new ToDo(new String[] {String.format("TODO %s", mainDescription)});
                break;
            case D:
                String deadline = parts[3];
                newTask = new Deadline(new String[] {
                        String.format("DEADLINE %s", mainDescription),
                        String.format("by %s", deadline)
                });
                break;
            case E:
                String startDate = parts[3];
                String endDate = parts[4];
                newTask = new Event(new String[] {
                        String.format("EVENT %s", mainDescription),
                        String.format("from %s", startDate),
                        String.format("to %s", endDate)
                });
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

    /**
     * Creates the parent directory and file when they do not exist.
     */
    private Boolean handlePath() throws IOException {
        if (this.parentDir != null && !Files.exists(this.parentDir)) {
            Files.createDirectories(this.parentDir);
        }
        if (!Files.exists(this.taskFile)) {
            Files.createFile(this.taskFile);
            System.out.println("Created missing file: " + this.taskFile.getFileName());
            return false;
        } else {
            return true;
        }
    }

    /**
     * Writes all tasks to disk, replacing the previous snapshot.
     *
     * @param taskList current tasks in their display order
     */
    public void saveTasks(TaskList taskList) {
        assert taskList != null : "Storage must save a non-null task list";
        assert taskList.getTaskList().stream().allMatch(task -> task != null)
                : "A task list must not contain null tasks";
        try {
            this.handlePath();
            Files.write(this.taskFile, taskList.getTaskList()
                    .stream()
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

        /** Converts a stored task type token to its enum value. */
        public static TaskType stringToTaskType(String taskInput) {
            if (taskInput == null) {
                return null;
            }
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

    /** Loads all persisted tasks, creating the storage file if necessary. */
    public List<Task> loadTasks() {
        try {
            Boolean fileExists = this.handlePath();
            if (fileExists) {
                return new ArrayList<>(
                        Files.readAllLines(this.taskFile)
                                .stream()
                                .map(Storage::mapTaskFromString)
                                .toList()
                );
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load tasks", e);
        }
    }
}
