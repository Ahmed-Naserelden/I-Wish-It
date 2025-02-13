package controller;

import client.SceneSwitcher;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import client.DataStore;
import connection.NetworkManager;
import connection.Request;
import connection.Response;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class MainController implements Initializable {

    @FXML
    public Label profileName;
    @FXML
    public Label profileBalance;
    @FXML
    public ImageView memberImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(DataStore.getMember().getUsername());
        profileName.setText(DataStore.getMember().getUsername());
        refreshBalance();
        if (DataStore.getMemberImage() != null) {
            memberImage.setImage(DataStore.getMemberImage());
            Circle circle = new Circle(24);
            circle.setCenterX(24);
            circle.setCenterY(24);
            memberImage.setClip(circle);
        }
    }

    @FXML
    public void onMarketplaceClicked(ActionEvent event) {
        DataStore.setInsideMarketPlace(true);
        SceneSwitcher.switchScene("/scene/Marketplace.fxml");
    }

    @FXML
    public void onWishlistClicked(ActionEvent event) {
        DataStore.setEffectiveMember(
                DataStore.getMember().getId(),
                DataStore.getMember().getUsername(),
                DataStore.getMember().getDob()
        );
        DataStore.setInsideMarketPlace(false);
        SceneSwitcher.switchScene("/scene/Wishlist.fxml");
    }

    @FXML
    public void onFriendsClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/Friends.fxml");
    }

    @FXML
    public void onWalletClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/Wallet.fxml");
    }

    @FXML
    public void onSignOutClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/SignIn.fxml");
    }

    @FXML
    public void onNotificationsClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/Notifications.fxml");
    }

    @FXML
    public void onFriendRequestsClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/FriendRequests.fxml");
    }

    public void refreshBalance() {
        NumberFormat formatter = NumberFormat.getInstance();
        NetworkManager.send(new Request("GET", "/api/recharge", DataStore.getMember().getId()));
        Response response = NetworkManager.receive();
        String formated_balance;
        if (response.isPassed()) {
            int balance = (int) response.getPayload();
            formated_balance = formatter.format(balance);

        } else {
            formated_balance = "<ERROR>";
        }
        profileBalance.setText(formated_balance);
    }

    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Integer showAmountDialog(String title, String text) {
        while (true) { // Keep asking until a valid number is entered
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(title);
            dialog.setHeaderText(null);
            dialog.setContentText(text);

            // Restrict input to only numbers
            TextField inputField = dialog.getEditor();
            inputField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) { // Allow only digits
                    inputField.setText(oldValue);
                }
            });

            // Show dialog and wait for user input
            Optional<String> result = dialog.showAndWait();

            // Check if user entered something valid
            if (result.isPresent()) {
                String input = result.get().trim();
                if (!input.isEmpty()) {
                    return Integer.parseInt(input);
                } else {
                }
            } else {
                return -1; // User cancelled
            }
        }
    }
}
