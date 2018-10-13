package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class MainController {

    public MainController() {

    }

    @FXML
    MenuItem save,load,saveAndExit,info,newGuest,removeGuest;

    @FXML
    ComboBox<String> guestSelect;

    @FXML
    TextField firstName,lastName,phoneNumber,emailAddress,entryDonation,additionalDonation,amountPaid,changeGiven;

    @FXML
    Label totalDue;


    /**
     * Updates the indicator for total amount of money that is due. This will dynamically update as the user
     * inputs the amount of money that the person has paid. This will also alert the user if they haven't given
     * sufficient change.
     */
    @FXML
    private void updateTotal() {
        //TODO: Implement Method
    }


    /**
     * Saves all of the information in the form into the User object associated with the loaded profile.
     * Must correctly handle the order in which the object is added to the User.java account, in order to handle
     * the generic data storage type that the User class uses.
     */
    @FXML
    private void saveFields() {
        //TODO: Implement Method
    }


    /**
     * Opens the Item Menu To Modify The Values In Items. Should close this window
     * and open the new one in focus.
     */
    @FXML
    private void openItemMenu() {
        //TODO: Implement Method
    }


    /**
     * Opens new FXML menu window for add-ons to User's Auction Visit.
     * Some possible items may be: T-Shirt, Glass, etc... This method will support all wildcard
     * entries that may not be classified elsewhere.
     */
    @FXML
    private void openAddOnMenu() {
        //TODO: Implement Method
    }



}
