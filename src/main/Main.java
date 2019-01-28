package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static boolean DEBUG = true;
    public static GuestController guestController;
    public static ItemController itemController;
    public static PaymentController paymentController;
    public static AddOnController addOnController;



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Guest.fxml"));
        primaryStage.setTitle("Checkout-EWB Version II: Guest Page");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(700);
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
    }

    /**
     * Main Method to Run in order to launch program
     */
    public static void main(String[] args) {
        launch(args);
    }
}
