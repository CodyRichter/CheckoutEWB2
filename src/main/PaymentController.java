package main;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.FXMLAddOn.PaymentContainer;

import java.awt.*;

public class PaymentController {

    private Guest selectedGuest = Main.guestController.guestSelect.getValue();
    private double totalPaid = 0;

    public PaymentController() {

    }

    @FXML
    private void initialize() {
        Main.paymentController = this;
    }

    @FXML
    VBox payments; //List of all payments

    @FXML
    Label totalDue; //Label on page containing the total amount of $$ due

    @FXML
    TextField amountPaid,changeGiven; //User inputs

    @FXML
    private void addPayment() {
        double paid = GuestController.parseInputToDouble(amountPaid);
        double change = GuestController.parseInputToDouble(changeGiven);
        double total = GuestController.parseInputToDouble(Main.guestController.totalDue);

        PaymentContainer p = new PaymentContainer(total,paid,change);

    }


}
