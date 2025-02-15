package controller;

import client.Client;
import client.DataStore;
import client.SceneSwitcher;
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
import connection.ProductPayload;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for the marketplace view.
 * Handles the initialization and display of marketplace items and wishlist items.
 */
public class MarketplaceController extends MainController implements Initializable {

    @FXML
    private VBox cardContainer; // Container for displaying product cards
    @FXML
    private Label itemsCount; // Label for displaying the count of available items

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

        // Send request to get the wishlist items for the current member
        NetworkManager.send(new Request("GET", "/api/wishlist", DataStore.getMember().getId()));
        Response wishlistResponse = NetworkManager.receive();

        // Map to store wishlist products and their contributions
        Map<Integer, Integer> wishlistProducts = new HashMap<>();

        // Populate the wishlist products map from the response payload
        for (ProductPayload item : (ArrayList<ProductPayload>) wishlistResponse.getPayload()) {
            wishlistProducts.put(item.getProductID(), item.getContribution());
        }

        // Send request to get the marketplace items
        NetworkManager.send(new Request("GET", "/api/marketplace"));
        Response marketplaceResponse = NetworkManager.receive();
        ArrayList<ProductPayload> products = (ArrayList<ProductPayload>) marketplaceResponse.getPayload();

        // Update the items count label with the number of available items
        itemsCount.setText("Available items (" + products.size() + ") in the marketplace:");

        try {
            // Load and display each product card
            for (ProductPayload product : products) {
                FXMLLoader loader = new FXMLLoader(
                        SceneSwitcher.class.getResource("/scene/WishlistCard.fxml")
                );
                Parent card = loader.load();
                WishlistCardController controller = loader.getController();
                controller.setProductData(product);

                // Set the card type based on whether the product is in the wishlist
                if (wishlistProducts.containsKey(product.getProductID())) {
                    controller.setContribution(wishlistProducts.get(product.getProductID()));
                    controller.setCardType("WISH");
                } else {
                    controller.setCardType("SKIP");
                }
                cardContainer.getChildren().add(card);
            }
        } catch (IOException e) {
            Client.logFail(e.getMessage());
        }
    }
}
