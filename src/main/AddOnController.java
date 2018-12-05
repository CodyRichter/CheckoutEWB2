package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
        for (AddOnContainer a : selectedGuest.getAddOnItems()) {
            itemList.getChildren().add(a);
        }
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
    VBox itemList;

    @FXML
    private void updateCost() {
        itemPrice.setText("$"+itemType.getValue().getCost());
    }


    /**
     * Adds an item of the type noted in the selector to the selected guest's inventory
     * Will be triggered on button blick of "Add Item"
     */
    @FXML
    private void addItem() {
        AddOnContainer aoc = new AddOnContainer(itemType.getValue(),itemType.getValue().getCost(),description.getText(),selectedGuest);
        selectedGuest.addItem(aoc);
        description.clear();
        itemList.getChildren().add(aoc);
    }

    /**
     * Removes an add-on item from a guest's inventory, updating their payment information accordingly
     * @param g Guest who owns item
     * @param a Add-On container to remove
     */
    public void removeItem(Guest g, AddOnContainer a) {
        g.removeItem(a);
        itemList.getChildren().remove(a);
    }


    /**
     * Exits the PaymentController and re-opens the GuestController for the current guest.
     */
    @FXML
    public void exit() {

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

            Main.guestController.loadGuestIntoForm(selectedGuest);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Page: Guest. (Attempted To Load From AddOnController)");
            System.out.println("Program Will Continue To Run To Allow Data Saving. Restart As Soon As Possible.");
        }

    }
}
