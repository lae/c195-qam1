package scheduler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import scheduler.util.FXUtil;

/**
 * The Main class of this application.
 *
 * @author Musee Ullah
 */
public class Main extends Application {
    /**
     * Main entrypoint into application.
     * Initializes a database connection then starts the JavaFX application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        State.startDBConnection();
        launch(args);
        State.closeDBConnection();
    }

    /**
     * The main entry point for this JavaFX application.
     * This is called after init and brings up a Login window, and then a Dashboard on successful login.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception Who knows.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!State.isDBConnected()) {
            Alert alert = FXUtil.detailedAlert(Alert.AlertType.ERROR, "", "Cannot connect to the database.\n" +
                    "Is the configuration correct?\n\nCheck the console for more information.");
            FXUtil.displayAlert(alert);
            Platform.exit();
            return;
        }
        // Bring up the login form as a utility window that'll float, first, and wait until it's closed.
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.UTILITY);
        FXUtil.loadView(loginStage, "Login.fxml");
        loginStage.setTitle("Scheduler Login");
        loginStage.showAndWait();

        // If the login window is closed and we're not logged in, exit the application.
        if (!State.isLoggedIn()) {
            System.out.println("Detected close without valid login. Exiting.");
            Platform.exit();
            return;
        }

        // If login was successful, initialize the primary dashboard window.
        FXUtil.loadView(primaryStage, "Dashboard.fxml");
        primaryStage.setTitle("Scheduler");
        primaryStage.show();
    }
}
