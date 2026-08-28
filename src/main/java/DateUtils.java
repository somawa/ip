import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    // Matches input like "2/12/2019 1800" or "02/12/2019 1800"
    // 'd' and 'M' single letters allow for single-digit days/months
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    // Outputs exactly: "Dec 02 2019, 6:00 PM"
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    public static LocalDateTime parseInput(String input) {
        try {
            return LocalDateTime.parse(input.trim(), INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
        }
    }

    public static String formatForStorage(LocalDateTime dateTime) {
        return dateTime.format(INPUT_FORMATTER);
    }

    public static String formatOutput(LocalDateTime dateTime) {
        return dateTime.format(OUTPUT_FORMATTER);
    }
}
