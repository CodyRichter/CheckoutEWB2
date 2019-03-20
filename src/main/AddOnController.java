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
import main.ConcurrentManagement.GuestFile;
import main.FXMLAddOn.AddOnContainer;
import main.FXMLAddOn.AddOnItem;

import java.util.Arrays;

/**
 * CheckoutEWB2 - main
 *
 * @author AddOnController
 * @version 1.0
 */
public class AddOnController {

    public GuestFile selectedGuest;
    public Guest selectedGuestLoaded = GuestController.selectedGuestLoaded;

    public AddOnController() {
    }

    @FXML
    public void initialize() {

        Main.addOnController = this;

        selectedGuest = GuestController.selectedGuest;

        itemType.getItems().addAll(Arrays.asList(AddOnItem.class.getEnumConstants())); //Add All Enums in AddOnItem to The List
        if (itemType.getItems().size() > 0) { //Set Value in Drop-Down
            itemType.setValue(itemType.getItems().get(0));
        } else { //If Nothing To Load, Disable Drop-Down
            itemType.setDisable(true);
        }
        updateCost();
        for (AddOnContainer a : selectedGuestLoaded.getAddOnItems()) {
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
    public void updateCost() {
        itemPrice.setText("$"+itemType.getValue().getCost());
    }


    /**
     * Adds an item of the type noted in the selector to the selected guest's inventory
     * Will be triggered on button blick of "Add Item"
     */
    @FXML
    public void addItem() {
        AddOnContainer aoc = new AddOnContainer(itemType.getValue(),itemType.getValue().getCost(),description.getText(),selectedGuestLoaded);
        selectedGuestLoaded.addItem(aoc);
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
        selectedGuest.save(selectedGuestLoaded);

        try {
            Stage stage = (Stage) addItem.getScene().getWindow();
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            Parent root = FXMLLoader.load(getClass().getResource("Guest.fxml"));
            Scene scene = new Scene(root, 700, 600);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.setResizable(true);
            stage.setTitle("Checkout-EWB Version "+Main.VERSION+": Guest Page");
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
