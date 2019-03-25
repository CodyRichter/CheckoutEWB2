package main.AddsOns;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CheckoutEWB2 - main.Transactions
 *
 * @author AddOnItem
 * @version 1.0
 */
public enum  AddOnItem {

    //TODO: Add tracking to number of ticket available

    //All Add-On Items Are In The Enums Below. Simply Add a new Enum to Create a new Add-On Item
    SHIRT,GLASS,TWO_GLASS,ADULT_TICKET,DISCOUNT_TICKET;

    /**
     * All Item Information Can Be Added Here.
     * To Add a new Add-On Item, create the Enum above and then add a new entry to the ArrayList
     * Below with the information on the item type, display name, and price
     */
    private static ArrayList<ItemAccessContainer> itemData = new ArrayList<>(Arrays.asList(
            new ItemAccessContainer(AddOnItem.SHIRT,"Shirt",10),
            new ItemAccessContainer(AddOnItem.GLASS,"Glass",7),
            new ItemAccessContainer(AddOnItem.TWO_GLASS,"2x Glass (Combo Deal)",12),
            new ItemAccessContainer(AddOnItem.ADULT_TICKET,"Adult Ticket",20),
            new ItemAccessContainer(AddOnItem.DISCOUNT_TICKET,"Student/Discount Ticket",10)));


    /**
     * Returns a string with the name of the item type. If for some reason the item
     * isn't valid, it will return "[INVALID ITEM TYPE]"
     * @return String containing add-on-item name
     */
    @Override
    public String toString() {
        for (ItemAccessContainer i : itemData) {
            if (this == i.item) return i.name;
        }
        return "[INVALID ITEM TYPE]";
    }

    /**
     * Returns the cost of the given add-on item.
     * @return Double with value of price of item
     */
    public double getCost() {
        for (ItemAccessContainer i : itemData) {
            if (this == i.item) return i.price;
        }
        return -1;
    }

    /**
     * Given a string, returns the correct enumerated type matching the string
     * @param s String to parse
     * @return Correct Type that string is describing
     */
    public static AddOnItem stringToAddOnItem(String s) {
        for (ItemAccessContainer i : itemData) {
            if (i.name.equalsIgnoreCase(s)) return i.item;
        }

        return AddOnItem.SHIRT; //Default to this type if not able to parse string
    }


}

class ItemAccessContainer {

    public AddOnItem item;
    public String name;
    public double price;

    ItemAccessContainer(AddOnItem item, String name, double price) {
        this.item = item;
        this.name = name;
        this.price = price;
    }
}
