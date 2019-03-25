package main.AddsOns;

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

    /**
     * Returns type of add on item. Note: the return type
     * of this method is an enumerated type, not a
     * string
     * @return Type of Add on item.
     */
    public AddOnItem getItemType() {
        return itemType;
    }

    /**
     * Returns cost of item as a double.
     * @return Cost of item
     */
    public double getCost() {
        return cost;
    }

    /**
     * Returns a description of the item as a string
     * @return Description if item as string, empty string if no description set
     */
    public String getDescription() {
        return description;
    }

    /**
     * Will return the owner of the add on item
     * @return Owner of add-on item. null if no Owner for item set
     */
    public Guest getOwner() {
        return owner;
    }

    /**
     * Deletes this add on item from a guest. Note: Calling this method can not
     * be reversed. Be sure that you want to actually do this.
     */
    private void remove() {
        Main.addOnController.removeItem(owner,this);
    }
}
