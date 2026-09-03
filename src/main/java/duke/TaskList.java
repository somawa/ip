package duke;

import java.util.ArrayList;
import java.util.List;

/** Stores and updates tasks in their user-visible order. */
public class TaskList {
    private List<Task> taskList;

    /** Creates an empty task list. */
    public TaskList() {
        this(new ArrayList<>());
    }

    /** Creates a task list containing the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.taskList = tasks;
    }

    /** Removes and returns the task identified by a one-based command index. */
    public Task delete(String[] splitInput) {
        Task removedTask;
        if (splitInput.length < 2) {
            throw new DeletionException();
        } else {
            try {
                int seq = Integer.parseInt(splitInput[1]);
                removedTask = this.taskList.get(seq - 1);
                this.taskList.remove(seq - 1);

            } catch (NumberFormatException e) {
                throw new DeletionException("Require an integer number to delete");
            } catch (IndexOutOfBoundsException e) {
                if (!this.taskList.isEmpty()) {
                    throw new DeletionException(String.format(
                            "Require a valid integer from %s to %s to delete from", 1, taskList.size()));
                } else {
                    throw new DeletionException("task list is empty, nothing to delete");
                }
            }
        }
        return removedTask;
    }

    /** Adds a task to the end of this list. */
    public void addTask(Task newTask, String header, String footer) {
        this.taskList.add(newTask);
    }

    /** Returns the number of tasks in this list. */
    public int getSize() {
        return this.taskList.size();
    }

    /** Returns the backing list of tasks in display order. */
    public List<Task> getTaskList() {
        return this.taskList;
    }

    /** Returns the task at the supplied zero-based index. */
    public Task getTask(int i) {
        return this.taskList.get(i);
    }

    /** Marks the task at the supplied zero-based index as completed. */
    public void markTask(int i) {
        taskList.get(i).mark();
    }
}
