package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.FXMLAddOn.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        File guestFile = new File("Guests.csv");
        File itemFile = new File("Items.csv");

        if (itemFile.exists()) {
            ArrayList<String> itemFileData = new ArrayList<>(Arrays.asList(DataManager.readFile(itemFile).split("\n")));
            ArrayList<String> header = new ArrayList<>(Arrays.asList(itemFileData.get(0).trim().split(",[ ]*")));

            for (int k = 1; k < itemFileData.size(); k++) { //Loop through everything but header row
                String lineAsString = itemFileData.get(k);
                ArrayList<String> line = new ArrayList<>(Arrays.asList(lineAsString.split(",")));
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

                i.add(header.get(1),line.get(1));
                i.add(header.get(2),line.get(2));

                DataManager.items.add(i);
            }

        }


        if (guestFile.exists()) {
            ArrayList<String> guestFileData = new ArrayList<>(Arrays.asList(DataManager.readFile(guestFile).split("\n")));
            ArrayList<String> header = new ArrayList<>(Arrays.asList(guestFileData.get(0).trim().split(",")));
            int firstHashmapIndex = 1;
            int firstNonHashmapIndex = header.indexOf("items");
            for (int i = 1; i < guestFileData.size(); i++) { //Starting at 1 to exclude header row from the data loading
                ArrayList<String> line = new ArrayList<>(Arrays.asList(guestFileData.get(i).split(",")));
                Guest g = new Guest();

                //
                // Correctly Parse and Set The Guest's Number
                //

                int number = -1;
                try {
                    number = Integer.parseInt(line.get(0));
                } catch (Exception e) {
                    System.out.println("Error Loading Data: Unable To Parse Guest Number.");
                }
                g.setNumber(number);

                //
                // Correctly Enter The Hashmap Fields
                //

                for (int k = firstHashmapIndex; k < firstNonHashmapIndex; k++) {
                    if (k < header.size() && k < line.size())
                        g.add(header.get(k), line.get(k));
                }

                //
                // Correctly Load In Items
                //

                if (firstNonHashmapIndex < line.size() && !line.get(firstNonHashmapIndex).equals("")) { //If there is item data to load
                    ArrayList<String> itemNumberListAsString = new ArrayList<>(Arrays.asList(line.get(firstNonHashmapIndex).split(";")));
                    ArrayList<Integer> itemNumberList = new ArrayList<>();
                    for (String s : itemNumberListAsString) {
                        int num = Integer.parseInt(s);
                        itemNumberList.add(num);
                    }

                    for (Item item : items) { //Loop through all items and add them to guest's inventory if they have the #
                        if (itemNumberList.contains(item.getNumber())) {
                            g.addItem(item);
                        }
                    }
                }

                //
                // Correctly Load In Add-On Items
                //

                if (firstNonHashmapIndex+1 < line.size() && firstNonHashmapIndex+1 < header.size() &&!line.get(firstNonHashmapIndex+1).equals("")) { //If there is add on item data to load
                    ArrayList<String> addOnItemList = new ArrayList<>(Arrays.asList(line.get(firstNonHashmapIndex+1).split(";")));


                    for (String s : addOnItemList) {
                        if (s.trim().isEmpty()) continue;
                        ArrayList<String> addOnItem = new ArrayList<>(Arrays.asList(s.split("_")));

                        AddOnItem itemType = AddOnItem.stringToAddOnItem(addOnItem.get(0)); //Get Item Type
                        double cost;
                        try {
                            cost = Double.parseDouble(addOnItem.get(1));
                        } catch (Exception e) {
                            cost = itemType.getCost();
                        }

                        String description = addOnItem.get(2);

                        AddOnContainer aoc = new AddOnContainer(itemType,cost,description,g);
                        g.addItem(aoc);
                    }

                }

                //Finally, add the new guest to the list.
                DataManager.guests.add(g);
            }

            //TODO: Load In Payment Information


        }

        //TODO: Read Data From .csv Files and Populate Lists
        //TODO: Separately put items into Guest's arraylist from the other objects.

        hasLoaded = true; //Tell program that data has been loaded
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveData() {
        ArrayList<String> guestFileData = new ArrayList<>(); //Stores all of the data in the guest file, line by line
        ArrayList<String> itemFileData = new ArrayList<>(); //Stores all of the data in the item file, line by line
        ArrayList<String> paymentFileData = new ArrayList<>(); //Stores all of the data in the transaction list file, line by line


        //
        // Loading Data From Program To Specific ArrayLists
        //
        if (!guests.isEmpty()) {
            ArrayList<String> header = new ArrayList<>();
            header.add("number");
            header.addAll(guests.get(0).getHeader());
            header.add("items");
            header.add("addOnItems");
            guestFileData.add(arrayListToDelimitedString(header, ","));
            for (Guest g : guests) { //Iterate through all lines in the guest array list
                ArrayList<String> lineToAdd = new ArrayList<>();
                lineToAdd.add("" + g.getNumber());
                lineToAdd.addAll(g.getAll());
                lineToAdd.add(arrayListToDelimitedString(g.getItemNumbersAsList(), ";")); //Add All Guest Items
                lineToAdd.add(arrayListToDelimitedString(g.getAddOnItemsAsList(), ";")); //All All Guest Add-On Items
                guestFileData.add(arrayListToDelimitedString(lineToAdd, ",")); //Add all HashMap values from guest to the List
            }
        }

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


        HashMap<Guest, List<PaymentContainer>> transactionList = new HashMap<>();
        guests.forEach(g -> transactionList.put(g,g.getPayments()));

        for (Guest g : transactionList.keySet()) { //Loop through all guests that are contained in the transaction list
            for (PaymentContainer p : transactionList.get(g)) { //Loop through all payments for the specified guest
                String description = p.getDescription();
                double paid = p.getPaid();
                double change = p.getChange();
                PaymentMethod paymentMethod = p.getPaymentMethod();
                PaymentType paymentType = p.getPaymentType();
                ArrayList<String> line = new ArrayList<>();

                //TODO: Save values into arraylist

                String lineToAdd = DataManager.arrayListToDelimitedString(line,",");
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

        FileWriter guestWriter = null, itemWriter = null;
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
     *
     * @param list List to be converted
     * @return String with elements of list delimited by commas
     */
    private static String arrayListToDelimitedString(ArrayList<String> list, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String val : list) {
            sb.append(val); //Put in string
            if (list.indexOf(val) != list.size() - 1) //Only add delimiter to elements that are not at the end
                sb.append(delimiter);
        }
        return sb.toString();
    }

    /**
     * Returns whether the program has already loaded data from a data.csv file into the
     * program
     *
     * @return True if Data Has Been Loaded
     */
    public static boolean hasLoadedData() {
        return hasLoaded;
    }

    private static String readFile(File f) {
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

}
