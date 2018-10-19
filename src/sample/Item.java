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
public class Item {

    private static ArrayList<Integer> validNumbers = new ArrayList<>();

    private ArrayList<String> content = new ArrayList<>();
    private HashMap<String,String> map = new HashMap<>();
    private Guest owner = null; //Owner of item. Null for no owner assigned.


    private int number = -1; //Identifying number for a Item. Must be specific.

    @SuppressWarnings("StatementWithEmptyBody")
    public Item() {
        for (int num = 0;!setNumber(num);num++) {} //Loop until we can set the number to be a valid number
    }


    /**
     * Sets the guest's identifying number.
     * This number MUST be unique for each guest.
     * @param num Number to set the guest to
     * @return Successful set of guest's number
     */
    public boolean setNumber(int num) {
        if (validNumbers.contains(num)) {
            return false; //Return false if the guest's desired number is already in use.
        }
        this.number = num;
        validNumbers.add(num); //Add the new number to the list of current numbers.
        return true;
    }

    /**
     * Provides the guest's unique identifying number.
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
     * Removes a value from the Item class containing the specified type
     * @param type Type of data to remove
     */
    public void remove(String type) {
        map.remove(map);
    }

}
