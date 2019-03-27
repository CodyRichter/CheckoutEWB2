package main.ConcurrentManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Unlike the traditional data manager, this is intended to be used over the network with multiple instances of the program
 * accessing one network location for saving and loading data.
 *
 * @author Cody R
 * @version 1.0
 */
public class ConcurrentDataManager {

    //Location where common files are stored on network
    public static final String networkLocation = System.getProperty("user.home") + "/Onedrive/CheckoutEWB";
    //Note: Order Matters. Place Folder Names as {Guest Directory, Item Directory, Transaction Directory}
    public static final String[] folders = {"Guests", "Transactions"};

    public static ObservableList<Item> items = FXCollections.observableArrayList();
    public static ObservableList<GuestFile> guests = FXCollections.observableArrayList();
    public static ObservableList<TransactionFile> transactions = FXCollections.observableArrayList();


    //
    // -------------------------------
    //          Loading Data
    // -------------------------------
    //

    /**
     * Loads all data into the program, from Guests, Items, and Transactions
     */
    public static void loadAllData() {
        guests.clear();
        transactions.clear();
        items.clear();
        setupNetworkLocation();

        loadItemData();

        File[] itemDirectory = new File(networkLocation + "/" + folders[0]).listFiles();
        if (itemDirectory != null)
            for (File f : itemDirectory) {
                if (!getFileExtension(f).equals(".csv")) return;
                GuestFile gf = new GuestFile(f.getAbsolutePath());
                guests.add(gf);
            }

    }


    /**
     * Loads in all items from file.
     */
    public static void loadItemData() {
        File itemFile = new File(networkLocation + "/Items.csv");
        if (itemFile.exists()) {

            ArrayList<String> itemFileData = new ArrayList<>(Arrays.asList(readFile(itemFile).split("\n")));
            ArrayList<String> header = new ArrayList<>(Arrays.asList(itemFileData.get(0).trim().split(",[ ]*")));

            for (int k = 1; k < itemFileData.size(); k++) { //Loop through everything but header row
                String lineAsString = itemFileData.get(k);
                ArrayList<String> line = new ArrayList<>(Arrays.asList(lineAsString.split(",")));

                for (int n = 0; n < line.size(); n++) { //Remove all special characters from loaded data
                    String[] toReplace = {"\n", "\t", "\r"};
                    for (String s : toReplace) {
                        line.set(n, line.get(n).replaceAll(s, ""));
                    }
                }

                //System.out.println(line.get(0) + " | " + line.get(1) + " | " + line.get(2));
                Item i = new Item();

                //
                // Correctly Parse and Set The Item's Number
                //

                int number = -1;
                try {
                    number = Integer.parseInt(line.get(0));
                } catch (Exception e) {
                    System.out.println("Error Loading Data: Unable To Parse Item Number.");
                }
                i.setNumber(number);


                //
                // Correctly Enter The Hashmap Fields
                //
                if (header.size() >= 2 && line.size() >= 2) {
                    i.add(header.get(1), line.get(1));
                }
                if (header.size() >= 3 && line.size() >= 3) {
                    i.add(header.get(2), line.get(2));
                }

                items.add(i);
            }

        }
    }

    //
    // -------------------------------
    //          Saving Data
    // -------------------------------
    //

    /**
     * Saves all data from the item List to a single CSV file
     */
    public static void saveItemData() {
        ArrayList<String> itemFileData = new ArrayList<>(); //Stores all of the data in the item file, line by line

        if (!items.isEmpty()) {
            ArrayList<String> header = new ArrayList<>();
            header.add("number");
            header.addAll(items.get(0).getHeader());
            itemFileData.add(arrayListToDelimitedString(header, ","));
            for (Item i : items) { //Iterate through all lines in the guest array list
                ArrayList<String> lineToAdd = new ArrayList<>();
                lineToAdd.add("" + i.getNumber());
                lineToAdd.addAll(i.getAll());
                itemFileData.add(arrayListToDelimitedString(lineToAdd, ",")); //Add all HashMap values from guest to the List
            }
        }

        String itemFileDataAsString;
        StringBuilder sb = new StringBuilder();
        for (String s : itemFileData) {
            sb.append(s);
            if (!itemFileData.get(itemFileData.size() - 1).equals(s)) { //Prevent Newline @ End of File
                sb.append("\n");
            }
        }
        itemFileDataAsString = sb.toString();

        File itemFile = new File(networkLocation + "/Items.csv");
        try { //Ensure Files Are Created
            if (!itemFile.exists())   //Ensure file for items exists
                //noinspection ResultOfMethodCallIgnored
                itemFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error! Unable To Create New Item Data CSV File!");
        }

        FileWriter itemWriter;
        try {
            itemWriter = new FileWriter(networkLocation + "/Items.csv");
            itemWriter.write(itemFileDataAsString);
            itemWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error While Writing To CSV Files!");
        }
    }

    //
    // -----------------------------------
    //          Utility Methods
    // -----------------------------------
    //

    public static void removeGuestFile(GuestFile f) {
        if (f.exists()) {
            f.unlock(); //Unlock file
            f.delete();
        }
        guests.remove(f);
    }

    /**
     * Ensures that the network location chosen has the correct folders
     * and structure for handling data in the program
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void setupNetworkLocation() {
        File dir = new File(networkLocation);
        if (!dir.exists()) { //Create New Folder if Doesn't Exist
            dir.mkdir();
        }

        for (String s : folders) {
            File f = new File(networkLocation + "/" + s);
            if (!f.exists()) { //Create New Folder if Doesn't Exist
                f.mkdir();
            }
        }
    }

    /**
     * Converts an ArrayList of Strings into a single string delimited by commas
     *
     * @param list List to be converted
     * @return String with elements of list delimited by commas
     */
    public static String arrayListToDelimitedString(ArrayList<String> list, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String val : list) {
            sb.append(val); //Put in string
            if (list.indexOf(val) != list.size() - 1) //Only add delimiter to elements that are not at the end
                sb.append(delimiter);
        }
        return sb.toString();
    }

    /**
     * Reads the contents of a file to a single string
     *
     * @param f File to read contents of
     * @return String containing all of the file contents
     */
    public static String readFile(File f) {
        if (f == null || !f.exists()) return "";

        byte[] encoded = {};

        try {
            encoded = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Loading File: " + f.getName());
        }
        return new String(encoded, Charset.defaultCharset());
    }

    /**
     * Returns the extension of the file
     *
     * @param file File to get extension of
     * @return File extension (including .)
     */
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    /**
     * Removes all problematic characters for CSV files from a String
     *
     * @param input String to have special characters removed from
     * @return Clean string safe to be put into CSV file
     */
    public static String clean(String input) {
        String result = input.replace('\n', ' ');
        result = result.replace('\r', ' ');
        result = result.replace('\n', ' ');
        result = result.replace('\t', ' ');
        result = result.replace('\\', ' ');
        result = result.replace('\"', ' ');
        result = result.replace('\'', ' ');
        result = result.replace(',', ' ');
        result = result.replace('_', ' ');
        result = result.replace('|', ' ');
        result = result.replace(';', ' ');
        return result;
    }

    /**
     * Removes all problematic characters from an arraylist of strings
     * Order is preserved
     *
     * @param s ArrayList of String to clean
     * @return Clean Arraylist
     */
    public static ArrayList<String> cleanAll(ArrayList<String> s) {
        ArrayList<String> cleanList = new ArrayList<>();
        s.forEach(x -> cleanList.add(clean(x)));
        return cleanList;
    }

}

