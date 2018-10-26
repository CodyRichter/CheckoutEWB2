package main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Guest data storage file: Contains all data that is saved/loaded into the
 * .csv file.
 *
 * @author Cody R
 * @version 1.0
 */
public class Guest implements Comparable<Guest> {

    private static ArrayList<Integer> validNumbers = new ArrayList<>();

    private HashMap<String,String> map = new HashMap<>();
    private ArrayList<Item> items; //TODO: Implement methods to handle adding and removing items from a guest

    private int number = -1; //Identifying number for a Guest. Must be specific.
    private boolean temp; //Whether the current guest is for temporary use. (Temp guests have no item #)

    @SuppressWarnings("StatementWithEmptyBody")
    public Guest() {
        for (int num = 0;!setNumber(num);num++) {} //Loop until we can set the number to be a valid number
    }

    /**
     * Creates a temporary guest for use within the program. A temporary guest does not have an item number
     * and is not meant to be saved into the config file
     * @param temp
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public Guest(boolean temp,String name) {
        if (!temp) { //Only run this if the guest is not a temp
            for (int num = 0;!setNumber(num);num++) {} //Loop until we can set the number to be a valid number
        } else {
            map.put("firstName",name);
        }
    }


    /**
     * Sets the guest's identifying number.
     * This number MUST be unique for each guest.
     * @param num Number to set the guest to
     * @return Successful set of guest's number
     */
    public boolean setNumber(int num) {
        if (temp || validNumbers.contains(num)) {
            return false; //Return false if the guest's desired number is already in use or if guest is temp
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
     * Adds a value to the Guest class containing information
     * @param type Identifier used to determine data type
     * @param value Value being stored
     */
    public void add(String type, String value) {
        map.put(type,value);
    }

    /**
     * Returns the desired value from the guest's inventory
     * It is possible for this value to be null!
     * @param key Key of item to get value of.
     * @return Value stored in guest's hashmap of inventory items.
     */
    public String get(String key) {
        String toReturn = map.get(key);
        if (toReturn == null) toReturn = "";
        return toReturn;
    }

    /**
     * Removes a value from the Guest class containing the specified type
     * @param type Type of data to remove
     */
    public void remove(String type) {
        if (map.containsKey(type))
            map.remove(type);
    }

    public void addItem(Item i) {
        //TODO: add item to guest's inventory
    }

    public void removeItem(Item i) {
        //TODO: Remove item from guest's inventory
    }

    @Override
    public String toString() {
        //Ensure We Have Data For All Fields, And Remove Null Entries
        String firstName = get("firstName");
        if (firstName==null) firstName="";
        String lastName = get("lastName");
        if (lastName==null) lastName="";

        //Only Return Fields That We Have
        if (firstName.equals("") && lastName.equals("")) {
            return "" + number;
        } else if (firstName.equals("")) {
            return ""+number+" | " + lastName;
        } else if (lastName.equals("")) {
            return ""+number+" | " + firstName;
        }
        //Ideal Case: Return:   # | Last Name, First Name
        return ""+number+" | " + lastName + ", " + firstName;
    }

    /**
     * Compares guests based on number
     * @return 0 if equal, 1 if this is greater than other guest's number, less if this is less than other guest's number
     */
    public int compareTo(Guest g2) {
        Guest g1 = this;
        if (g1.number == g2.number) return 0; //If both are equal
        if (g1.number > g2.number) return 1; //If this is greater than that
        return -1; //If this is less than that
    }
}
