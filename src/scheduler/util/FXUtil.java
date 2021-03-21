package scheduler.util;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import scheduler.Main;

import java.io.IOException;
import java.util.Optional;

public class FXUtil {
    /**
     * Loads a new View onto a Stage.
     * This updates the stage to a new View with some size bounding.
     *
     * @param stage The stage to update.
     * @param view  Path to the FXML document for the new View.
     * @return an instance of the FXMLLoader to interact with, if necessary.
     * @throws IOException さあ
     */
    public static FXMLLoader loadView(Stage stage, String view) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(Main.class.getResource("/scheduler/view/" + view));
        loader.load();
        Scene scene = new Scene(loader.getRoot());
        stage.setScene(scene);
        stage.sizeToScene();

        /* Ensures that the main screen can't be resized to smaller than necessary. */
        stage.setOnShown(evt -> {
            stage.setMinHeight(stage.getHeight());
            stage.setMinWidth(stage.getWidth());
        });

        return loader;
    }

    /**
     * Wrapper around Alert.showAndWait() to fix sizing issues.
     * On my Linux system alert dialogs do not show properly via normal usage, so this provides a quick fix.
     * See https://github.com/javafxports/openjdk-jfx/issues/222 and https://bugs.openjdk.java.net/browse/JDK-8246795
     * <p>
     * This function makes use of a lambda expression to execute a couple of statements inside of a separate thread from
     * the JavaFX application's thread so that time is given for the alert window to resize itself before making the
     * window not resizeable via mouse. This is needed to get around buggy behaviour in JavaFX under tiling WMs.
     *
     * @param alert The instantiated Alert object to apply changes on.
     * @return a result object containing information about the user's actions.
     */
    public static Optional<ButtonType> displayAlert(Alert alert) {
        alert.setResizable(true);
        // Forces window to float.
        alert.initStyle(StageStyle.UTILITY);

        Thread t = new Thread(() -> {
            // I'm not spending any more time on dealing with this platform bug.
            // This provides some time for the dialog to resize itself correctly, and then disable resizes
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return;
            }
            Platform.runLater(() -> alert.setResizable(false));
        });
        t.start();

        return alert.showAndWait();
    }

    /**
     * Wrapper function to create a detailed Alert.
     * This prepares a label and expanded textarea in a typical alert dialog.
     *
     * @param type    The type of alert, e.g. confirmation or error.
     * @param message The headlining message of the alert prompt.
     * @param details Specific details associated with the alert that are relevant to the user.
     * @return an instance of the Alert object to act upon.
     */
    public static Alert detailedAlert(Alert.AlertType type, String message, String details) {
        Alert alert = new Alert(type);
        Label topMessage = new Label(message);
        TextArea detailsBox = new TextArea(details);

        detailsBox.setEditable(false);
        detailsBox.setWrapText(true);
        detailsBox.setMaxHeight(Double.MAX_VALUE);
        GridPane.setHgrow(detailsBox, Priority.ALWAYS);
        GridPane body = new GridPane();
        body.add(topMessage, 0, 0);
        body.add(detailsBox, 0, 1);
        alert.getDialogPane().setContent(body);

        return alert;
    }

    /**
     * This takes an FX event and identifies the Stage that it came from.
     * https://docs.oracle.com/javase/8/javafx/api/javafx/event/Event.html
     *
     * @param event an FX event triggered from the application.
     * @return the Stage the event occurred in.
     */
    public static Stage getStage(Event event) {
        return (Stage) ((Control) event.getSource()).getScene().getWindow();
    }
}
