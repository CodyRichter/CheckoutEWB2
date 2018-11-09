package main.FXMLAddOn;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import main.Main;

public class PaymentContainer extends HBox {

    private int total = -1;
    private int paid = -1;
    private int change = -1;

    public PaymentContainer(int total, int paid, int change) {
        super();
        setSpacing(8);
        setAlignment(Pos.CENTER_LEFT);
        this.total = total;
        this.paid = paid;
        this.change = change;

        Label l = new Label("Total: " + total + ", Paid: " + paid + ", Change: " + change);
        Button b = new Button("Delete");
        b.setOnAction(e -> remove()); //Remove From Container When Clicked On

        this.getChildren().add(l);
        this.getChildren().add(b);
    }

    public int getTotal() {
        return total;
    }

    public int getPaid() {
        return paid;
    }

    public int getChange() {
        return change;
    }

    private void remove() {
        //TODO: Update Total Counters For Change
        Main.guestController.remove(this);
    }
}
