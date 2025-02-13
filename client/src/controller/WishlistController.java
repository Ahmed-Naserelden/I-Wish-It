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

public class WishlistController extends MainController implements Initializable {

    @FXML
    private VBox cardContainer;
    @FXML
    private Label wishlistDob;
    @FXML
    private Label wishlistText;

    private String name;
    private int count;

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

    private void setCount(int new_count) {
        count = new_count;
        wishlistText.setText(name + " has (" + count + ") items in their wishlist");
    }

    public void decrementCount() {
        setCount(count - 1);
    }

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
