package duke;

public abstract class Task {
    private boolean done;
    private String taskDescription;

    public Task(String taskDescription) {
        this.taskDescription = taskDescription;
        this.done = false;
    }

    private char doneRep() {
        if (this.done) {
            return 'X';
        } else {
            return ' ';
        }
    }

    public void mark() {
        this.done = true;
    }

    public void unmark() {
        this.done = false;
    }

    /**
     * Returns this task's stable on-disk representation.
     *
     * @return a line that can be written to the task file
     */
    public abstract String toStorageString();

    protected boolean isDone() {
        return done;
    }

    protected String getTaskDescription() {
        return taskDescription;
    }

    @Override
    public String toString() {
        return "[" + doneRep() + "] " + this.taskDescription;
    }
}
