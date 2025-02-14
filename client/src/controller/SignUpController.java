package controller;

import client.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import connection.Request;
import connection.NetworkManager;
import connection.Response;
import connection.SignUpPayload;

/**
 * Controller class for the sign-up view.
 * Handles user interactions and sign-up actions.
 */
public class SignUpController {

    @FXML
    private TextField firstNameField; // TextField for entering the first name
    @FXML
    private TextField lastNameField; // TextField for entering the last name
    @FXML
    private TextField emailField; // TextField for entering the email
    @FXML
    private DatePicker dobField; // DatePicker for entering the date of birth
    @FXML
    private PasswordField passwordField; // PasswordField for entering the password
    @FXML
    private PasswordField reenterPasswordField; // PasswordField for re-entering the password
    @FXML
    private Button signUpButton; // Button for triggering the sign-up action

    /**
     * Handles the action event triggered by clicking the sign-up button.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void handleAction(ActionEvent event) {
        doAction();
    }

    /**
     * Performs the sign-up action.
     * Validates the input fields and sends a sign-up request to the server.
     */
    public void doAction() {
        String email = emailField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String password = passwordField.getText().trim();
        String reenterPassword = reenterPasswordField.getText().trim();
        String dob = dobField.getValue().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || dob.isEmpty() || password.isEmpty() || reenterPassword.isEmpty()) {
            MainController.showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(reenterPassword)) {
            MainController.showAlert("Error", "Passwords don't match!", Alert.AlertType.ERROR);
            return;
        }

        signUpButton.setDisable(true);

        SignUpPayload signUpPayload = new SignUpPayload(email, firstName + " " + lastName, password, dob);

        Request signUpRequest = new Request("POST", "/api/auth/signup", signUpPayload);

        NetworkManager.send(signUpRequest);

        Response response = NetworkManager.receive();

        if (response.isPassed()) {
            SceneSwitcher.switchScene("/scene/SignIn.fxml");
            MainController.showAlert(
                    "Success",
                    "Account created successfully!",
                    Alert.AlertType.INFORMATION
            );
        } else {
            MainController.showAlert(
                    "Signup Failed",
                    "Email already exists or invalid data!",
                    Alert.AlertType.ERROR
            );
        }

        signUpButton.setDisable(false);
    }

    /**
     * Handles the action event triggered by clicking the cancel button.
     * Switches the scene to the sign-in view.
     */
    @FXML
    private void handleCancelButtonAction() {
        SceneSwitcher.switchScene("/scene/SignIn.fxml");
    }
}
