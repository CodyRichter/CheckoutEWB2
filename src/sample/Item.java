package sample;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Item data storage file: Contains all data that is saved/loaded into the
 * .csv file.
 *
 * @author Cody R
 * @version 1.0
 */
public class Item implements Comparable<Item> {

    private static ArrayList<Integer> validNumbers = new ArrayList<>();

    private HashMap<String,String> map = new HashMap<>();

    private int number = -1; //Identifying number for an Item. Must be specific.

    @SuppressWarnings("StatementWithEmptyBody")
    public Item() {
        for (int num = 0;!setNumber(num);num++) {} //Loop until we can set the number to be a valid number
    }


    /**
     * Sets the item's identifying number.
     * This number MUST be unique for each item.
     * @param num Number to set the item to
     * @return Successful set of item's number
     */
    public boolean setNumber(int num) {
        if (validNumbers.contains(num)) {
            return false; //Return false if the item's desired number is already in use.
        }
        this.number = num;
        validNumbers.add(num); //Add the new number to the list of current numbers.
        return true;
    }

    /**
     * Provides the item's unique identifying number.
     * @return Unique number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Adds a value to the Item class containing information
     * @param type Identifier used to determine data type
     * @param value Value being stored
     */
    public void add(String type, String value) {
        map.put(type,value);
    }

    /**
     * Returns the desired value from the item's inventory
     * It is possible for this value to be null!
     * @param key Key of item to get value of.
     * @return Value stored in item's hashmap of inventory items.
     */
    public String get(String key) {
        String toReturn = map.get(key);
        if (toReturn == null) toReturn = "";
        return toReturn;
    }

    /**
     * Removes a value from the Item class containing the specified type
     * @param type Type of data to remove
     */
    public void remove(String type) {
        if (map.containsKey(type))
            map.remove(type);
    }

    @Override
    public String toString() {
        //Ensure We Have Data For All Fields, And Remove Null Entries
        String itemName = get("itemName");
        if (itemName==null) itemName="";

        //Only Return Fields That We Have
        if (itemName.equals("")) {
            return ""+number;
        }
        //Ideal Case: Return:   # | Item Name
        return ""+number+" | " + itemName;
    }

    /**
     * Compares items based on number
     * @return 0 if equal, 1 if this is greater than other item's number, less if this is less than other item's number
     */
    public int compareTo(Item i2) {
        Item i1 = this;
        if (i1.number == i2.number) return 0; //If both are equal
        if (i1.number > i2.number) return 1; //If this is greater than that
        return -1; //If this is less than that
    }
}
