package main.FXMLAddOn;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.Main;

public class PaymentContainer extends HBox {

    private double total = -1;
    private double paid = -1;
    private double change = -1;

    public PaymentContainer(double total, double paid, double change) {
        super(); //Construct
        setSpacing(8); //Set padding between objects
        setAlignment(Pos.CENTER_LEFT); //Align contents to center left
        this.total = total;
        this.paid = paid;
        this.change = change;

        Label l = new Label("Total: " + total + ", Paid: " + paid + ", Change: " + change);
        Button b = new Button("Delete");
        b.setOnAction(e -> remove()); //Remove From Container When Clicked On

        this.getChildren().add(l);
        this.getChildren().add(b);
    }

    public double getTotal() {
        return total;
    }

    public double getPaid() {
        return paid;
    }

    public double getChange() {
        return change;
    }

    private void remove() {
        //TODO: Update Total Counters For Change
        Main.paymentController.remove(this);
    }
}
