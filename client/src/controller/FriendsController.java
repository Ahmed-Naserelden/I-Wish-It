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

public class FriendsController extends MainController implements Initializable {

    @FXML
    private VBox cardContainer;
    @FXML
    private Label itemsCount;
    @FXML
    private TextField friendEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        NetworkManager.send(new Request("GET", "/api/friends", DataStore.getMember().getId()));
        Response response = NetworkManager.receive();
        ArrayList<FriendPayload> friends = (ArrayList<FriendPayload>) response.getPayload();

        itemsCount.setText("You have (" + friends.size() + ") friends:");

        try {
            for (FriendPayload friend : friends) {
                FXMLLoader loader = new FXMLLoader(
                        SceneSwitcher.class.getResource("/scene/FriendCard.fxml")
                );
                Parent card = loader.load();
                FriendCardController controller = loader.getController();
                controller.setFriendData(friend);
                controller.setCardType("FRIEND");
                cardContainer.getChildren().add(card);
            }
        } catch (IOException e) {
            Client.logFail(e.getMessage());
        }
    }

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
            SceneSwitcher.switchScene("/scene/Friends.fxml");
        } else {
            MainController.showAlert(
                "Failed",
                (String) response.getPayload(),
                Alert.AlertType.ERROR
            );
        }
    }
}
