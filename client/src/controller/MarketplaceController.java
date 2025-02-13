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

public class MarketplaceController extends MainController implements Initializable {

    @FXML
    private VBox cardContainer;
    @FXML
    private Label itemsCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        NetworkManager.send(new Request("GET", "/api/wishlist", DataStore.getMember().getId()));
        Response wishlistReponse = NetworkManager.receive();

        Map<Integer, Integer> wishlistProducts = new HashMap<>();

        for (ProductPayload item : (ArrayList<ProductPayload>) wishlistReponse.getPayload()) {
            wishlistProducts.put(item.getProductID(), item.getContribution());
        }

        NetworkManager.send(new Request("GET", "/api/marketplace"));
        Response marketplaceResponse = NetworkManager.receive();
        ArrayList<ProductPayload> products = (ArrayList<ProductPayload>) marketplaceResponse.getPayload();

        itemsCount.setText("Available items (" + products.size() + ") in the marketplace:");

        try {
            for (ProductPayload product : products) {
                FXMLLoader loader = new FXMLLoader(
                        SceneSwitcher.class.getResource("/scene/WishlistCard.fxml")
                );
                Parent card = loader.load();
                WishlistCardController controller = loader.getController();
                controller.setProductData(product);

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
