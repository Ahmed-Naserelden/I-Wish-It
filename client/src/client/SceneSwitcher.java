package client;

import connection.Request;
import connection.NetworkManager;
import controller.SignInController;
import controller.SignUpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;

/**
 * SceneSwitcher class to manage scene transitions in the application.
 * Handles loading and switching between different FXML scenes.
 */
public class SceneSwitcher {

    private static Stage stage;
    private static Scene scene;
    private static final VBox CONTAINER = new VBox();
    private static Object controller;
    private static String current;

    /**
     * Sets the primary stage for the application.
     * Initializes the main scene and sets up key event handling.
     *
     * @param stage The primary stage for this application.
     */
    public static void setPrimaryStage(Stage stage) {
        SceneSwitcher.stage = stage;
        SceneSwitcher.stage.setTitle("iWishIt Application - v0.1.0");
        SceneSwitcher.stage.getIcons().add(new Image("scene/img/ico/coin.png"));

        // Wrap VBox inside Scene and set it once
        scene = new Scene(CONTAINER, 1280, 720); // Default size
        stage.setScene(scene);
        stage.show();
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (current.endsWith("SignIn.fxml")) {
                    ((SignInController) controller).doAction();
                } else if (current.endsWith("SignUp.fxml")) {
                    ((SignUpController) controller).doAction();
                }
            }
        });
    }

    /**
     * Switches to a different scene specified by the FXML file.
     * Loads the FXML file, sets the controller, and updates the scene.
     *
     * @param fxmlFile The path to the FXML file to load.
     */
    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFile));
            if (loader.getLocation() == null) {
                throw new IllegalArgumentException("FXML file not found: " + fxmlFile);
            }

            Parent root = loader.load();
            controller = loader.getController();

            VBox.setVgrow(root, Priority.ALWAYS);

            CONTAINER.getChildren().setAll(root);

            stage.setOnCloseRequest(event -> {
                Client.logExit("Closing Application");
                NetworkManager.send(new Request("POST", "/api/client/disconnect"));
                NetworkManager.receive();
                NetworkManager.closeConnection();
                System.exit(0);
            });
            current = fxmlFile;
        } catch (IOException | IllegalArgumentException e) {
            Client.logFail(e.toString());
        }
    }
}
