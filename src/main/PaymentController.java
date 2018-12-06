package main;

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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.FXMLAddOn.AddOnContainer;
import main.FXMLAddOn.PaymentContainer;
import main.FXMLAddOn.PaymentMethod;
import main.FXMLAddOn.PaymentType;

import java.text.DecimalFormat;

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
        paymentMethod.getItems().add(PaymentMethod.CASH);
        paymentMethod.getItems().add(PaymentMethod.CHECK);
        paymentMethod.getItems().add(PaymentMethod.OTHER);
        paymentMethod.setValue(PaymentMethod.CASH);

        //Set correct values of Payment Type
        paymentType.getItems().add(PaymentType.PAYMENT);
        paymentType.getItems().add(PaymentType.DONATION);
        paymentType.setValue(PaymentType.PAYMENT);

        //Update Counters
        updateMoneyCounters();
    }

    //
    // JavaFX Variables
    //

    @FXML
    VBox payments; //Container holding all payments for given user.

    @FXML
    Label total,totalPaid,totalDue,deltaTotal; //[Sum of All Items + Donations,Amount Paid So Far,Amount Remaining To Pay,Label Saying If Change/Payment Needed]

    @FXML
    TextField amountPaid,changeGiven,description; //Input fields for a new payment.

    @FXML
    ChoiceBox<PaymentMethod> paymentMethod; //Payment Method: Cash/Check

    @FXML
    ChoiceBox<PaymentType> paymentType; //Payment Type: Payment/Donation


    //
    // Payment Management Methods
    //


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
        paymentType.setValue(PaymentType.PAYMENT);
        paymentMethod.setValue(PaymentMethod.CASH);

        payments.getChildren().add(p); //Add the payment to display on the form
        selectedGuest.getPayments().add(p); //Add the payment to the list of all payments for the guest

        updateMoneyCounters();

    }

    /**
     * Removes a specific payment from the Guest's total list of payments.
     * @param p PaymentContainer to removePayment from guest.
     */
    public void removePayment(PaymentContainer p) {
        payments.getChildren().remove(p);
        selectedGuest.getPayments().remove(p); //Remove the payment from the list of all the payments for the guest.
        updateMoneyCounters();
    }

    //
    // Payment Label Methods
    //

    /**
     * Calculates the total amount of money that is due
     */
    @FXML
    private void getTotal() {
        double totalAsDouble = 0;
        for (Item i : selectedGuest.getItems()) {
            try {
                totalAsDouble += Double.parseDouble(i.get("itemPrice")); //Gets the price of each item and adds it to the total
            } catch (Exception ignored) {}
        }
        for (PaymentContainer p : selectedGuest.getPayments()) {
            if (p.getPaymentType() == PaymentType.DONATION) {
                totalAsDouble += p.getPaid()-p.getChange();
            }
        }
        for (AddOnContainer a : selectedGuest.getAddOnItems()) {
            totalAsDouble += a.getCost();
        }
        total.setText("$"+new DecimalFormat("#.##").format(totalAsDouble));
    }

    /**
     * Calculates the amount of money that has been paid in total so far.
     */
    @FXML
    private void getAmountPaid() {
        double paidAsDouble = 0;
        for (PaymentContainer p : selectedGuest.getPayments()) {
            paidAsDouble += p.getPaid()-p.getChange();
        }
        totalPaid.setText("$"+new DecimalFormat("#.##").format(paidAsDouble));
    }

    /**
     * Calculates the amount of money that is due back to the guest
     */
    @FXML
    private void getChangeDue() {
        double totalDouble = Double.parseDouble(total.getText().substring(1,total.getText().length()));
        double paidDouble = Double.parseDouble(totalPaid.getText().substring(1,totalPaid.getText().length()));
        double amountDue = Double.parseDouble(total.getText().substring(1,total.getText().length()))-Double.parseDouble(totalPaid.getText().substring(1,totalPaid.getText().length()));
        deltaTotal.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        if (amountDue > 0) { //Guest Must Pay More Money
            deltaTotal.setTextFill(Color.RED);
            deltaTotal.setText("Payment Needed: ");
            totalDue.setText("$"+new DecimalFormat("#.##").format(Math.abs(amountDue)));
        } else if (amountDue < 0) {//Guest Is Owed Change
            deltaTotal.setTextFill(Color.BLUE);
            deltaTotal.setText("Give Change: ");
            totalDue.setText("$"+new DecimalFormat("#.##").format(Math.abs(amountDue)));
        } else { //Paid In Full, Everything Is Set.
            deltaTotal.setTextFill(Color.GREEN);
            deltaTotal.setText("No Action Needed");
            totalDue.setText("");
        }
    }

    /**
     * Updates the labels for total,change due, and payment due
     */
    private void updateMoneyCounters() {
        getTotal();
        getAmountPaid();
        getChangeDue();
    }

    /**
     * Exits the PaymentController and re-opens the GuestController for the current guest.
     */
    @FXML
    public void exit() {

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

            Main.guestController.loadGuestIntoForm(selectedGuest);

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
