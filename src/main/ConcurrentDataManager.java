package main;

import main.FXMLAddOn.PaymentContainer;

import java.io.File;
import java.io.IOException;

/**
 * Unlike the traditional data manager, this is intended to be used over the network with multiple instances of the program
 * accessing one network location for saving and loading data.
 *
 * @author Cody R
 * @version 1.0
 */
public class ConcurrentDataManager {

    //Location where common files are stored on network
    private static final String networkLocation = "//SENARII/Users/Cody/Documents/CheckoutEWB";
    //Note: Order Matters. Place Folder Names as {Guest Directory, Item Directory, Transaction Directory}
    private static final String[] folders = {"Guests","Items","Transactions"};


    private static boolean allDataLoaded = false;

    //
    // -------------------------------
    //          Loading Data
    // -------------------------------
    //

    /**
     * Loads all data into the program, from Guests, Items, and Transactions
     */
    public static void loadAllData() {
        if (allDataLoaded) return;
        //TODO: Load All Items Into Program

        //TODO: Load All Guests Into Program

        //TODO: Load All Transactions Into Program

        allDataLoaded = true;
    }

    /**
     * Loads in the updated data for a specific Guest or Item from File. Any Existing Information In This
     * Entry Will Be Overwritten.
     * @param s File Number
     */
    public static Guest loadGuestData(String s) {
        Guest g = null;

        //TODO: Load Guest File Data into g

        return g;
    }

    //
    // -------------------------------
    //          Saving Data
    // -------------------------------
    //


    /**
     * Unlike the traditional data manager, this method will only update the saved data from a single loaded Guest or Item.
     * @param g Guest to update data for
     */
    public static void saveData(Guest g) {
        setupNetworkLocation(); //Ensure Network Location is Correctly Set Up Before Accessing Fields
        String fileContents = "";

        //TODO: Write All Guest Data To CSV String

        saveData(folders[0],""+g.getNumber(),fileContents);
    }

    /**
     * Unlike the traditional data manager, this method will only update the saved data from a single loaded Guest or Item.
     * @param i Item to update data for
     */
    public static void saveData(Item i) {
        String fileContents = "";

        //TODO: Write All Item Data To CSV String

        saveData(folders[1],""+i.getNumber(),fileContents);
    }

    public static void saveData(PaymentContainer p) {
        String fileContents = "";

        //TODO: Write All Payment Data To CSV String

        //TODO: Find Way To Give All Transactions Unique IDs.
        saveData(folders[2],"",fileContents);
    }

    /**
     * Generic Method For saving data.
     * @param folder Subdirectory of network location containing the file to modify
     * @param fileName Name Of File To Modify, Without File Extension
     * @param fileContents New Contents Of File To Write
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void saveData(String folder, String fileName, String fileContents) {
        setupNetworkLocation(); //Ensure Network Location is Correctly Set Up Before Accessing Fields
        // ------------------------------------------------------------
        File f = new File(networkLocation+"/"+folder+"/"+fileName+".csv");
        if (!f.exists()) { //Ensure That File Exists
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("[Error]: Critical Error Saving Data. Unable To Create Save File.");
                e.printStackTrace();
            }
        }

        //TODO: Write The fileContents to the CSV file

        //Note: No need to load the data into the program because if we are saving the data, we have the latest copy.
    }

    //
    // -----------------------------------
    //          Utility Methods
    // -----------------------------------
    //

    /**
     * Checks Whether All Data Has Been Loaded Into Program Yet. If Data Needs To Be Loaded, Return true
     * @return Whether Data Needs to be loaded still
     */
    public static boolean needToLoadData() {
        return !allDataLoaded;
    }


    /**
     * Ensures that the network location chosen has the correct folders
     * and structure for handling data in the program
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void setupNetworkLocation() {
        for (String s : folders) {
            File f = new File(networkLocation+s);
            if (!f.exists()) { //Create New Folder if Doesn't Exist
                f.mkdir();
            }
        }
    }
}

