package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * CheckoutEWB2 Data Manager. Handles all loading/saving of data from program
 * to csv files.
 *
 * @author Cody R
 * @version 1.0
 */
public class DataManager {

    public static ObservableList<Guest> guests = FXCollections.observableArrayList();
    public static ObservableList<Item> items = FXCollections.observableArrayList();
    private static boolean hasLoaded = false;

    public static void loadData() {
        //TODO: Read Data From .csv Files and Populate Lists
        //TODO: Separately put items into Guest's arraylist from the other objects.


        hasLoaded = true; //Tell program that data has been loaded
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveData() {
        ArrayList<String> guestFileData = new ArrayList<>();
        ArrayList<String> itemFileData = new ArrayList<>();

        //
        // Loading Data From Program To Specific ArrayLists
        //
        if (!guests.isEmpty()) {
            guestFileData.add(arrayListToCSVString(guests.get(0).getHeader()));
            for (Guest g : guests) { //Iterate through all lines in the guest array list
                guestFileData.add(arrayListToCSVString(g.getAll())); //Add all HashMap values from guest to the List
                //TODO: Save Item Data and Addon Data
            }
        }

        if (!items.isEmpty()) {
            itemFileData.add(arrayListToCSVString(items.get(0).getHeader()));
            for (Item i : items) { //Iterate through all lines in the guest array list
                itemFileData.add(arrayListToCSVString(i.getAll())); //Add all HashMap values from guest to the List
            }
        }

        //
        // Manipulating Loaded ArrayLists into Strings To Be Written
        //
        String guestFileDataAsString;
        String itemFileDataAsString;
        StringBuilder sb = new StringBuilder();
        for (String s : guestFileData) {
            sb.append(s).append("\n");
        }
        guestFileDataAsString = sb.toString();
        sb = new StringBuilder();
        for (String s : itemFileData) {
            sb.append(s).append("\n");
        }
        itemFileDataAsString = sb.toString();

        //
        // Writing Data To File
        //
        File guestFile = new File("Guests.csv");
        File itemFile = new File("Items.csv");

        try { //Ensure Files Are Created
            if (!guestFile.exists())  //Ensure file for guests exists
                guestFile.createNewFile();
            if (!itemFile.exists())   //Ensure file for items exists
                itemFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error! Unable To Create New Data CSV File!");
        }

        FileWriter guestWriter=null, itemWriter=null;
        try {
            guestWriter = new FileWriter("Guests.csv");
            itemWriter = new FileWriter("Items.csv");

            guestWriter.write(guestFileDataAsString);
            itemWriter.write(itemFileDataAsString);

            guestWriter.close();
            itemWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error While Writing To CSV Files!");
        }
    }

    /**
     * Converts an ArrayList of Strings into a single string delimited by commas
     * @param list List to be converted
     * @return String with elements of list delimited by commas
     */
    private static String arrayListToCSVString(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String val : list) {
            sb.append(clean(val)); //Put in clean string
            if (list.indexOf(val) != list.size()-1) //Only add commas to elements that are not at the end
                sb.append(",");
            }
        return sb.toString();
    }

    /**
     * Returns whether the program has already loaded data from a data.csv file into the
     * program
     * @return True if Data Has Been Loaded
     */
    public static boolean hasLoadedData() {
        return hasLoaded;
    }

    /**
     * Removes all problematic characters for CSV files from a String
     * @param input String to have special characters removed from
     * @return Clean string safe to be put into CSV file
     */
    public static String clean(String input) {
        String result = input.replace('\n',' ');
        result = result.replace('\r',' ');
        result = result.replace('\n',' ');
        result = result.replace('\t',' ');
        result = result.replace('\\',' ');
        result = result.replace('\"',' ');
        result = result.replace('\'',' ');
        result = result.replace(',',' ');
        return result;
    }

}
