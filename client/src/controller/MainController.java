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

/**
 * MainController class to manage the main view of the application.
 * Handles initialization, navigation, and user interactions.
 */
public class MainController implements Initializable {

    @FXML
    public Label profileName; // Label for displaying the profile name
    @FXML
    public Label profileBalance; // Label for displaying the profile balance
    @FXML
    public ImageView memberImage; // ImageView for displaying the member's image

    /**
     * Initializes the controller class.
     * This method is automatically called after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
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

    /**
     * Handles the event when the Marketplace button is clicked.
     * Switches the scene to the Marketplace view.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void onMarketplaceClicked(ActionEvent event) {
        DataStore.setInsideMarketPlace(true);
        SceneSwitcher.switchScene("/scene/Marketplace.fxml");
    }

    /**
     * Handles the event when the Wishlist button is clicked.
     * Switches the scene to the Wishlist view.
     *
     * @param event The action event triggered by clicking the button.
     */
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

    /**
     * Handles the event when the Friends button is clicked.
     * Switches the scene to the Friends view.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void onFriendsClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/Friends.fxml");
    }

    /**
     * Handles the event when the Wallet button is clicked.
     * Switches the scene to the Wallet view.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void onWalletClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/Wallet.fxml");
    }

    /**
     * Handles the event when the Sign Out button is clicked.
     * Switches the scene to the Sign In view.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void onSignOutClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/SignIn.fxml");
    }

    /**
     * Handles the event when the Notifications button is clicked.
     * Switches the scene to the Notifications view.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void onNotificationsClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/Notifications.fxml");
    }

    /**
     * Handles the event when the Friend Requests button is clicked.
     * Switches the scene to the Friend Requests view.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    public void onFriendRequestsClicked(ActionEvent event) {
        SceneSwitcher.switchScene("/scene/FriendRequests.fxml");
    }

    /**
     * Refreshes the profile balance by sending a request to the server.
     * Updates the profile balance label with the new balance.
     */
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

    /**
     * Shows an alert dialog with the specified title, message, and alert type.
     *
     * @param title The title of the alert dialog.
     * @param message The message to display in the alert dialog.
     * @param type The type of the alert dialog (e.g., INFORMATION, WARNING, ERROR).
     */
    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a dialog to prompt the user to enter an amount.
     * Restricts input to only numbers and keeps asking until a valid number is entered.
     *
     * @param title The title of the dialog.
     * @param text The prompt text to display in the dialog.
     * @return The entered amount as an Integer, or -1 if the user cancels the dialog.
     */
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
                }
            } else {
                return -1; // User cancelled
            }
        }
    }
}
