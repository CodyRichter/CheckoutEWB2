package main;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.FXMLAddOn.PaymentContainer;
import main.FXMLAddOn.PaymentMethod;
import main.FXMLAddOn.PaymentType;

import java.util.ArrayList;
import java.util.EventListener;

public class PaymentController {

    private Guest selectedGuest = Main.guestController.guestSelect.getValue();

    public PaymentController() {

    }

    @FXML
    private void initialize() {
        Main.paymentController = this;

        selectedGuest = Main.guestController.guestSelect.getValue(); //Get the guest currently selected in the guest controller.
        for (PaymentContainer p : selectedGuest.getPayments()) { //Add all current payments
            payments.getChildren().add(p);
        }

        changeGiven.addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    addPayment();
                }}});

        amountPaid.addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    addPayment();
                }}});

        description.addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    addPayment();
                }}});

        //Set correct values of Payment Method
        ArrayList<PaymentMethod> paymentMethodList = new ArrayList<>();
        paymentMethodList.add(PaymentMethod.CASH);
        paymentMethodList.add(PaymentMethod.CHECK);
        paymentMethodList.add(PaymentMethod.OTHER);
        paymentMethod.setItems(FXCollections.observableArrayList(paymentMethodList));
        paymentMethod.setValue(PaymentMethod.CASH);

        //Set correct values of Payment Type
        ArrayList<PaymentType> paymentTypeList = new ArrayList<>();
        paymentTypeList.add(PaymentType.PAYMENT);
        paymentTypeList.add(PaymentType.DONATION);
        paymentType.setItems(FXCollections.observableArrayList(paymentTypeList));
        paymentType.setValue(PaymentType.PAYMENT);

    }

    @FXML
    VBox payments; //Container holding all payments for given user.

    @FXML
    Label total,totalPaid,totalDue; //[Sum of All Items + Donations,Amount Paid So Far,Amount Remaining To Pay]

    @FXML
    TextField amountPaid,changeGiven,description; //Input fields for a new payment.

    @FXML
    ChoiceBox<PaymentMethod> paymentMethod; //Payment Method: Cash/Check

    @FXML
    ChoiceBox<PaymentType> paymentType; //Payment Type: Payment/Donation

    /**
     * Adds a new payment to the current guest. This method creates a PaymentContainer,
     * and displays the payment in a list on the page.
     */
    @FXML
    private void addPayment() {

        //This if-statement will trigger if there are invalid characters in the field that are not numeric.
        if (!isValidNumber(amountPaid.getText()) || !isValidNumber(changeGiven.getText())) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Error");
            a.setHeaderText("Unable To Add Payment");
            a.setContentText("The current payment was unable to be added. Please ensure that the amount paid and the amount of change is valid.");
            a.showAndWait();
            return; //Exit method here to stop adding payment
        }

        //Get values from input fields
        double paid = GuestController.parseInputToDouble(amountPaid);
        double change = GuestController.parseInputToDouble(changeGiven);
        String desc = DataManager.clean(description.getText());

        PaymentContainer p = new PaymentContainer(paid,change,paymentMethod.getValue(),paymentType.getValue(),desc);

        //Wipe the text in the fields for the
        amountPaid.clear();
        changeGiven.clear();
        description.clear();
        paymentMethod.setValue(PaymentMethod.CASH);

        payments.getChildren().add(p); //Add the payment to display on the form
        selectedGuest.getPayments().add(p); //Add the payment to the list of all payments for the guest
    }

    /**
     * Removes a specific payment from the Guest's total list of payments.
     * @param p PaymentContainer to remove from guest.
     */
    public void remove(PaymentContainer p) {
        payments.getChildren().remove(p);
        selectedGuest.getPayments().remove(p); //Remove the payment from the list of all the payments for the guest.
    }


    /**
     * Exits the PaymentController and re-opens the GuestController for the current guest.
     */
    @FXML
    public void exit() {

        //TODO: Set Guest that is loaded in this PaymentController to be Guest that is loaded in GuestController.

        try {
            Stage stage = (Stage) amountPaid.getScene().getWindow();
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            Parent root = FXMLLoader.load(getClass().getResource("Guest.fxml"));
            Scene scene = new Scene(root, 700, 600);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.setTitle("Checkout-EWB Version II: Guest Page");
            stage.setScene(scene);
            stage.show();
            stage.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading Page: Guest. (Attempted To Load From PaymentController)");
            System.out.println("Program Will Continue To Run To Allow Data Saving. Restart As Soon As Possible.");
        }

    }

    /**
     * Will check an input string and see whether it can be parsed into a valid number
     * @param str String to check
     * @return String can be parsed into a number (double)
     */
    private static boolean isValidNumber(String str) {
        if (str.isEmpty()) return true; //Empty string = 0

        try { //If this works, then true
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException e) { //String can't be parsed, so is false
            return false;
        }
        return true;
    }

}
