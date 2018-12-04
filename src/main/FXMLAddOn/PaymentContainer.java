package main.FXMLAddOn;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.Main;
import main.PaymentController;

public class PaymentContainer extends HBox {

    private String description = "";
    private double paid = -1;
    private double change = -1;
    private PaymentMethod paymentMethod = PaymentMethod.OTHER;

    public PaymentContainer(double paid, double change, PaymentMethod p,String description) {
        super(); //Construct
        setSpacing(8); //Set padding between objects
        setAlignment(Pos.CENTER_LEFT); //Align contents to center left
        this.paid = paid;
        this.change = change;
        paymentMethod = p;
        if (description.length() > 50) description = description.substring(0,50); //Prevent Super Long Strings
        this.description = description;

        Label l;
        if (description.isEmpty()) {
            l = new Label("Paid: " + paid + ", Change: " + change + ", With: " + p);
        } else {
            l = new Label("Paid: " + paid + ", Change: " + change + ", With: " + p + ", For: " + description);
        }
        Button b = new Button("Delete");
        b.setOnAction(e -> remove()); //Remove From Container When Clicked On

        this.getChildren().add(l);
        this.getChildren().add(b);
    }

    public double getPaid() {
        return paid;
    }

    public double getChange() {
        return change;
    }

    public String getDescription() {
        return description;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    private void remove() {
        Main.paymentController.remove(this);
    }
}
