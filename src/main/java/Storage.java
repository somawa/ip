import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Persists the current task list to the project's data directory.
 */
public class Storage {
    private static final Path TASK_FILE = Path.of("data", "blud.txt");
    private static final Path parentDir = TASK_FILE.getParent();

    private Storage() {
        // Utility class; do not instantiate.
    }

    /**
     * Writes all tasks to disk, replacing the previous snapshot.
     *
     * @param tasks current tasks in their display order
     */
    public static void saveTasks(List<Task> tasks) {
        try {
//            Files.createDirectories(TASK_FILE.getParent());
            // 1. Create parent directories if they don't exist
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            // 2. Create the file if it doesn't exist
            if (!Files.exists(TASK_FILE)) {
                Files.createFile(TASK_FILE);
                System.out.println("Created missing file: " + TASK_FILE.getFileName());
            } else {
                System.out.println("File already exists. No action taken.");
            }
            Files.write(TASK_FILE, tasks.stream()
                    .map(Task::toStorageString)
                    .toList());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save tasks", e);
        }
    }

//    public static void loadTasks() {
//
//    }
}
