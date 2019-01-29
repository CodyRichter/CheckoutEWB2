package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class ItemController {

    public Item selectedItem = null;

    public ItemController() {

    }

    /**
     * Will run when the page is loaded. Sets up formatting for general use later in program
     */
    @FXML
    public void initialize() {

        Main.itemController = this;

        if (!DataManager.hasLoadedData()) { //If Data from .csv file hasn't been loaded, load the data into the form
            DataManager.loadData();
        }
        if (!DataManager.items.isEmpty()) {
            itemSelect.setItems(DataManager.items); //Populate the selector with the loaded Items
            itemSelect.setValue(DataManager.items.get(0));
            selectedItem = itemSelect.getValue();
        }

        if (!DataManager.guests.isEmpty()) { //Ensure that the Item list has elements in it
            ownerSelect.setItems(DataManager.guests); //Populate the selector with the loaded Items
            ownerSelect.getItems().sort(Guest::compareTo);
        }
        updateForm(itemSelect.getValue()); //If this value is null, it will load a blank form.
    }

    @FXML
    public MenuItem save,load,saveAndExit,newItem,removeItem;

    @FXML
    public ComboBox<Item> itemSelect;

    @FXML
    public ComboBox<Guest> ownerSelect;

    @FXML
    public TextField itemName,itemPrice;

    @FXML
    public Button saveButton,removeOwner,switchButton;

    //
    // --------------------------------------
    // Menu Bar Interactions
    // --------------------------------------
    //

    /**
     * Saves current program data into .csv file
     */
    @FXML
    public void saveDataToFile() {
        if (itemSelect.getValue() != null)
            saveForm(); //Saves all fields in form to the item object
        DataManager.saveData();
    }

    /**
     * Loads data into program from previous .csv save file.
     */
    @FXML
    public void loadDataFromFile() {
        DataManager.loadData(); //Loads data from file
        if (DataManager.items.size() > 0) { //If there are any items loaded, set the page to the first one
            loadItemIntoForm(DataManager.items.get(0));
        }
    }

    /**
     * Saves program data and exits program after saving.
     */
    @FXML
    public void saveDataToFileAndExit() {
        saveDataToFile();
        exit();
    }

    /**
     * Sets everything in the current form to the specified item. This method is
     * used when loading in item data.
     * @param i Item's data to load.
     */
    public void loadItemIntoForm(Item i) {
        itemSelect.getItems().sorted();
        itemSelect.setValue(i);
        updateForm(i);
        saveDataToFile(); //Force save data to ensure headers are loaded into Hashmap when saving
    }


    /**
     * Creates a new Item object, adds it to the list of all Items, and sets
     * the current form to be that of the Item.
     */
    @FXML
    public void newItem() {
        Item i = new Item();

        DataManager.items.add(i); //Add guest to total guest list

        Set<Item> temp = new HashSet<>(DataManager.items);
        DataManager.items.clear();
        DataManager.items.addAll(temp);
        DataManager.items.sorted();

        itemSelect.setItems(DataManager.items);
        itemSelect.setValue(i);
    }

    /**
     * Removes a Item object from the list of loaded Items. If the Item to delete is
     * the currently loaded Item in the form, the form will be cleared and a blank page will be loaded.
     */
    @FXML
    public void removeItem() {
        if (itemSelect.getValue() == null) return; //If ItemSelect has no items selected, don't try to removePayment nothing
        itemSelect.getValue().free();
        DataManager.items.remove(itemSelect.getValue()); //Remove Item from master list
        itemSelect.getItems().remove(itemSelect.getValue()); //Remove Item from combo box
        if (!DataManager.items.isEmpty()) //Try to update next value to display
            itemSelect.setValue(DataManager.items.get(0));
        else itemSelect.getItems().clear();
        updateForm(itemSelect.getValue()); //Update fields in form
        removeItem.setDisable(true);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        removeItem.setDisable(false);
                    }}, 500);
    }

    /**
     * Exits program and closes all windows.
     * Note: This WILL NOT save any data loaded into the program at the
     * time of exiting.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }

    /**
     * Directs user to documentation page with instructions on program. Requires internet access for
     * the readme.md page to load.
     */
    @FXML
    public void showDocumentation() {
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
    public void showGuestFromSelector() {
        selectedItem = itemSelect.getValue();
        updateForm(selectedItem);
    }


    /**
     * Sets the owner of the current item to the guest that is selected in the ownerSelect combobox.
     */
    @FXML
    public void setItemOwner() {
        Guest g = ownerSelect.getValue();
        if (g == null || g.getNumber() == -1) return; //If it is a temporary guest, exit method
        g.addItem(itemSelect.getValue());
        ownerSelect.setDisable(true);
    }

    /**
     * Removes the owner of the current item and sets the currently loaded item
     * to have no owner.
     */
    @FXML
    public void removeItemOwner() {
        ownerSelect.getValue().removeItem(selectedItem);
        ownerSelect.setDisable(false);
        ownerSelect.setValue(new Guest(true,"No Owner"));


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
    public void updateForm(Item i) {
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
        removeOwner.setDisable(false);
        ownerSelect.setDisable(false);
        ownerSelect.setValue(new Guest (true,"No Owner"));
        for (Guest g : DataManager.guests) { //Loop through all loaded Items and see if the guest has the item
            if (g.getItems().contains(i)) {
                ownerSelect.setValue(g);
                ownerSelect.setDisable(true);
                break;
            }
        }
    }

    /**
     * Saves all values currently loaded into the form to the Item's data class file.
     * This method must run before exiting/loading new window, or all changes made will be lost
     * from lastSave->present.
     */
    @FXML
    public void saveForm() {
        Item i = itemSelect.getValue();
        i.add("itemName",itemName.getText()); //Puts Each TextField Into Item's HashMap
        i.add("itemPrice",itemPrice.getText()); //Puts Each TextField Into Item's HashMap

        itemSelect.getItems().sort(Item::compareTo);
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
