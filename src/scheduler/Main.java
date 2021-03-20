package scheduler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * Main entrypoint into application.
     * Populates the Inventory with a couple of Parts and Products, then starts the JavaFX application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        DBConnection.open();
        launch(args);
        DBConnection.close();
    }

    /**
     * Loads a new View.
     * This updates the stage to a new View with some size bounding.
     *
     * @param stage The stage to update.
     * @param view  Path to the FXML document for the new View.
     * @return an instance of the FXMLLoader to interact with, if necessary.
     * @throws IOException さあ
     */
    public static FXMLLoader loadView(Stage stage, String view) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(view));
        loader.load();
        Scene scene = new Scene(loader.getRoot());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        /* Ensures that the main screen can't be resized to smaller than necessary. */
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());

        return loader;
    }

    /**
     * Wrapper around Alert.showAndWait() to fix sizing issues.
     * On my Linux system alert dialogs do not show properly via normal usage, so this provides a quick fix.
     * See https://github.com/javafxports/openjdk-jfx/issues/222 and https://bugs.openjdk.java.net/browse/JDK-8246795
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
     * The main entry point for this JavaFX application.
     * This is called after init and sets up the MainScreen View as our entry scene.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception Who knows.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        loadView(primaryStage, "/scheduler/view/Login.fxml");
        primaryStage.setTitle("Scheduler");
    }
}
