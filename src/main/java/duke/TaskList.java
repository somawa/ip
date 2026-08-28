package duke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskList {
    private List<Task> taskList;
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
                    throw new DeletionException(String.format("Require a valid integer from %s to %s to delete from", 1, taskList.size()));
                } else {
                    throw new DeletionException("task list is empty, nothing to delete");
                }
            }
        }
        return removedTask;
    }

    public void addTask(Task newTask, String header, String footer) {
        System.out.println("broke 0");
        this.taskList.add(newTask);
        System.out.println("broke 1");
        System.out.println("broke 2");
    }

    public TaskList() {
        new TaskList(new ArrayList<Task>());
    }

    public TaskList(List<Task> tasks) {
        this.taskList = tasks;
    }

    public int getSize() {
        return this.taskList.size();
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public Task getTask(int i) {
        return this.taskList.get(i);
    }

    public void markTask(int i) {
        taskList.get(i).mark();
    }
}
