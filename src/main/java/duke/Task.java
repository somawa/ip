package duke;

/** Provides common state and behavior for all Blud tasks. */
public abstract class Task {
    private boolean done;
    private final String taskDescription;

    /** Creates a task with the supplied description and an incomplete status. */
    public Task(String taskDescription) {
        assert taskDescription != null : "A task must always have a description";
        this.taskDescription = taskDescription;
        this.done = false;
    }

    /** Returns the display marker for the task's completion status. */
    private char doneRep() {
        if (this.done) {
            return 'X';
        } else {
            return ' ';
        }
    }

    /** Marks this task as completed. */
    public void mark() {
        this.done = true;
    }

    /** Marks this task as incomplete. */
    public void unmark() {
        this.done = false;
    }

    /**
     * Returns this task's stable on-disk representation.
     *
     * @return a line that can be written to the task file
     */
    public abstract String toStorageString();

    /** Returns whether this task is completed. */
    protected boolean isDone() {
        return done;
    }

    /** Returns the description displayed for this task. */
    protected String getTaskDescription() {
        return taskDescription;
    }

    /** Returns this task's display representation. */
    @Override
    public String toString() {
        return "[" + doneRep() + "] " + this.taskDescription;
    }
}
