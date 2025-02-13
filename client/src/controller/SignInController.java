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

public class SignInController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleAction(ActionEvent event) {
        doAction();
    }

    public void doAction() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Login Failed", "Email or password cannot be empty!");
            return;
        }

        signInAction(new SignInPayload(email, password));
    }

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

    @FXML
    private void handleCreateAccountButtonAction(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/SignUp.fxml");
    }

    private static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
