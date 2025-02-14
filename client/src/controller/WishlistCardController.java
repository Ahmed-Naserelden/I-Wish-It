package controller;

import client.DataStore;
import client.SceneSwitcher;
import connection.NetworkManager;
import connection.Request;
import connection.Response;
import connection.WishlistPayload;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import connection.ContributionPayload;
import connection.ProductPayload;

/**
 * Controller class for the wishlist card view.
 * Handles the display and actions for individual wishlist cards.
 */
public class WishlistCardController implements Initializable {

    Runnable decrementCount;

    @FXML
    private AnchorPane parent; // Parent container for the card
    @FXML
    private ImageView cardImage; // ImageView for displaying the product image
    @FXML
    private Label cardName; // Label for displaying the product name
    @FXML
    private ProgressBar cardProgress; // ProgressBar for displaying the contribution progress
    @FXML
    private Label cardPrice; // Label for displaying the product price
    @FXML
    private Label cardBrand; // Label for displaying the product brand
    @FXML
    private Label cardDesc; // Label for displaying the product description
    @FXML
    private Button cardAction; // Button for performing actions (e.g., Add, Remove, Contribute)
    @FXML
    private Label contributionText; // Label for displaying the contribution text

    private int productId; // ID of the product
    private int productPrice; // Price of the product
    private int contribution; // Contribution amount
    private String cardType; // Type of the card (e.g., WISH, SKIP, SANTA)

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
     * Sets the type of the card and updates the UI accordingly.
     *
     * @param type The type of the card (e.g., WISH, SKIP, SANTA).
     */
    public void setCardType(String type) {
        this.cardType = type;

        switch (type) {
            case "WISH":
                cardAction.setStyle("-fx-background-radius: 30; -fx-text-fill: red;");
                cardAction.setText("Remove from Wishlist");
                contributionText.setVisible(true);
                contributionText.setManaged(true);
                cardProgress.setVisible(true);
                cardProgress.setManaged(true);
                break;
            case "SKIP":
                cardAction.setStyle("-fx-background-radius: 30; -fx-text-fill: green;");
                cardAction.setText("Add to Wishlist");
                contributionText.setVisible(false);
                contributionText.setManaged(false);
                cardProgress.setVisible(false);
                cardProgress.setManaged(false);
                break;
            case "SANTA":
                cardAction.setStyle("-fx-background-radius: 30; -fx-text-fill: green;");
                cardAction.setText("Contribute");
                break;
        }

        updateContribution();
    }

    /**
     * Updates the contribution progress and UI elements.
     */
    private void updateContribution() {
        NumberFormat formatter = NumberFormat.getInstance();
        String price = formatter.format(productPrice);
        if (cardType.equals("WISH") || cardType.equals("SANTA")) {
            String contributed = formatter.format(contribution);
            cardPrice.setText(contributed + "/" + price + " Wi");
        } else {
            cardPrice.setText(price + " Wi");
        }

        cardProgress.setProgress(((double) contribution / productPrice));

        if (contribution == productPrice) {
            if (cardType.equals("SANTA")) {
                cardAction.setDisable(true);
                cardAction.setText("Completed");
            } else {
                cardAction.setText("Get your wish ❤");
                cardAction.setStyle("-fx-background-radius: 30; -fx-text-fill: white; -fx-background-color: #0088C8;");
            }
        }
    }

    /**
     * Sets the product data and updates the UI with the product's information.
     *
     * @param product The ProductPayload object containing the product's information.
     */
    public void setProductData(ProductPayload product) {
        productId = product.getProductID();
        productPrice = product.getPrice();
        cardName.setText(product.getName());
        cardBrand.setText(product.getBrand());
        cardDesc.setText(product.getDesc());
        Image image = DataStore.getProductImage(product.getImage());
        cardImage.setImage(image);
        contribution = product.getContribution();
    }

    /**
     * Adds the item to the wishlist.
     */
    private void AddItemAction() {
        WishlistPayload wishlistPayload = new WishlistPayload(DataStore.getMember().getId(), productId);
        NetworkManager.send(new Request("POST", "/api/wishlist", wishlistPayload));
        Response response = NetworkManager.receive();
        if (response.isPassed()) {
            setCardType("WISH");
            ((MainController) SceneSwitcher.getController()).refreshBalance();
        } else {
            MainController.showAlert(
                    "Failed",
                    "Cannot do this action!",
                    Alert.AlertType.ERROR
            );
        }
    }

    /**
     * Removes the item from the wishlist.
     */
    private void removeItemAction() {
        WishlistPayload wishlistPayload = new WishlistPayload(DataStore.getMember().getId(), productId);
        NetworkManager.send(new Request("DELETE", "/api/wishlist", wishlistPayload));
        Response response = NetworkManager.receive();
        if (response.isPassed()) {
            contribution = 0;
            setCardType("SKIP");
            ((MainController) SceneSwitcher.getController()).refreshBalance();
            if (!DataStore.isInsideMarketPlace()) {
                decrementCount.run();
                hide();
            }
        } else {
            MainController.showAlert("Failed", "Cannot do this action!", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles the contribution action.
     */
    private void contributeAction() {
        int fromMemberId = DataStore.getMember().getId();
        int toMemberId = DataStore.getEffectiveMemberId();
        int amount = MainController.showAmountDialog("Contribution", "Enter an amount:");
        if (amount == -1) {
            return;
        }
        Request request = new Request("POST", "/api/contribute", new ContributionPayload(fromMemberId, toMemberId, productId, amount));
        NetworkManager.send(request);
        Response response = NetworkManager.receive();
        if (response.isPassed()) {
            ((MainController) SceneSwitcher.getController()).refreshBalance();
            contribution += (int) response.getPayload();
            updateContribution();
        } else {
            MainController.showAlert("Failed",
                    (String) response.getPayload(),
                    Alert.AlertType.ERROR
            );
        }
    }

    /**
     * Handles the action button click event.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void onAction(ActionEvent event) {
        switch (this.cardType) {
            case "WISH":
                removeItemAction();
                break;
            case "SKIP":
                AddItemAction();
                break;
            case "SANTA":
                contributeAction();
                break;
        }

        updateContribution();
    }

    /**
     * Handles the show info button click event.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void onShowInfo(ActionEvent event) {
        System.out.println(">>> productId: " + productId);
    }

    /**
     * Sets the contribution amount.
     *
     * @param contribution The contribution amount.
     */
    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    /**
     * Hides the card.
     */
    public void hide() {
        parent.setVisible(false);
        parent.setManaged(false);
    }

    /**
     * Sets the decrement count function.
     *
     * @param function The function to decrement the count.
     */
    public void setDecrementCount(Runnable function) {
        decrementCount = function;
    }
}
