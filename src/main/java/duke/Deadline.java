package duke;

import java.time.LocalDateTime;

public class Deadline extends Task {
//    private String deadline;
    private LocalDateTime deadline;
    public Deadline (String[] parts) {
        if (parts[0].length() < 10) {
            throw new DeadlineException();
        } else if (parts.length < 2 || !parts[1].startsWith("by")) {
            throw new DeadlineException("Missing deadline (/by) for deadline task");
        }
        String mainDescription = parts[0].substring(9).strip();
        super(mainDescription);
        this.deadline = DateUtils.parseInput(parts[1].substring(3).strip());
    }

    @Override
    public String toString() {
        return String.format(
                "[D]%s (by: %s)",
                super.toString(),
                DateUtils.formatOutput(deadline)
        );
    }

    @Override
    public String toStorageString() {
        return String.format(
                "D | %d | %s | %s",
                isDone() ? 1 : 0,
                getTaskDescription(),
                DateUtils.formatForStorage(deadline)
        );
    }
}
