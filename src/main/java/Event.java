public class Event extends Task {
    private String startDate;
    private String endDate;
    public Event (String[] parts) {
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
