package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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


        hasLoaded = true; //Tell program that data has been loaded
    }

    public static void saveData() {
        //TODO: Save All Data In Guest List to .csv File
        //TODO: Save All Data In Item List to .csv File
    }

    /**
     * Returns whether the program has already loaded data from a data.csv file into the
     * program
     * @return True if Data Has Been Loaded
     */
    public static boolean hasLoadedData() {
        return hasLoaded;
    }
}
