package client;

import javafx.application.Application;
import javafx.stage.Stage;
import connection.NetworkManager;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        SceneSwitcher.setPrimaryStage(stage);
        SceneSwitcher.switchScene("/scene/SignIn.fxml");
        NetworkManager.openConnection();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void logFail(String string) {
        System.out.println("[\u001B[31m\u001B[1mFAIL\u001B[0m] " + string);
    }

    public static void logPass(String string) {
        System.out.println("[\u001B[32m\u001B[1mPASS\u001B[0m] " + string);
    }

    public static void logInfo(String string) {
        System.out.println("[\u001B[34m\u001B[1mINFO\u001B[0m] " + string);
    }

    public static void logExit(String string) {
        System.out.println("[\u001B[35m\u001B[1mEXIT\u001B[0m] " + string);
    }
}
