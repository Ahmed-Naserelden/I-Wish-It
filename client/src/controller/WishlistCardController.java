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

public class WishlistCardController implements Initializable {

    Runnable decrementCount;

    @FXML
    private AnchorPane parent;
    @FXML
    private ImageView cardImage;
    @FXML
    private Label cardName;
    @FXML
    private ProgressBar cardProgress;
    @FXML
    private Label cardPrice;
    @FXML
    private Label cardBrand;
    @FXML
    private Label cardDesc;
    @FXML
    private Button cardAction;
    @FXML
    private Label contributionText;

    private int productId;
    private int productPrice;
    private int contribution;
    private String cardType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

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

    @FXML
    private void onShowInfo(ActionEvent event) {
        System.out.println(">>> productId: " + productId);
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public void hide() {
        parent.setVisible(false);
        parent.setManaged(false);
    }

    public void setDecrementCount(Runnable function) {
        decrementCount = function;
    }
}
