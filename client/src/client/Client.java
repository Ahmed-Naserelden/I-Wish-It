package client;

import javafx.application.Application;
import javafx.stage.Stage;
import connection.NetworkManager;

/**
 * Main client application class.
 * Handles the initialization and startup of the application.
 */
public class Client extends Application {

    /**
     * Starts the JavaFX application.
     * Sets the primary stage and switches to the sign-in scene.
     * Opens the network connection.
     *
     * @param stage The primary stage for this application.
     * @throws Exception if an error occurs during application startup.
     */
    @Override
    public void start(Stage stage) throws Exception {
        SceneSwitcher.setPrimaryStage(stage);
        SceneSwitcher.switchScene("/scene/SignIn.fxml");
        NetworkManager.openConnection();
    }

    /**
     * The main entry point for the application.
     * Launches the JavaFX application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Logs a failure message to the console.
     *
     * @param string The message to log.
     */
    public static void logFail(String string) {
        System.out.println("[\u001B[31m\u001B[1mFAIL\u001B[0m] " + string);
    }

    /**
     * Logs a success message to the console.
     *
     * @param string The message to log.
     */
    public static void logPass(String string) {
        System.out.println("[\u001B[32m\u001B[1mPASS\u001B[0m] " + string);
    }

    /**
     * Logs an informational message to the console.
     *
     * @param string The message to log.
     */
    public static void logInfo(String string) {
        System.out.println("[\u001B[34m\u001B[1mINFO\u001B[0m] " + string);
    }

    /**
     * Logs an exit message to the console.
     *
     * @param string The message to log.
     */
    public static void logExit(String string) {
        System.out.println("[\u001B[35m\u001B[1mEXIT\u001B[0m] " + string);
    }
}
