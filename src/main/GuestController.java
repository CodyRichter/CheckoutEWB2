package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.FXMLAddOn.AddOnContainer;
import main.FXMLAddOn.AddOnItem;

import java.net.URI;
import java.util.*;

public class GuestController {

    protected HashMap<String, TextField> textFields; //Hashmap containing all textfields in program

    public GuestController() {

    }

    /**
     * Will run when the page is loaded. Sets up formatting for general use later in program
     */
    @FXML
    protected void initialize() {
        Main.guestController = this;
        textFields = new HashMap<>();
        textFields.put("firstName", firstName);
        textFields.put("lastName", lastName);
        textFields.put("phoneNumber", phoneNumber);
        textFields.put("emailAddress", emailAddress);

        if (!DataManager.hasLoadedData()) { //If Data from .csv file hasn't been loaded, load the data into the form
            DataManager.loadData();
        }
        if (!DataManager.guests.isEmpty()) { //Ensure that the guest list has elements in it
            guestSelect.setItems(DataManager.guests); //Populate the selector with the loaded guests
            guestSelect.setValue(DataManager.guests.get(0));
        }
        updateForm(guestSelect.getValue()); //If this value is null, it will load a blank form.

    }

    @FXML
    MenuItem save, load, saveAndExit, newGuest, removeGuest;

    @FXML
    ComboBox<Guest> guestSelect;

    @FXML
    TextField firstName, lastName, phoneNumber, emailAddress;

    @FXML
    Button manageAddOns, saveButton, switchButton, managePayments;

    @FXML
    VBox itemList;

    //
    // --------------------------------------
    // Menu Bar Interactions
    // --------------------------------------
    //

    /**
     * Saves current program data into .csv file
     */
    @FXML
    protected void saveDataToFile() {
        if (guestSelect.getValue() != null)
            saveForm(); //Saves all fields in form to the guest object
        DataManager.saveData();
    }

    /**
     * Loads data into program from previous .csv save file.
     */
    @FXML
    protected void loadDataFromFile() {
        DataManager.loadData(); //Loads data from file
        if (DataManager.guests.size() > 0) { //If there are any guests loaded, set the page to the first one
            loadGuestIntoForm(DataManager.guests.get(0));
        }
    }

    /**
     * Saves program data and exits program after saving.
     */
    @FXML
    protected void saveDataToFileAndExit() {
        saveDataToFile();
        exit();
    }

    /**
     * Sets everything in the current form to the specified guest. This method is
     * used when reloading the guest selector from another page.
     * @param g Guest's data to load.
     */
    public void loadGuestIntoForm(Guest g) {
        guestSelect.getItems().sorted();
        guestSelect.setValue(g);
        updateForm(g);
    }


    /**
     * Creates a new guest object, adds it to the list of all guests, and sets
     * the current form to be that of the guest.
     */
    @FXML
    protected void createNewGuest() {
        Guest g = new Guest();

        DataManager.guests.add(g); //Add guest to total guest list

        Set<Guest> temp = new HashSet<>(DataManager.guests);
        DataManager.guests.clear();
        DataManager.guests.addAll(temp);
        DataManager.guests.sorted();

        guestSelect.setItems(DataManager.guests);
        guestSelect.setValue(g);
    }

    /**
     * Removes a guest object from the list of loaded guests. If the guest to delete is
     * the currently loaded guest in the form, the form will be cleared and a blank page will be loaded.
     */
    @FXML
    protected void removeGuest() {
        Guest g = guestSelect.getValue();
        if (g == null) return; //If guestSelect has no items selected, don't try to removePayment nothing
        g.free();
        DataManager.guests.remove(g); //Remove guest from master list
        guestSelect.getItems().remove(g); //Remove guest from combo box
        if (!DataManager.guests.isEmpty()) //Try to update next value to display
            guestSelect.setValue(DataManager.guests.get(0));
        else guestSelect.getItems().clear();
        updateForm(guestSelect.getValue()); //Update fields in form
        removeGuest.setDisable(true);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        removeGuest.setDisable(false);
                    }}, 500);
    }

    /**
     * Exits program and closes all windows.
     * Note: This WILL NOT save any data loaded into the program at the
     * time of exiting.
     */
    @FXML
    protected void exit() {
        System.exit(0);
    }

    /**
     * Directs user to documentation page with instructions on program. Requires internet access for
     * the readme.md page to load.
     */
    @FXML
    protected void showDocumentation() {
        try {
            java.awt.Desktop.getDesktop().browse(new URI("https://github.com/Senarii/CheckoutEWB/blob/master/readme.md"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error In Method: showDocumentation(). Unable to load Github readme.md page");
        }
    }

    /**
     * Loads the guest that has been clicked on in the Guest Selector combobox
     * into the form, and populates all of the fields accordingly.
     */
    @FXML
    protected void showGuestFromSelector() {
        updateForm(guestSelect.getValue());
    }

    //
    // --------------------------------------
    // Item Management
    // --------------------------------------
    //

    /**
     * Opens new FXML menu window for add-ons to Guest's Auction Visit.
     * Some possible items may be: T-Shirt, Glass, etc... This method will support all wildcard
     * entries that may not be classified elsewhere.
     */
    @FXML
    protected void openAddOnMenu() {

        saveForm();

        try {
            Stage stage = (Stage) managePayments.getScene().getWindow();
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            Parent root = FXMLLoader.load(getClass().getResource("AddOns.fxml"));
            Scene scene = new Scene(root, 600, 400);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.setTitle("Checkout-EWB Version II: Add-On Item Manager");
            stage.setScene(scene);
            stage.show();
            stage.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Page: Add-On Item Manager.");
            System.out.println("Program Will Continue To Run To Allow Data Saving. Restart As Soon As Possible.");
        }
    }

    //
    // --------------------------------------
    // Payment
    // --------------------------------------
    //

    /**
     * Opens a new FXML window for the user to enter/manage their payments. Note that calling this method
     * will save the current data entered in the form before changing the windows.
     */
    @FXML
    protected void openPaymentWindow() {

        saveForm();

        try {
            Stage stage = (Stage) managePayments.getScene().getWindow();
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            Parent root = FXMLLoader.load(getClass().getResource("Total.fxml"));
            Scene scene = new Scene(root, 600, 400);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.setTitle("Checkout-EWB Version II: Payment Manager");
            stage.setScene(scene);
            stage.show();
            stage.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Page: Payment Manager.");
            System.out.println("Program Will Continue To Run To Allow Data Saving. Restart As Soon As Possible.");
        }
    }


    //
    // --------------------------------------
    // Utility Methods
    // --------------------------------------
    //

    /**
     * Given a TextField or Label, will parse the contents of it to a double. If the number is can not be parsed
     * then will return 0. Will also return 0 if input field is empty.
     * @param o Container holding the field to parse (Eg. TextField, Label)
     * @return Value of double in container, 0 if can't be read, 0 if empty.
     */
    public static double parseInputToDouble(Object o) {
        if (!(o instanceof TextField) && !(o instanceof Label)) return 0;

        Label l = null;
        TextField t = null;

        if (o instanceof TextField) t = (TextField) o;
        if (o instanceof Label) l = (Label) o;

        try {
            //If the textfield isn't empty, get the value in it
            if (t != null && !t.getText().equals("")) return Double.parseDouble(t.getText());
            else if (l != null && !l.getText().equals("")) return Double.parseDouble(l.getText());
        } catch (Exception e) {
            if (Main.DEBUG) e.printStackTrace();
        }
        return 0;
    }


    /**
     * Updates all fields in form to match the data of the current guest.
     * If the value passed to this method is null, the form will be wiped.
     *
     * @param g Guest's data to load. (null to clear form)
     */
    protected void updateForm(Guest g) {
        itemList.getChildren().clear();
        if (g == null) { //If the guest that is being loaded is null, clear the form and reset it to default state
            for (TextField t : textFields.values()) { //Iterate through all textFields and wipe them
                t.setText("");
                t.setDisable(true);
            }
            manageAddOns.setDisable(true);
            managePayments.setDisable(true);
            saveButton.setDisable(true);
            return;
        }

        for (String s : textFields.keySet()) {
            TextField t = textFields.get(s); //Load desired TextField from HashMap
            t.setDisable(false);
            t.setText(g.get(s)); //Set Value of TextField to Guest's Value For That Field. NullPointer will be caught in Guest Class if exists.
        }

        //If guest is loaded, enable buttons to get to other menus
        manageAddOns.setDisable(false);
        managePayments.setDisable(false);
        saveButton.setDisable(false);

        //Load all auction items into inventory

        for (Item i : g.getItems()) {
            Label l = new Label();
            l.setText("$" + i.get("itemPrice") + " : " + i.get("itemName"));
            itemList.getChildren().add(l);
        }

        //Load all add-on items into inventory

        HashMap<AddOnItem,Integer> addOnsInInventory = new HashMap<>();

        for (AddOnContainer a : g.getAddOnItems()) {
            //Get the current amount of the item. If it isn't in the map yet, there is now 1.
            int amount = addOnsInInventory.getOrDefault(a.getItemType(), 0);
            addOnsInInventory.put(a.getItemType(),amount+1);
        }

        for (AddOnItem i : addOnsInInventory.keySet()) {
            Label l = new Label();
            l.setText("$"+(i.getCost()*addOnsInInventory.get(i))+" : " + i + "      (Qty: "+ addOnsInInventory.get(i) +")");
            itemList.getChildren().add(l);
        }

        if (g.getItems().isEmpty() && g.getAddOnItems().isEmpty()) itemList.getChildren().clear();
    }

    /**
     * Saves all values currently loaded into the form to the Guest's data class file.
     * This method must run before exiting/loading new window, or all changes made will be lost
     * from lastSave->present.
     */
    @FXML
    protected void saveForm() {
        Guest g = guestSelect.getValue();
        for (String s : textFields.keySet()) {
            g.add(s, textFields.get(s).getText()); //Puts Each TextField Into Guest's HashMap
        }

        //Note: all payment information is handled from the PaymentController.

        guestSelect.getItems().sort(Guest::compareTo);
    }


    /**
     * Loads the FXML page for Items, and switches the current window to that. Note that
     * calling this method DOES NOT save the current data in the form before changing windows
     */
    @FXML
    public void changePages() {
        try {
            Stage stage = (Stage) firstName.getScene().getWindow();
            stage.setMinHeight(300);
            stage.setMinWidth(700);
            Parent root = FXMLLoader.load(getClass().getResource("Item.fxml"));
            Scene scene = new Scene(root, 700, 300);
            stage.setHeight(300);
            stage.setWidth(700);
            stage.setTitle("Checkout-EWB Version II: Item Page");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Page: Item.fxml.");
            System.out.println("Program Will Continue To Run To Allow Data Saving. Restart As Soon As Possible.");
        }
    }
}
