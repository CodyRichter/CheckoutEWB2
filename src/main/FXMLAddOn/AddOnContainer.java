package main.FXMLAddOn;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.Guest;
import main.Main;

public class AddOnContainer extends HBox {

    private AddOnItem itemType;
    private double cost;
    private String description = "";
    private Guest owner = null;

    public AddOnContainer(AddOnItem type, double cost, String desc, Guest owner) {
        super(); //Construct
        setSpacing(8); //Set padding between objects
        setAlignment(Pos.CENTER_LEFT); //Align contents to center left
        this.itemType = type;
        this.cost = cost;
        if (description.length() > 50) description = description.substring(0,50); //Prevent Super Long Strings
        this.description = desc;
        this.owner = owner;

        Label l = new Label("Item: " + type + ", Price: " + cost + ", Notes: " + description);
        Button b = new Button("Delete");
        b.setOnAction(e -> remove()); //Remove From Container When Clicked On

        this.getChildren().add(l);
        this.getChildren().add(b);
        owner.addItem(this);
    }

    public AddOnItem getItemType() {
        return itemType;
    }

    public double getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public Guest getOwner() {
        return owner;
    }

    private void remove() {
        Main.addOnController.removeItem(owner,this);
    }
}
