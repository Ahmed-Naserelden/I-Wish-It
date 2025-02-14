package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import client.DataStore;
import client.SceneSwitcher;
import connection.NetworkManager;
import connection.RechargePayload;
import connection.Request;
import connection.Response;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller class for the wallet view.
 * Handles user interactions and recharge actions.
 */
public class WalletController extends MainController {

    @FXML
    private Label cardHolderName; // Label for displaying the card holder's name

    @FXML
    private TextField pointsField; // TextField for entering the recharge amount

    @FXML
    private Button rechargeButton; // Button for triggering the recharge action

    /**
     * Initializes the controller class.
     * This method is automatically called after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        cardHolderName.setText(DataStore.getMember().getUsername());
    }

    /**
     * Handles the action event triggered by clicking the recharge button.
     * Validates the input amount and sends a recharge request to the server.
     *
     * @param event The action event triggered by clicking the button.
     */
    public void handleRechargeButton(ActionEvent event) {
        String rechargeAmount = pointsField.getText().trim();
        int amount;

        if (!rechargeAmount.matches("[1-9]\\d*")) {
            MainController.showAlert(
                "Invalid Amount!",
                "Please enter a valid positive amount greater than 0.",
                Alert.AlertType.ERROR
            );
            return;
        } else {
            amount = Integer.parseInt(rechargeAmount);
        }

        rechargeButton.setDisable(true);

        int userId = DataStore.getMember().getId();
        RechargePayload rechargePayload = new RechargePayload(userId, amount);
        Request rechargeRequest = new Request("PATCH", "/api/recharge", rechargePayload);
        NetworkManager.send(rechargeRequest);
        Response response = NetworkManager.receive();

        if (response.isPassed()) {
            refreshBalance();
            pointsField.clear();
            MainController.showAlert(
                "Success",
                "Balance has been successfully recharged!",
                Alert.AlertType.INFORMATION
            );
        } else {
            MainController.showAlert(
                "Error",
                "There was an error while recharging the balance.",
                Alert.AlertType.ERROR
            );
        }

        rechargeButton.setDisable(false);
    }
}
