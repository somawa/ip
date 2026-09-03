package duke;

import javafx.application.Application;

/** Launches Blud separately to work around JavaFX classpath issues. */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Blud.class, args);
    }
}
