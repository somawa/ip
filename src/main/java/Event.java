public class Event extends Task {
    private String startDate;
    private String endDate;
    public Event (String[] parts) {
        if (parts[0].length() < 7) {
            throw new EventException();
        } else if (parts.length < 2) {
            throw new DeadlineException("Missing start (/from) and end (/to) dates for deadline task");
        } else if (parts.length < 3) {
            if (!parts[1].startsWith("from") && parts[1].startsWith("to")) {
                throw new DeadlineException("Missing start (/from) date for deadline task");
            } else if (parts[1].startsWith("from") && !parts[1].startsWith("to")) {
                throw new DeadlineException("Missing end (/to) date for deadline task");
            } else {
                throw new DeadlineException("Missing start (/from) and end (/to) dates for deadline task");
            }
        }
        String mainDescription = parts[0].substring(6).strip();
        super(mainDescription);
        this.startDate = parts[1].substring(5).strip();
        this.endDate = parts[2].substring(3).strip();
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), startDate, endDate);
    }
}
