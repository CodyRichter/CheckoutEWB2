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
        ArrayList<String> guestFileData = new ArrayList<>(); //Stores all of the data in the guest file, line by line
        ArrayList<String> itemFileData = new ArrayList<>(); //Stores all of the data in the item file, line by line

        //
        // Loading Data From Program To Specific ArrayLists
        //
        if (!guests.isEmpty()) {
            ArrayList<String> header = new ArrayList<>();
            header.add("number");
            header.addAll(guests.get(0).getHeader());
            header.add("items");
            header.add("addOnItems");
            guestFileData.add(arrayListToDelimitedString(header,","));
            for (Guest g : guests) { //Iterate through all lines in the guest array list
                ArrayList<String> lineToAdd = new ArrayList<>();
                lineToAdd.add(""+g.getNumber());
                lineToAdd.addAll(g.getAll());
                lineToAdd.add(arrayListToDelimitedString(g.getItemNumbersAsList(),"|")); //Add All Guest Items
                lineToAdd.add(arrayListToDelimitedString(g.getAddOnItemsAsList(),"|")); //All All Guest Add-On Items
                guestFileData.add(arrayListToDelimitedString(lineToAdd,",")); //Add all HashMap values from guest to the List
            }
        }

        if (!items.isEmpty()) {
            ArrayList<String> header = new ArrayList<>();
            header.add("number");
            header.addAll(items.get(0).getHeader());
            itemFileData.add(arrayListToDelimitedString(header,","));
            for (Item i : items) { //Iterate through all lines in the guest array list
                ArrayList<String> lineToAdd = new ArrayList<>();
                lineToAdd.add(""+i.getNumber());
                lineToAdd.addAll(i.getAll());
                itemFileData.add(arrayListToDelimitedString(lineToAdd,",")); //Add all HashMap values from guest to the List
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
    private static String arrayListToDelimitedString(ArrayList<String> list,String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String val : list) {
            sb.append(val); //Put in string
            if (list.indexOf(val) != list.size()-1) //Only add delimiter to elements that are not at the end
                sb.append(delimiter);
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
        result = result.replace('_',' ');
        result = result.replace('|',' ');
        return result;
    }

}
