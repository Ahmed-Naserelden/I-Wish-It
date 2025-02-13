package controller;

import connection.NetworkManager;
import connection.NotificationPayload;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class NotificationCardController implements Initializable {

    @FXML
    private AnchorPane body;
    @FXML
    private Label uTitle;
    @FXML
    private Label uTimestamp;
    @FXML
    private ImageView uImage;
    @FXML
    private Button uAction;

    private Runnable decrementCount;
    private int notificationId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onActionClicked(ActionEvent event) {
        NetworkManager.send(new Request("DELETE", "/api/notification", notificationId));
        Response response = NetworkManager.receive();
        if (response.isPassed()) {
            hide();
            decrementCount.run();
        } else {
            MainController.showAlert(
                    "Failed",
                    "Cannot do this action!",
                    Alert.AlertType.ERROR
            );
        }
    }

    public void setData(NotificationPayload payload) {
        notificationId = payload.getId();
        uTitle.setText(payload.getDesc());
        uTimestamp.setText(payload.getTimestamp().toString());
    }

    public void setDecrementCount(Runnable function) {
        decrementCount = function;
    }

    public void hide() {
        body.setVisible(false);
        body.setManaged(false);
    }
}
