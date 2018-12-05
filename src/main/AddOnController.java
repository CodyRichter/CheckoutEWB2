package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.FXMLAddOn.AddOnContainer;
import main.FXMLAddOn.AddOnItem;

/**
 * CheckoutEWB2 - main
 *
 * @author AddOnController
 * @version 1.0
 */
public class AddOnController {

    private Guest selectedGuest = null;

    public AddOnController() {
    }

    @FXML
    private void initialize() {

        Main.addOnController = this;

        selectedGuest = Main.guestController.guestSelect.getValue();

        itemType.getItems().add(AddOnItem.SHIRT);
        itemType.getItems().add(AddOnItem.GLASS);
        itemType.setValue(AddOnItem.SHIRT);
        updateCost();
    }

    @FXML
    Button addItem;

    @FXML
    TextField description;

    @FXML
    ComboBox<AddOnItem> itemType;

    @FXML
    Label itemPrice;



    @FXML
    private void updateCost() {
        itemPrice.setText("$"+itemType.getValue().getCost());
    }



    @FXML
    private void addItem() {
        System.out.println("Selected Guest: " + selectedGuest.get("firstName"));
        AddOnContainer aoc = new AddOnContainer(itemType.getValue(),itemType.getValue().getCost(),description.getText(),selectedGuest);
        selectedGuest.addItem(aoc);
        description.clear();
        System.out.println("Added Item!: " + aoc.getItemType());
    }

    /**
     * Removes an add-on item from a guest's inventory, updating their payment information accordingly
     * @param g Guest who owns item
     * @param a Add-On container to remove
     */
    public void removeItem(Guest g, AddOnContainer a) {
        //TODO: Remove Item From VBox of Items
        g.removeItem(a);
    }


    /**
     * Exits the PaymentController and re-opens the GuestController for the current guest.
     */
    @FXML
    public void exit() {

        //TODO: Set Guest that is loaded in this PaymentController to be Guest that is loaded in GuestController.

        try {
            Stage stage = (Stage) addItem.getScene().getWindow();
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            Parent root = FXMLLoader.load(getClass().getResource("Guest.fxml"));
            Scene scene = new Scene(root, 700, 600);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.setResizable(true);
            stage.setTitle("Checkout-EWB Version II: Guest Page");
            stage.setScene(scene);
            stage.show();
            stage.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Page: Guest. (Attempted To Load From AddOnController)");
            System.out.println("Program Will Continue To Run To Allow Data Saving. Restart As Soon As Possible.");
        }

    }
}
