package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.AddsOns.AddOnContainer;
import main.AddsOns.AddOnItem;
import main.ConcurrentManagement.ConcurrentDataManager;
import main.ConcurrentManagement.GuestFile;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class GuestController {

    private HashMap<String, TextInputControl> textFields; //Hashmap containing all textfields in program
    private StringBuilder searchWordTyped = new StringBuilder();
    public static GuestFile selectedGuest = null;
    public static Guest selectedGuestLoaded = null;

    public GuestController() {

    }

    /**
     * Will run when the page is loaded. Sets up formatting for general use later in program
     */
    @FXML
    public void initialize() {
        Main.guestController = this;
        textFields = new HashMap<>();
        textFields.put("firstName", firstName);
        textFields.put("lastName", lastName);
        textFields.put("phoneNumber", phoneNumber);
        textFields.put("emailAddress", emailAddress);
        textFields.put("address", address);

        header.setText("Checkout-EWB Ver."+Main.VERSION);


        currSearch.setVisible(false);
        //
        // Allows User to Search Through Guests
        //
        guestSelect.setOnKeyPressed((KeyEvent e) -> {
            if (guestSelect.isDisabled()) return; //Ensure Box is Functional\
            if (e.getCode() == KeyCode.ESCAPE) { //If ESC is Pressed, Clear Search Results and Reset Items in Box
                updateGuestSelector(ConcurrentDataManager.guests,ConcurrentDataManager.guests.get(0));
                searchWordTyped = new StringBuilder(); //Clear Word
                currSearch.setVisible(false);

            } else if (e.getCode() == KeyCode.BACK_SPACE) { //If Press Backspace, Delete Last Character
                if (searchWordTyped.length() == 0) return; //Ensure Not Deleting Nothing
                searchWordTyped.deleteCharAt(searchWordTyped.length()-1);

            } else if (e.getCode().isLetterKey() || e.getCode().isDigitKey() || e.getCode() == KeyCode.COMMA || e.getCode() == KeyCode.SPACE){ //If Char is valid for searching
                searchWordTyped.append(e.getCode().getName().toLowerCase());

            } else { //Otherwise end here
                return;
            }

            //Run the search on the characters inputted so far
            ObservableList<GuestFile> matchingList = FXCollections.observableArrayList();
            String goal = searchWordTyped.toString();

            //Show User Current Search
            currSearch.setVisible(true);
            currSearch.setText(goal);

            for (GuestFile g : ConcurrentDataManager.guests) {
                if (g.toString().toLowerCase().contains(goal.toLowerCase())) {
                    matchingList.add(g);
                }
            }
            updateGuestSelector(matchingList,matchingList.size() > 0 ? matchingList.get(0) : null);
        });


        File f = new File(ConcurrentDataManager.networkLocation);

        //Using Network Data Management
        ConcurrentDataManager.loadAllData(); //Load in all Data From Network

        if (!f.exists()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Critial Error");
            a.setHeaderText("Unable To Locate Data Files");
            a.setContentText("CheckoutEWB was unable to locate the data files required to run the program. Please ensure you are connected to the network and that you have permission to access the directory.");
            a.showAndWait();
            System.exit(0);
        }

        guestSelect.setItems(ConcurrentDataManager.guests);
        if (!guestSelect.getItems().isEmpty()) {
            updateForm(guestSelect.getItems().get(0));
            guestSelect.setValue(guestSelect.getItems().get(0));
        } else {
            updateForm(null);
            guestSelect.setDisable(true);
        }
        updateGuestSelector(ConcurrentDataManager.guests,guestSelect.getValue());
    }


    @FXML
    private MenuItem save, saveAndExit, newGuest, removeGuest;

    @FXML
    private ComboBox<GuestFile> guestSelect;

    @FXML
    private TextField firstName, lastName, phoneNumber, emailAddress;

    @FXML
    private TextArea address;

    @FXML
    private Button manageAddOns, saveButton, switchButton, managePayments;

    @FXML
    private VBox itemList;

    @FXML
    private Label currSearch,header;
    //
    // --------------------------------------
    // Menu Bar Interactions
    // --------------------------------------
    //

    /**
     * Saves current program data into .csv file
     */
    @FXML
    void saveGuestData() {
        GuestFile gf = guestSelect.getValue();
        if (gf == null) return;
        for (String s : textFields.keySet()) {
            selectedGuestLoaded.add(s, textFields.get(s).getText()); //Puts Each TextField Into Guest's HashMap
        }

        GuestFile newGuestFile = gf.save(selectedGuestLoaded);
        updateGuestSelector(ConcurrentDataManager.guests,newGuestFile); //Update selector to reflect on any changes that might be made when saving guest (e.g.: name change)
        //updateForm(newGuestFile);
    }

    /**
     * Saves the data from the form into a guest object and guest file
     * @param gf Guest File to save to
     * @param g Guest to save
     * @return New File
     */
    private GuestFile saveGuestData(GuestFile gf, Guest g) {
        if (gf == null) return gf;
        for (String s : textFields.keySet()) {
            g.add(s, textFields.get(s).getText()); //Puts Each TextField Into Guest's HashMap
        }

        return gf.save(g);
    }

    /**
     * Saves program data and exits program after saving.
     */
    @FXML
    public void saveGuestDataAndExit() {
        if (guestSelect.getValue() == null) exit();
        guestSelect.getValue().unlock();
        saveGuestData();
        exit();
    }

    /**
     * Sets everything in the current form to the specified guest. This method is
     * used when reloading the guest selector from another page.
     * @param g Guest's data to load.
     */
    void loadGuestIntoForm(GuestFile g) {
        guestSelect.getItems().sorted();
        guestSelect.setValue(g);
        updateForm(g);
    }


    /**
     * Creates a new guest object, adds it to the list of all guests, and sets
     * the current form to be that of the guest.
     */
    @FXML
    public void createNewGuest() {

        double gNum = -1;

        TextInputDialog d = new TextInputDialog();
        d.setTitle("Create New Guest");
        d.setContentText("Please enter the desired Guest number. Leave blank to automatically assign.");
        Button cancel = (Button) d.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.addEventFilter(ActionEvent.ACTION, event ->
                d.setResult(null));
        d.showAndWait();
        if (d.getResult() == null) return;
        if (d.getResult().isEmpty()) {
            while (!GuestFile.isNumberAvailable(gNum)) {
                gNum++;
            }
        } else {
            boolean success = true;
            try {
                gNum = Double.parseDouble(d.getResult());
            } catch (Exception ignored) {
                success = false;
            }
            if (!success || gNum < 0 || !GuestFile.isNumberAvailable(gNum)) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Unable To Create Guest");
                a.setContentText("The number you have chosen is not valid. Please ensure that it is formatted correctly and that it isn't already taken.");
                a.showAndWait();
                return;
            }
        }

        updateForm(null); //Wipe Form
        Guest g = new Guest(true);
        g.setNumber(gNum);
        GuestFile gf = new GuestFile(gNum);
        GuestFile newGuestFile = saveGuestData(gf,g);
        if (!ConcurrentDataManager.guests.contains(newGuestFile))
            ConcurrentDataManager.guests.add(newGuestFile);
        updateForm(newGuestFile);
        updateGuestSelector(ConcurrentDataManager.guests,newGuestFile);
        guestSelect.setDisable(false);
    }

    /**
     * Removes a guest object from the list of loaded guests. If the guest to delete is
     * the currently loaded guest in the form, the form will be cleared and a blank page will be loaded.
     */
    @FXML
    public void removeGuest() {
        Guest g = selectedGuestLoaded;
        if (g == null) return; //If guestSelect has no items selected, don't try to removePayment nothing

        //Confirm that you really want to delete this Guest
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Remove Guest");
        a.setHeaderText("Confirm Deleting of Guest: " + g.getNumber());
        a.setContentText("Are you sure that you want to delete this guest? All information will be lost forever. (A long time)");
        a.showAndWait();
        if (a.getResult() != ButtonType.OK) return;

        ConcurrentDataManager.removeGuestFile(guestSelect.getValue());

        if (ConcurrentDataManager.guests.size() > 0) {
            updateGuestSelector(ConcurrentDataManager.guests, ConcurrentDataManager.guests.get(0));
            updateForm(ConcurrentDataManager.guests.get(0)); //Update fields in form
            guestSelect.setDisable(false);
        } else { //Wipe & Disable Form
            updateForm(null);
            guestSelect.setDisable(true);
        }
    }

    /**
     * Renames all files in the Guest directory to the correct toString display format
     */
    @FXML
    public void formatFiles() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Format Files");
        a.setHeaderText("Are You Sure You Want To Format All Guest Files?");
        a.setContentText("Formatting all of the guest files has the potential to erase data from other locations that hasn't been saved, and should only be used initially to fix imported file names. \nAre you sure you want to continue?");
        a.showAndWait();
        if (a.getResult() != ButtonType.OK) return;
            GuestFile.formatAllGuestFiles();
    }

    @FXML
    public void countCheckIn() {
        int total = 0;
        for (GuestFile gf : ConcurrentDataManager.guests) {
            Guest g = gf.load();
            for (AddOnContainer aoc : g.getAddOnItems()) {
                if (aoc.getItemType() == AddOnItem.CHECK_IN) {
                    total++;
                    break;
                }
            }
        }
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Total Check In");
        a.setHeaderText("Total Number of Guests Checked In");
        a.setContentText(""+total);
        a.showAndWait();
    }

    /**
     * Updates all of the values in the Guest selector before they are displayed from the file
     */
    @FXML
    public void updateGuestSelectorFromFilesBeforeRender() {
        updateGuestSelector(ConcurrentDataManager.guests,selectedGuest);
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
    public void showDocumentation() {
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
    public void showGuestFromSelector() {

        if (guestSelect.getValue() != null && guestSelect.getValue().isLocked()) { //Don't Load Guest if Open From Another Location
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("[Error] Unable To Open Guest File");
            a.setHeaderText("Unable To Load Guest");
            a.setContentText("The current Guest is currently open from another location. Please change guests at that computer in order to modify the data in this instance.");
            a.showAndWait();
            guestSelect.setValue(selectedGuest);
            return;
        }
//        ObservableList<GuestFile> g = ConcurrentDataManager.guests;
//        g.sort(GuestFile::compareTo);
//        Platform.runLater(() -> guestSelect.setItems(g));

        GuestFile currGuest = guestSelect.getValue();
        updateForm(currGuest);
        //updateGuestSelector(ConcurrentDataManager.guests,currGuest);
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
    public void openAddOnMenu() {
        guestSelect.getValue().unlock();
        saveGuestData();

        try {
            Stage stage = (Stage) managePayments.getScene().getWindow();
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            Parent root = FXMLLoader.load(getClass().getResource("AddOns.fxml"));
            Scene scene = new Scene(root, 600, 400);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.setTitle("Checkout-EWB Version "+Main.VERSION+": Add-On Item Manager");
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
    public void openPaymentWindow() {
        guestSelect.getValue().unlock();
        saveGuestData();

        try {
            Stage stage = (Stage) managePayments.getScene().getWindow();
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            Parent root = FXMLLoader.load(getClass().getResource("Total.fxml"));
            Scene scene = new Scene(root, 600, 400);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.setTitle("Checkout-EWB Version "+Main.VERSION+": Payment Manager");
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
     * @param gf Guest's data to load from file. (null to clear form)
     */
    private void updateForm(GuestFile gf) {
        if (selectedGuest != null) {
            selectedGuest.unlock();
        }
        if (gf == null) { //If the guest that is being loaded is null, clear the form and reset it to default state
            for (TextInputControl t : textFields.values()) { //Iterate through all textFields and wipe them
                t.setText("");
                t.setDisable(true);
            }
            manageAddOns.setDisable(true);
            managePayments.setDisable(true);
            saveButton.setDisable(true);
            selectedGuest = null;
            selectedGuestLoaded = null;
            return;
        }
        selectedGuest = gf;
        selectedGuestLoaded = gf.load();
        selectedGuest.lock();
        itemList.getChildren().clear();

        for (String s : textFields.keySet()) {
            TextInputControl t = textFields.get(s); //Load desired TextField from HashMap
            t.setDisable(false);
            t.setText(selectedGuestLoaded.get(s)); //Set Value of TextField to Guest's Value For That Field. NullPointer will be caught in Guest Class if exists.
        }

        //If guest is loaded, enable buttons to get to other menus
        manageAddOns.setDisable(false);
        managePayments.setDisable(false);
        saveButton.setDisable(false);

        //Load all auction items into inventory

        for (Item i : selectedGuestLoaded.getItems()) {
            Label l = new Label();
            l.setText("$" + i.get("itemPrice") + " [#" + i.getNumber() + "] : " + i.get("itemName"));
            itemList.getChildren().add(l);
        }

        //Load all add-on items into inventory

        //HashMap<AddOnItem,Integer> addOnsInInventory = new HashMap<>();
//        for (AddOnContainer a : selectedGuestLoaded.getAddOnItems()) {
//            //Get the current amount of the item. If it isn't in the map yet, there is now 1.
//            int amount = addOnsInInventory.getOrDefault(a.getItemType(), 0);
//            addOnsInInventory.put(a.getItemType(),amount+1);
//        }

        for (AddOnContainer ic : selectedGuestLoaded.getAddOnItems()) {
            Label l = new Label();
            l.setText("$"+(ic.getCost())+" : " + ic.getItemType().toString());
            if (!ic.getDescription().isEmpty()) // If there is a description for the item
                l.setText(l.getText()+ " | NOTE: " + ic.getDescription());
            itemList.getChildren().add(l);
        }

        if (selectedGuestLoaded.getItems().isEmpty() && selectedGuestLoaded.getAddOnItems().isEmpty()) itemList.getChildren().clear();
    }

    /**
     * Updates the Guest selector with the new contents of the list.
     * @param c Collection to Update Selector With
     * @param g GuestFile to display information for
     */
    private void updateGuestSelector(ObservableList<GuestFile> c, GuestFile g) {
        if (g == null || c.size() == 0) return;
        if (c == ConcurrentDataManager.guests) ConcurrentDataManager.loadGuestData(); //Load in fresh guest data into form.
        Set<GuestFile> temp = new HashSet<>(c);
        c.clear();
        c.addAll(temp);
        guestSelect.setItems(c);
        guestSelect.getItems().sort(GuestFile::compareTo);
        guestSelect.setValue(g);
    }

    /**
     * Clears the current search being done for a specific guest.
     */
    @FXML
    private void clearSearch() {
        searchWordTyped = new StringBuilder();
        updateGuestSelector(ConcurrentDataManager.guests,ConcurrentDataManager.guests.get(0));
        currSearch.setVisible(false);
    }


    /**
     * Loads the FXML page for Items, and switches the current window to that. Note that
     * calling this method DOES NOT save the current data in the form before changing windows
     */
    @FXML
    public void changePages() {
        if (guestSelect.getValue() != null) {
            guestSelect.getValue().unlock();
            saveGuestData();
        }

        try {
            Stage stage = (Stage) firstName.getScene().getWindow();
            stage.setMinHeight(300);
            stage.setMinWidth(700);
            Parent root = FXMLLoader.load(getClass().getResource("Item.fxml"));
            Scene scene = new Scene(root, 700, 300);
            stage.setHeight(300);
            stage.setWidth(700);
            stage.setTitle("Checkout-EWB Version "+Main.VERSION+": Item Page");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Page: Item.fxml.");
            System.out.println("Program Will Continue To Run To Allow Data Saving. Restart As Soon As Possible.");
        }
    }

    //
    // ---------------------------
    //
    // ---------------------------
    //
}
