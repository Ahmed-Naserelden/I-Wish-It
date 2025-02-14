/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import client.Client;
import client.DataStore;
import client.SceneSwitcher;
import connection.FriendPayload;
import connection.NetworkManager;
import connection.Request;
import connection.Response;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controller class for the friend requests view.
 * Handles the initialization and display of friend requests.
 */
public class FriendRequestsController extends MainController implements Initializable {

    @FXML
    private VBox cardContainer; // Container for displaying friend request cards
    @FXML
    private Label itemsCount; // Label for displaying the count of friend requests

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

        // Send request to get the friend requests for the current member
        NetworkManager.send(new Request("GET", "/api/friends/request", DataStore.getMember().getId()));
        Response response = NetworkManager.receive();
        ArrayList<FriendPayload> friends = (ArrayList<FriendPayload>) response.getPayload();

        // Update the items count label with the number of friend requests
        itemsCount.setText("You have (" + friends.size() + ") friend requests:");

        try {
            // Load and display each friend request card
            for (FriendPayload friend : friends) {
                FXMLLoader loader = new FXMLLoader(
                        SceneSwitcher.class.getResource("/scene/FriendCard.fxml")
                );
                Parent card = loader.load();
                FriendCardController controller = loader.getController();
                controller.setFriendData(friend);
                controller.setCardType("REQUEST");
                cardContainer.getChildren().add(card);
            }
        } catch (IOException e) {
            Client.logFail(e.getMessage());
        }
    }
}
