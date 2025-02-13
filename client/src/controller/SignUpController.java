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

public class SignUpController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker dobField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField reenterPasswordField;
    @FXML
    private Button signUpButton;

    @FXML
    private void handleAction(ActionEvent event) {
        doAction();
    }

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

    @FXML
    private void handleCancelButtonAction() {
        SceneSwitcher.switchScene("/scene/SignIn.fxml");
    }
}
