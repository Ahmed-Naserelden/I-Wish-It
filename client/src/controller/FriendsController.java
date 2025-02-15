package controller;

import client.Client;
import client.DataStore;
import client.SceneSwitcher;
import connection.FriendActionPayload;
import connection.FriendPayload;
import connection.NetworkManager;
import connection.Request;
import connection.Response;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controller class for the friends view.
 * Handles the initialization and display of friends and adding new friends.
 */
public class FriendsController extends MainController implements Initializable {

    @FXML
    private VBox cardContainer; // Container for displaying friend cards
    @FXML
    private Label itemsCount; // Label for displaying the count of friends
    @FXML
    private TextField friendEmail; // TextField for entering a friend's email
    
    private int count;

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

        // Send request to get the friends list for the current member
        NetworkManager.send(new Request("GET", "/api/friends", DataStore.getMember().getId()));
        Response response = NetworkManager.receive();
        ArrayList<FriendPayload> friends = (ArrayList<FriendPayload>) response.getPayload();

        // Update the items count label with the number of friends
        setCount(friends.size());

        try {
            // Load and display each friend card
            for (FriendPayload friend : friends) {
                FXMLLoader loader = new FXMLLoader(
                        SceneSwitcher.class.getResource("/scene/FriendCard.fxml")
                );
                Parent card = loader.load();
                FriendCardController controller = loader.getController();
                controller.setDecrementCount(this::decrementCount);
                controller.setFriendData(friend);
                controller.setCardType("FRIEND");
                cardContainer.getChildren().add(card);
            }
        } catch (IOException e) {
            Client.logFail(e.getMessage());
        }
    }

    /**
     * Handles the event when the "Add Friend" button is clicked.
     * Sends a request to add a new friend and updates the view accordingly.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void onFriendAddClicked(ActionEvent event) {
        NetworkManager.send(
                new Request("POST", "/api/friends",
                        new FriendActionPayload(
                                DataStore.getMember().getId(),
                                friendEmail.getText()
                        )
                )
        );
        Response response = NetworkManager.receive();
        if (response.isPassed()) {
            MainController.showAlert(
                "Success",
                (String) response.getPayload(),
                Alert.AlertType.INFORMATION
            );
            friendEmail.clear();
        } else {
            MainController.showAlert(
                "Failed",
                (String) response.getPayload(),
                Alert.AlertType.ERROR
            );
        }
    }
        /**
     * Sets the count of friends and updates the friend text label.
     *
     * @param new_count The new count of wishlist items.
     */
    private void setCount(int new_count) {
        count = new_count;
        itemsCount.setText("You have (" + count + ") friend:");
    }

    /**
     * Decrements the count of friends by one.
     */
    public void decrementCount() {
        setCount(count - 1);
    }
}
