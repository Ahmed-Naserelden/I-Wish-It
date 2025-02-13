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
 * FXML Controller class
 *
 * @author Work
 */
public class FriendRequestsController extends MainController implements Initializable {

    @FXML
    private VBox cardContainer;
    @FXML
    private Label itemsCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        NetworkManager.send(new Request("GET", "/api/friends/request", DataStore.getMember().getId()));
        Response response = NetworkManager.receive();
        ArrayList<FriendPayload> friends = (ArrayList<FriendPayload>) response.getPayload();

        itemsCount.setText("You have (" + friends.size() + ") friend requests:");

        try {
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
