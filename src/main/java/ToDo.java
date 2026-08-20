public class ToDo extends Task {
    public ToDo (String[] parts) {
        String mainDescription = parts[0].substring(5).strip();
        super(mainDescription);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}