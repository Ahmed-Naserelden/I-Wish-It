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

public class WalletController extends MainController {

    @FXML
    private Label cardHolderName;

    @FXML
    private TextField pointsField;

    @FXML
    private Button rechargeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        cardHolderName.setText(DataStore.getMember().getUsername());
    }

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
