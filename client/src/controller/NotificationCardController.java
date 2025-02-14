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

/**
 * Controller class for the notification card view.
 * Handles the display and actions for individual notification cards.
 */
public class NotificationCardController implements Initializable {

    @FXML
    private AnchorPane body; // Container for the notification card
    @FXML
    private Label uTitle; // Label for displaying the notification title
    @FXML
    private Label uTimestamp; // Label for displaying the notification timestamp
    @FXML
    private ImageView uImage; // ImageView for displaying the notification image
    @FXML
    private Button uAction; // Button for performing actions on the notification

    private Runnable decrementCount; // Function to decrement the notification count
    private int notificationId; // ID of the notification

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
     * Handles the action button click event.
     * Sends a request to delete the notification and updates the view accordingly.
     *
     * @param event The action event triggered by clicking the button.
     */
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

    /**
     * Sets the data for the notification card and updates the UI.
     *
     * @param payload The NotificationPayload object containing the notification data.
     */
    public void setData(NotificationPayload payload) {
        notificationId = payload.getId();
        uTitle.setText(payload.getDesc());
        uTimestamp.setText(payload.getTimestamp().toString());
    }

    /**
     * Sets the decrement count function.
     *
     * @param function The function to decrement the count.
     */
    public void setDecrementCount(Runnable function) {
        decrementCount = function;
    }

    /**
     * Hides the notification card.
     */
    public void hide() {
        body.setVisible(false);
        body.setManaged(false);
    }
}
