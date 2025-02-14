package controller;

import client.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import client.DataStore;
import connection.MemberPayload;
import connection.Request;
import connection.SignInPayload;
import connection.NetworkManager;
import connection.Response;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the sign-in view.
 * Handles user interactions and sign-in actions.
 */
public class SignInController {

    @FXML
    private TextField emailField; // TextField for entering the email
    @FXML
    private PasswordField passwordField; // PasswordField for entering the password
    @FXML
    private Button loginButton; // Button for triggering the login action

    /**
     * Initializes the controller class.
     * This method is automatically called after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Handles the action event triggered by clicking the login button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleAction(ActionEvent event) {
        doAction();
    }

    /**
     * Performs the sign-in action.
     * Validates the input fields and sends a sign-in request to the server.
     */
    public void doAction() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Login Failed", "Email or password cannot be empty!");
            return;
        }

        signInAction(new SignInPayload(email, password));
    }

    /**
     * Sends a sign-in request to the server and handles the response.
     *
     * @param payload The SignInPayload object containing the email and password.
     */
    public static void signInAction(SignInPayload payload) {
        Request signInRequest = new Request("POST", "/api/auth/signin", payload);

        NetworkManager.send(signInRequest);
        Response response = NetworkManager.receive();

        if (response.isPassed()) {
            DataStore.setMember((MemberPayload) response.getPayload());
            DataStore.setEffectiveMember(
                    DataStore.getMember().getId(),
                    DataStore.getMember().getUsername(),
                    DataStore.getMember().getDob()
            );
            DataStore.setInsideMarketPlace(true);
            SceneSwitcher.switchScene("/scene/Marketplace.fxml");
        } else {
            showError("Login Failed", "Invalid email or password!");
        }
    }

    /**
     * Handles the action event triggered by clicking the create account button.
     * Switches the scene to the sign-up view.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleCreateAccountButtonAction(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/SignUp.fxml");
    }

    /**
     * Shows an error alert with the specified title and message.
     *
     * @param title The title of the alert.
     * @param message The message to display in the alert.
     */
    private static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
