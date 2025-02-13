package controller;

import client.Client;
import client.DataStore;
import client.SceneSwitcher;
import connection.NetworkManager;
import connection.NotificationPayload;
import connection.ProductPayload;
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

public class NotificationsController extends MainController implements Initializable {

    @FXML
    private VBox container;
    @FXML
    private Label text;

    private int count = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        NetworkManager.send(new Request("GET", "/api/notification", DataStore.getMember().getId()));
        Response response = NetworkManager.receive();

        ArrayList<NotificationPayload> notifications = (ArrayList<NotificationPayload>) response.getPayload();
        setCount(notifications.size());

        try {
            for (NotificationPayload notification : notifications) {
                FXMLLoader loader = new FXMLLoader(
                        SceneSwitcher.class.getResource("/scene/NotificationCard.fxml")
                );
                Parent card = loader.load();
                NotificationCardController controller = loader.getController();
                controller.setDecrementCount(this::decrementCount);
                controller.setData(notification);
                container.getChildren().add(card);
            }
        } catch (IOException e) {
            Client.logFail(e.getMessage());
        }
    }

    private void setCount(int new_count) {
        count = new_count;
        text.setText("You have (" + count + ") notifications:");
    }

    public void decrementCount() {
        setCount(count - 1);
    }
}
