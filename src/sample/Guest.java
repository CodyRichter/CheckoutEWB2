package sample;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Guest data storage file: Contains all data that is saved/loaded into the
 * .csv file.
 *
 * @author Cody R
 * @version 1.0
 */
public class Guest {

    private static ArrayList<Integer> validNumbers = new ArrayList<>();

    private ArrayList<String> content = new ArrayList<>();
    private HashMap<String,String> map = new HashMap<>();

    private int number = -1; //Identifying number for a Guest. Must be specific.

    @SuppressWarnings("StatementWithEmptyBody")
    public Guest() {
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
        map.remove(map);
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

}
