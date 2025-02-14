package controller;

import client.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import client.DataStore;
import client.SceneSwitcher;
import connection.NetworkManager;
import connection.Request;
import connection.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import connection.ProductPayload;

/**
 * Controller class for the wishlist view.
 * Handles the initialization and display of wishlist items.
 */
public class WishlistController extends MainController implements Initializable {

    @FXML
    private VBox cardContainer; // Container for displaying wishlist cards
    @FXML
    private Label wishlistDob; // Label for displaying the member's date of birth
    @FXML
    private Label wishlistText; // Label for displaying the wishlist text

    private String name; // Name of the member
    private int count; // Count of wishlist items

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

        NetworkManager.send(new Request("GET", "/api/wishlist", DataStore.getEffectiveMemberId()));
        Response response = NetworkManager.receive();
        ArrayList<ProductPayload> products = (ArrayList<ProductPayload>) response.getPayload();

        name = DataStore.getEffectiveMemberName();
        setCount(products.size());

        String dob = DataStore.getEffectiveMemberDob();
        int remainingDays = daysUntilBirthday(dob);
        wishlistDob.setText("Birthday: " + dob + " (" + remainingDays + " days remaining)");

        boolean is_user = DataStore.getEffectiveMemberId() == DataStore.getMember().getId();
        String cardType = is_user ? "WISH" : "SANTA";
        try {
            for (ProductPayload product : products) {
                FXMLLoader loader = new FXMLLoader(
                        SceneSwitcher.class.getResource("/scene/WishlistCard.fxml")
                );
                Parent card = loader.load();
                WishlistCardController controller = loader.getController();
                controller.setDecrementCount(this::decrementCount);
                controller.setProductData(product);
                controller.setCardType(cardType);
                cardContainer.getChildren().add(card);
            }
        } catch (IOException e) {
            Client.logFail(e.getMessage());
        }
    }

    /**
     * Sets the count of wishlist items and updates the wishlist text label.
     *
     * @param new_count The new count of wishlist items.
     */
    private void setCount(int new_count) {
        count = new_count;
        wishlistText.setText(name + " has (" + count + ") items in their wishlist");
    }

    /**
     * Decrements the count of wishlist items by one.
     */
    public void decrementCount() {
        setCount(count - 1);
    }

    /**
     * Calculates the number of days until the next birthday.
     *
     * @param isoDate The date of birth in ISO format (e.g., "2000-02-15").
     * @return The number of days until the next birthday.
     */
    public static int daysUntilBirthday(String isoDate) {
        // Parse the ISO date string (e.g., "2000-02-15")
        LocalDate birthDate = LocalDate.parse(isoDate, DateTimeFormatter.ISO_LOCAL_DATE);

        // Get today's date
        LocalDate today = LocalDate.now();

        // Get the birthday for this year
        LocalDate nextBirthday = birthDate.withYear(today.getYear());

        // If the birthday has already passed this year, use next year
        if (!nextBirthday.isAfter(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        // Calculate days until next birthday
        return (int) ChronoUnit.DAYS.between(today, nextBirthday);
    }
}
