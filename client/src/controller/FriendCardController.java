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

public class FriendCardController implements Initializable {

    @FXML
    private Label memberName;
    @FXML
    private Label memberDob;
    @FXML
    private Button actionButton;
    @FXML
    private Button showButton;
    @FXML
    private ImageView cardImage;

    private int friendId;
    private String cardType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

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
