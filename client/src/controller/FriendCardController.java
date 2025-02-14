package controller;

import client.Client;
import client.DataStore;
import client.SceneSwitcher;
import connection.FriendActionPayload;
import connection.FriendPayload;
import connection.NetworkManager;
import connection.Request;
import connection.Response;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

/**
 * Controller class for the friend card view.
 * Handles the display and actions for individual friend cards.
 */
public class FriendCardController implements Initializable {

    @FXML
    private Label memberName; // Label for displaying the friend's name
    @FXML
    private Label memberDob; // Label for displaying the friend's date of birth
    @FXML
    private Button actionButton; // Button for performing actions (e.g., Decline, Remove)
    @FXML
    private Button showButton; // Button for showing the friend's profile
    @FXML
    private ImageView cardImage; // ImageView for displaying the friend's image

    private int friendId; // ID of the friend
    private String cardType; // Type of the card (e.g., FRIEND, REQUEST)

    /**
     * Initializes the controller class.
     * This method is automatically called after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Sets the type of the card and updates the UI accordingly.
     *
     * @param type The type of the card (e.g., FRIEND, REQUEST).
     */
    public void setCardType(String type) {
        cardType = type;
        switch (cardType) {
            case "FRIEND":
                break;
            case "REQUEST":
                actionButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 20;");
                actionButton.setText("Decline");
                showButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-background-radius: 20;");
                showButton.setText("Accept");
                break;
        }
    }

    /**
     * Sets the friend data and updates the UI with the friend's information.
     *
     * @param friend The FriendPayload object containing the friend's information.
     */
    public void setFriendData(FriendPayload friend) {
        friendId = friend.getId();
        memberName.setText(friend.getName());
        memberDob.setText(friend.getDob());

        Image image = DataStore.getMemberImage(friend.getImage());
        if (image != null) {
            cardImage.setImage(image);
        }

        Circle circle = new Circle(26);
        circle.setCenterX(26);
        circle.setCenterY(26);
        cardImage.setClip(circle);
    }

    /**
     * Handles the event when the "Show Profile" button is clicked.
     * Performs actions based on the card type (e.g., view profile, accept request).
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void onShowProfile(ActionEvent event) {
        switch (cardType) {
            case "FRIEND":
                DataStore.setEffectiveMember(
                        friendId,
                        memberName.getText(),
                        memberDob.getText()
                );
                DataStore.setInsideMarketPlace(false);
                SceneSwitcher.switchScene("/scene/Wishlist.fxml");
                break;
            case "REQUEST": // Accept
                NetworkManager.send(
                        new Request("POST", "/api/friends/request",
                                new FriendActionPayload(DataStore.getMember().getId(), friendId)
                        )
                );
                Response response = NetworkManager.receive();
                if (response.isPassed()) {
                    MainController.showAlert(
                            "Success",
                            "You've accepted a friend request!",
                            Alert.AlertType.INFORMATION
                    );
                    SceneSwitcher.switchScene("/scene/FriendRequests.fxml");
                } else {
                    MainController.showAlert(
                            "Failed",
                            "There is an error accepting the request!",
                            Alert.AlertType.ERROR
                    );
                }
                break;
        }
    }

    /**
     * Handles the event when the action button is clicked.
     * Performs actions based on the card type (e.g., remove friend, decline request).
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void onActionClicked(ActionEvent event) {
        Response response;
        switch (cardType) {
            case "FRIEND":
                NetworkManager.send(
                        new Request("DELETE", "/api/friends",
                                new FriendActionPayload(DataStore.getMember().getId(), friendId)
                        )
                );
                response = NetworkManager.receive();
                if (response.isPassed()) {
                    MainController.showAlert(
                            "Success",
                            "Sadly, a friend has been removed successfully!",
                            Alert.AlertType.INFORMATION
                    );
                    SceneSwitcher.switchScene("/scene/Friends.fxml");
                } else {
                    MainController.showAlert(
                            "Failed",
                            "Fortunately, you cannot remove your friend :)",
                            Alert.AlertType.ERROR
                    );
                }
                break;
            case "REQUEST": // Decline
                NetworkManager.send(
                        new Request("DELETE", "/api/friends/request",
                                new FriendActionPayload(DataStore.getMember().getId(), friendId)
                        )
                );
                response = NetworkManager.receive();
                if (response.isPassed()) {
                    MainController.showAlert(
                            "Success",
                            "Sadly, you've declined a friend request!",
                            Alert.AlertType.INFORMATION
                    );
                    SceneSwitcher.switchScene("/scene/FriendRequests.fxml");
                } else {
                    MainController.showAlert(
                            "Failed",
                            "There is an error declining the request!",
                            Alert.AlertType.ERROR
                    );
                }
                break;
        }
    }
}
