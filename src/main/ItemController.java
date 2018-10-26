package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URI;

public class ItemController {

    public ItemController() {

    }

    /**
     * Will run when the page is loaded. Sets up formatting for general use later in program
     */
    @FXML
    private void initialize() {

        if (!DataManager.hasLoadedData()) { //If Data from .csv file hasn't been loaded, load the data into the form
            DataManager.loadData();
        }
        if (DataManager.items.isEmpty()) { //Ensure that the Item list has elements in it
            //TODO: Display Alert To User That No Item Data To Load
        } else {
            itemSelect.setItems(DataManager.items); //Populate the selector with the loaded Items
            itemSelect.setValue(DataManager.items.get(0));
        }

        if (!DataManager.guests.isEmpty()) { //Ensure that the Item list has elements in it
            ownerSelect.setItems(DataManager.guests); //Populate the selector with the loaded Items
        }
        updateForm(itemSelect.getValue()); //If this value is null, it will load a blank form.
    }

    @FXML
    MenuItem save,load,saveAndExit;

    @FXML
    ComboBox<Item> itemSelect;

    @FXML
    ComboBox<Guest> ownerSelect;

    @FXML
    TextField itemName,itemPrice;

    @FXML
    Button saveButton,removeOwner,switchButton;

    //
    // --------------------------------------
    // Menu Bar Interactions
    // --------------------------------------
    //

    /**
     * Saves current program data into .csv file
     */
    @FXML
    private void saveData() {
        saveForm(); //Saves all fields in form to the item object
        DataManager.saveData();
    }

    /**
     * Loads data into program from previous .csv save file.
     */
    @FXML
    private void loadData() {
        DataManager.loadData(); //Loads data from file
        //TODO: Use code from init method to update form.
    }

    /**
     * Saves program data and exits program after saving.
     */
    @FXML
    private void saveDataAndExit() {
        saveData();
        exit();
    }

    /**
     * Creates a new Item object, adds it to the list of all Items, and sets
     * the current form to be that of the Item.
     */
    @FXML
    private void newItem() {
        Item i = new Item();
        DataManager.items.add(i); //Add Item to total Item list
        itemSelect.getItems().add(i);
        itemSelect.getItems().sorted();
        itemSelect.setValue(i);
        //TODO: Sort Item Based On ##
        //TODO: Update Combo Box with new name if changed after saving!!
    }

    /**
     * Removes a Item object from the list of loaded Items. If the Item to delete is
     * the currently loaded Item in the form, the form will be cleared and a blank page will be loaded.
     */
    @FXML
    private void removeItem() {
        if (itemSelect.getValue() == null) return; //If ItemSelect has no items selected, don't try to remove nothing
        DataManager.items.remove(itemSelect.getValue()); //Remove Item from master list
        itemSelect.getItems().remove(itemSelect.getValue()); //Remove Item from combo box
        if (!DataManager.items.isEmpty()) //Try to update next value to display
            itemSelect.setValue(DataManager.items.get(0));
        updateForm(itemSelect.getValue()); //Update fields in form
    }

    /**
     * Exits program and closes all windows.
     * Note: This WILL NOT save any data loaded into the program at the
     * time of exiting.
     */
    @FXML
    private void exit() {
        System.exit(0);
    }

    /**
     * Directs user to documentation page with instructions on program. Requires internet access for
     * the readme.md page to load.
     */
    @FXML
    private void showDocumentation() {
        try {
            java.awt.Desktop.getDesktop().browse(new URI("https://github.com/Senarii/CheckoutEWB/blob/master/readme.md"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error In Method: showDocumentation(). Unable to load Github readme.md page");
        }
    }

    /**
     * Loads the Item that has been clicked on in the Item Selector combobox
     * into the form, and populates all of the fields accordingly.
     */
    @FXML
    private void showGuestFromSelector() {
        updateForm(itemSelect.getValue());
    }


    /**
     * Sets the owner of the current item to the guest that is selected in the ownerSelect combobox.
     */
    @FXML
    private void setItemOwner() {
        Guest g = ownerSelect.getValue();
        g.addItem(itemSelect.getValue());
    }

    /**
     * Removes the owner of the current item and sets the currently loaded item
     * to have no owner.
     */
    @FXML
    private void removeItemOwner() {
        if (ownerSelect.getValue().get("firstName").equals("No Owner")) return; //Ensure not removing a non-existant owner
        for (int k = 0; k < DataManager.guests.size(); k++) { //Loop through all loaded Items and see if the
            Guest g = DataManager.guests.get(k);
            if (g.get("items").contains(itemSelect.getValue().get("itemName"))) {
                g.removeItem(itemSelect.getValue());
            }
        }
        ownerSelect.setValue(new Guest (true,"No Owner"));
    }

    //
    // --------------------------------------
    // Utility Methods
    // --------------------------------------
    //

    /**
     * Updates all fields in form to match the data of the current item.
     * If the value passed to this method is null, the form will be wiped.
     * @param i Item's data to load. (null to clear form)
     */
    private void updateForm(Item i) {
        if (i == null) { //If the item that is being loaded is null, clear the form and reset it to default state
            itemName.setText("");
            itemName.setDisable(true);
            itemPrice.setText("");
            itemPrice.setDisable(true);
            saveButton.setDisable(true);
            ownerSelect.setDisable(true);
            ownerSelect.setValue(new Guest (true,"No Owner"));
            removeOwner.setDisable(true);
            return;
        }

        itemName.setText(i.get("itemName"));
        itemName.setDisable(false);
        itemPrice.setText(i.get("itemPrice"));
        itemPrice.setDisable(false);
        saveButton.setDisable(false);
        ownerSelect.setDisable(false);
        removeOwner.setDisable(false);
        ownerSelect.setValue(new Guest (true,"No Owner"));
        for (int k = 0; k < DataManager.guests.size(); k++) { //Loop through all loaded Items and see if the
            Guest g = DataManager.guests.get(k);
            if (g.getItems().contains(i)) {
                ownerSelect.setValue(g);
            }
        }
    }

    /**
     * Saves all values currently loaded into the form to the Item's data class file.
     * This method must run before exiting/loading new window, or all changes made will be lost
     * from lastSave->present.
     */
    @FXML
    private void saveForm() {
        Item i = itemSelect.getValue();
        i.add("itemName",itemName.getText()); //Puts Each TextField Into Item's HashMap
        i.add("itemPrice",itemPrice.getText()); //Puts Each TextField Into Item's HashMap
        if (!ownerSelect.getValue().get("firstName").equals("No Owner")) { //Ensure owner exists
            ownerSelect.getValue().addItem(i);
        }

        //This last few lines of code ensure the combobox displays the correct value.
        itemSelect.getItems().remove(i);
        itemSelect.getItems().add(i);
        itemSelect.getItems().sorted();
        itemSelect.setValue(i);
    }

    /**
     * Loads the FXML page for Guests, and switches the current window to that.
     */
    @FXML
    public void changePages() {
        try {
            Stage stage = (Stage) itemName.getScene().getWindow();
            stage.setMinHeight(600);
            stage.setMinWidth(700);
            Parent root = FXMLLoader.load(getClass().getResource("Guest.fxml"));
            Scene scene = new Scene(root, 700, 600);
            stage.setHeight(600);
            stage.setWidth(700);
            stage.setTitle("Checkout-EWB Version II: Guest Page");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Page: Guest.fxml.");
            System.out.println("Program Will Continue To Run To Allow Data Saving. Restart As Soon As Possible.");
        }
    }

}
