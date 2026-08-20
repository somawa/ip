public class Deadline extends Task {
    private String deadline;
    public Deadline (String[] parts) {
        String mainDescription = parts[0].substring(9).strip();
        super(mainDescription);
        this.deadline = parts[1].substring(3).strip();
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), deadline);
    }
}
