public class Task {
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

    @Override
    public String toString() {
        return "[" + doneRep() + "] " + this.taskDescription;
    }
}
