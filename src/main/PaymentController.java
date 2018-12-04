package main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.FXMLAddOn.PaymentContainer;


public class PaymentController {

    private Guest selectedGuest = Main.guestController.guestSelect.getValue();

    public PaymentController() {

    }

    @FXML
    private void initialize() {
        Main.paymentController = this;

        //TODO: Find way to bring focus back to GuestController on exit.
        //amountPaid.getScene().getWindow().setOnCloseRequest(event -> {System.out.println("Exiting!");});

    }

    @FXML
    VBox payments; //Container holding all payments for given user.

    @FXML
    Label totalDue; //Label on page containing the total amount of $$ due

    @FXML
    TextField amountPaid,changeGiven; //Input fields for a new payment.


    /**
     * Adds a new payment to the current guest. This method creates a PaymentContainer,
     * and displays the payment in a list on the page.
     */
    @FXML
    private void addPayment() {
        double paid = GuestController.parseInputToDouble(amountPaid);
        double change = GuestController.parseInputToDouble(changeGiven);
        double total = GuestController.parseInputToDouble(Main.guestController.totalDue);

        PaymentContainer p = new PaymentContainer(total,paid,change);
        payments.getChildren().add(p);
    }

    /**
     * Removes a specific payment from the Guest's total list of payments.
     * @param p PaymentContainer to remove from guest.
     */
    public void remove(PaymentContainer p) {
        payments.getChildren().remove(p);
    }


    /**
     * Exits the PaymentController and re-opens the GuestController for the current guest.
     */
    public void exit() {

        //TODO: Open GuestController
        //TODO: Ensure controller is for selectedGuest

    }

}
