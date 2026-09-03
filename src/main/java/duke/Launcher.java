package duke;

import javafx.application.Application;

/** Launches Blud separately to work around JavaFX classpath issues. */
public class Launcher {
    /** Launches the JavaFX application with the supplied command-line arguments. */
    public static void main(String[] args) {
        Application.launch(Blud.class, args);
    }
}
