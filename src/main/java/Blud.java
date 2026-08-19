/**
 * Entry point for the Blud chatbot.
 */
public class Blud {
    /**
     * Chains together a chat section using the input string array
     *
     * @param parts correspond to input string array in order of chaining
     *
     * @return section comprised of chained parts
     */
    public static String section(String[] parts) {
        return String.join("\n", parts);
    }
    /**
     * Starts Blud and displays its name, entry and exit greeting.
     *
     * @param args command-line arguments, which are currently unused
     */
    public static void main(String[] args) {
        /**
         * AI-Generated String Banner
         */
        String banner = " ____  _            _\n"
                + "| __ )| |_   _  ___| |\n"
                + "|  _ \\| | | | |/ __| |\n"
                + "| |_) | | |_| | (__|_|\n"
                + "|____/|_|\\__,_|\\___(_)\n";
        String greeting = "Hey! This is Blud, what can I do for you today?";
        String departure = "Thanks for the conversation, see you soon!";
        String breakLine = "-------------------------------";
        System.out.println(section(new String[]{breakLine, banner}));
        System.out.println(section(new String[]{greeting, breakLine}));
        System.out.println(section(new String[]{departure, breakLine}));
    }
}
