package duke;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/** Starts the JavaFX interface for the Blud chatbot. */
public class BludApplication extends Application {
    /** Loads the FXML view and displays the primary application window. */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(BludApplication.class.getResource("/view/MainWindow.fxml"));
            BorderPane root = loader.load();
            loader.<MainWindow>getController().setBlud(new Blud());
            Scene scene = new Scene(root);
            stage.setTitle("Blud Chatbot");
            stage.setMinWidth(560);
            stage.setMinHeight(640);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Unable to load the Blud interface.", exception);
        }
    }
}
