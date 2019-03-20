//package main;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import main.FXMLAddOn.*;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * CheckoutEWB2 Data Manager. Handles all loading/saving of data from program
// * to csv files.
// *
// * @author Cody R
// * @version 1.0
// */
//public class DataManager {
//
//    public static ObservableList<Guest> guests = FXCollections.observableArrayList();
//    public static ObservableList<Item> items = FXCollections.observableArrayList();
//    private static boolean hasLoaded = false;
//
//
//    //TODO: Don't Save Data To File if It Is The Same as When Loaded
//    private static ObservableList<Guest> lastLoadedGuests = FXCollections.observableArrayList();
//    private static ObservableList<Item> lastLoadedItems = FXCollections.observableArrayList();
//
//    /**
//     * Loads all saved program data from CSV files if the files exist. If the files do exist, the contents
//     * will be parsed and loaded into the program. The file names that will be loaded are:
//     * - Guests.csv
//     * - Items.csv
//     * - Transactions.csv
//     */
//    public static void loadData() {
//        File guestFile = new File("Guests.csv");
//        File itemFile = new File("Items.csv");
//        File paymentFile = new File("Transactions.csv");
//
//        if (itemFile.exists()) {
//
//            ArrayList<String> itemFileData = new ArrayList<>(Arrays.asList(DataManager.readFile(itemFile).split("\n")));
//            ArrayList<String> header = new ArrayList<>(Arrays.asList(itemFileData.get(0).trim().split(",[ ]*")));
//
//            for (int k = 1; k < itemFileData.size(); k++) { //Loop through everything but header row
//                String lineAsString = itemFileData.get(k);
//                ArrayList<String> line = new ArrayList<>(Arrays.asList(lineAsString.split(",")));
//
//                for (int n = 0; n < line.size(); n++) { //Remove all special characters from loaded data
//                    String[] toReplace = {"\n","\t","\r"};
//                    for (String s : toReplace) {
//                        line.set(n, line.get(n).replaceAll(s, ""));
//                    }
//                }
//
//                //System.out.println(line.get(0) + " | " + line.get(1) + " | " + line.get(2));
//                Item i = new Item();
//
//                //
//                // Correctly Parse and Set The Item's Number
//                //
//
//                int number = -1;
//                try {
//                    number = Integer.parseInt(line.get(0));
//                } catch (Exception e) {
//                    System.out.println("Error Loading Data: Unable To Parse Item Number.");
//                }
//                i.setNumber(number);
//
//
//                //
//                // Correctly Enter The Hashmap Fields
//                //
//
//                i.add(header.get(1),line.get(1));
//                i.add(header.get(2),line.get(2));
//
//                DataManager.items.add(i);
//            }
//
//        }
//
//
//        if (guestFile.exists()) {
//
//            ArrayList<String> guestFileData = new ArrayList<>(Arrays.asList(DataManager.readFile(guestFile).split("\n")));
//            ArrayList<String> header = new ArrayList<>(Arrays.asList(guestFileData.get(0).trim().split(",")));
//            int firstHashmapIndex = 1;
//            int firstNonHashmapIndex = header.indexOf("items");
//            for (int i = 1; i < guestFileData.size(); i++) { //Starting at 1 to exclude header row from the data loading
//                ArrayList<String> line = new ArrayList<>(Arrays.asList(guestFileData.get(i).split(",")));
//
//                for (int n = 0; n < line.size(); n++) { //Remove all special characters from loaded data
//                    String[] toReplace = {"\n","\t","\r"};
//                    for (String s : toReplace) {
//                        line.set(n, line.get(n).replaceAll(s, ""));
//                    }
//                }
//
//                Guest g = new Guest();
//
//                //
//                // Correctly Parse and Set The Guest's Number
//                //
//
//                int number = -1;
//                try {
//                    number = Integer.parseInt(line.get(0));
//                } catch (Exception e) {
//                    System.out.println("Error Loading Data: Unable To Parse Guest Number.");
//                }
//                g.setNumber(number);
//
//                //
//                // Correctly Enter The Hashmap Fields
//                //
//
//                for (int k = firstHashmapIndex; k < firstNonHashmapIndex; k++) {
//                    if (k < header.size() && k < line.size())
//                        g.add(header.get(k), line.get(k));
//                }
//
//                //
//                // Correctly Load In Items
//                //
//
//                if (firstNonHashmapIndex < line.size() && !line.get(firstNonHashmapIndex).equals("")) { //If there is item data to load
//                    ArrayList<String> itemNumberListAsString = new ArrayList<>(Arrays.asList(line.get(firstNonHashmapIndex).split(";")));
//                    ArrayList<Integer> itemNumberList = new ArrayList<>();
//                    for (String s : itemNumberListAsString) {
//                        int num = Integer.parseInt(s);
//                        itemNumberList.add(num);
//                    }
//
//                    for (Item item : items) { //Loop through all items and add them to guest's inventory if they have the #
//                        if (itemNumberList.contains(item.getNumber())) {
//                            g.addItem(item);
//                        }
//                    }
//                }
//
//                //
//                // Correctly Load In Add-On Items
//                //
//
//                if (firstNonHashmapIndex+1 < line.size() && firstNonHashmapIndex+1 < header.size() &&!line.get(firstNonHashmapIndex+1).equals("")) { //If there is add on item data to load
//                    ArrayList<String> addOnItemList = new ArrayList<>(Arrays.asList(line.get(firstNonHashmapIndex+1).split(";")));
//
//
//                    for (String s : addOnItemList) {
//                        if (s.trim().isEmpty()) continue;
//                        ArrayList<String> addOnItem = new ArrayList<>(Arrays.asList(s.split("_")));
//                        if (addOnItem.isEmpty()) continue; //Don't operate on a badly loaded list
//                        AddOnItem itemType = AddOnItem.stringToAddOnItem(addOnItem.get(0)); //Get Item Type
//                        double cost;
//                        String description = "";
//                        try {
//                            if (addOnItem.size() >= 2) {
//                                cost = Double.parseDouble(addOnItem.get(1));
//                            } else { //Don't load in information with payment data
//                                continue;
//                            }
//                        } catch (Exception e) {
//                            cost = itemType.getCost();
//                        }
//
//                        if (addOnItem.size() >= 3) {
//                            description = addOnItem.get(2);
//                        }
//
//                        AddOnContainer aoc = new AddOnContainer(itemType,cost,description,g);
//                        g.addItem(aoc);
//                    }
//
//                }
//
//                //Finally, add the new guest to the list.
//                DataManager.guests.add(g);
//            }
//
//
//
//
//            if (paymentFile.exists()) {
//
//                ArrayList<String> paymentFileData = new ArrayList<>(Arrays.asList(DataManager.readFile(paymentFile).split("\n")));
//
//                for (int k = 1; k < paymentFileData.size(); k++) { //Loop through everything but header row
//                    String lineAsString = paymentFileData.get(k);
//                    ArrayList<String> line = new ArrayList<>(Arrays.asList(lineAsString.split(",")));
//                    for (int n = 0; n < line.size(); n++) { //Remove all special characters from loaded data
//                        String[] toReplace = {"\n","\t","\r"};
//                        for (String s : toReplace) {
//                            line.set(n, line.get(n).replaceAll(s, ""));
//                        }
//                    }
//
//                    //
//                    // Correctly Parse and Set The Payment's Associated Guest
//                    //
//
//                    Guest owner = null;
//
//                    int number = -1;
//                    try {
//                        number = Integer.parseInt(line.get(0));
//                    } catch (Exception e) {
//                        System.out.println("Error Loading Data: Unable To Parse Guest Number From Payment");
//                    }
//
//                    for (Guest g : guests) {
//                        if (g.getNumber() == number) {
//                            owner = g;
//                            break;
//                        }
//                    }
//
//                    if (owner == null) continue; //If we didn't find a valid owner for this transaction, we can discard it.
//
//
//                    // Amount Paid
//                    // Index: 1
//
//                    double paid = 0;
//                    try {
//                        paid = Double.parseDouble(line.get(1));
//                    } catch (Exception e) {
//                        System.out.println("Error Loading Data: Unable To Parse Amount Paid From Payment");
//                    }
//
//                    // Change Given
//                    // Index: 2
//
//                    double change = 0;
//                    try {
//                        change = Double.parseDouble(line.get(2));
//                    } catch (Exception e) {
//                        System.out.println("Error Loading Data: Unable To Parse Change Given From Payment");
//                    }
//
//                    // Payment Method
//                    // Index: 3
//
//                    PaymentMethod method = PaymentMethod.stringToPaymentMethod(line.get(3));
//
//                    // Payment Type
//                    // Index: 4
//
//                    PaymentType type = PaymentType.stringToPaymentType(line.get(4));
//
//                    // Description
//                    // Index: 5
//                    String description = "";
//                    if (5 < line.size())
//                        description = line.get(5);
//
//                    PaymentContainer p = new PaymentContainer(paid,change,method,type,description);
//                    owner.getPayments().add(p);
//                }
//
//            }
//
//
//        }
//
//        lastLoadedGuests = FXCollections.observableArrayList(guests);
//        lastLoadedItems = FXCollections.observableArrayList(items);
//
//        hasLoaded = true; //Tell program that data has been loaded
//    }
//
//    /**
//     * Saves all ldata currently in the program to CSV files for later use. The contents of any existing files in the same
//     * directory will be overwritten in order to handle the new data. The file names that will be saved to are:
//     * - Guests.csv
//     * - Items.csv
//     * - Transactions.csv
//     */
//    public static void saveData() {
//        ArrayList<String> guestFileData = new ArrayList<>(); //Stores all of the data in the guest file, line by line
//        ArrayList<String> itemFileData = new ArrayList<>(); //Stores all of the data in the item file, line by line
//        ArrayList<String> transactionFileData = new ArrayList<>(); //Stores all of the data in the transaction list file, line by line
//
//
//        //
//        // Loading Data From Program To Specific ArrayLists
//        //
//        if (!guests.isEmpty()) {
//            ArrayList<String> header = new ArrayList<>();
//            header.add("number");
//            header.addAll(guests.get(0).getHeader());
//            header.add("items");
//            header.add("addOnItems");
//            guestFileData.add(arrayListToDelimitedString(header, ","));
//            for (Guest g : guests) { //Iterate through all lines in the guest array list
//                ArrayList<String> lineToAdd = new ArrayList<>();
//                lineToAdd.add("" + g.getNumber());
//                lineToAdd.addAll(cleanAll(g.getAll())); //Note: must clean all to remove problematic characters
//                lineToAdd.add(arrayListToDelimitedString(g.getItemNumbersAsList(), ";")); //Add All Guest Items
//                lineToAdd.add(arrayListToDelimitedString(g.getAddOnItemsAsList(), ";")); //All All Guest Add-On Items
//                guestFileData.add(arrayListToDelimitedString(lineToAdd, ",")); //Add all HashMap values from guest to the List
//            }
//        }
//
//        if (!items.isEmpty()) {
//            ArrayList<String> header = new ArrayList<>();
//            header.add("number");
//            header.addAll(items.get(0).getHeader());
//            itemFileData.add(arrayListToDelimitedString(header, ","));
//            for (Item i : items) { //Iterate through all lines in the guest array list
//                ArrayList<String> lineToAdd = new ArrayList<>();
//                lineToAdd.add("" + i.getNumber());
//                lineToAdd.addAll(i.getAll());
//                itemFileData.add(arrayListToDelimitedString(lineToAdd, ",")); //Add all HashMap values from guest to the List
//            }
//        }
//
//
//        HashMap<Guest, List<PaymentContainer>> transactionList = new HashMap<>();
//        guests.forEach(g -> transactionList.put(g,g.getPayments()));
//
//        String header = "number,amountPaid,changeGiven,paymentMethod,paymentType,description";
//        transactionFileData.add(header);
//
//        for (Guest g : transactionList.keySet()) { //Loop through all guests that are contained in the transaction list
//            for (PaymentContainer p : transactionList.get(g)) { //Loop through all payments for the specified guest
//                String description = p.getDescription();
//                double paid = p.getPaid();
//                double change = p.getChange();
//                PaymentMethod paymentMethod = p.getPaymentMethod();
//                PaymentType paymentType = p.getPaymentType();
//                ArrayList<String> line = new ArrayList<>();
//
//                line.add(""+g.getNumber()); //Number
//                line.add(""+paid); //Amount Paid
//                line.add(""+change); //Change
//                line.add(""+paymentMethod); //Payment Method
//                line.add(""+paymentType); //Payment Type
//                line.add(description);
//
//                String lineToAdd = DataManager.arrayListToDelimitedString(line,",");
//                transactionFileData.add(lineToAdd);
//            }
//        }
//
//
//        //
//        // Manipulating Loaded ArrayLists into Strings To Be Written
//        //
//        String guestFileDataAsString;
//        String itemFileDataAsString;
//        String paymentFileDataAsString;
//
//        StringBuilder sb = new StringBuilder();
//        for (String s : guestFileData) {
//            sb.append(s);
//            if (!guestFileData.get(guestFileData.size()-1).equals(s)) { //Prevent Newline @ End of File
//                sb.append("\n");
//            }
//        }
//        guestFileDataAsString = sb.toString();
//
//        sb = new StringBuilder();
//        for (String s : itemFileData) {
//            sb.append(s);
//            if (!itemFileData.get(itemFileData.size()-1).equals(s)) { //Prevent Newline @ End of File
//                sb.append("\n");
//            }
//        }
//        itemFileDataAsString = sb.toString();
//
//        sb = new StringBuilder();
//        for (String s : transactionFileData) {
//            sb.append(s);
//            if (!transactionFileData.get(transactionFileData.size()-1).equals(s)) { //Prevent Newline @ End of File
//                sb.append("\n");
//            }
//        }
//        paymentFileDataAsString = sb.toString();
//
//        //
//        // Writing Data To File
//        //
//        File guestFile = new File("Guests.csv");
//        File itemFile = new File("Items.csv");
//        File transactionFile = new File("Transactions.csv");
//
//        try { //Ensure Files Are Created
//            if (!guestFile.exists())  //Ensure file for guests exists
//                //noinspection ResultOfMethodCallIgnored
//                guestFile.createNewFile();
//            if (!itemFile.exists())   //Ensure file for items exists
//                //noinspection ResultOfMethodCallIgnored
//                itemFile.createNewFile();
//            if (!itemFile.exists())   //Ensure file for payments exists
//                //noinspection ResultOfMethodCallIgnored
//                transactionFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Error! Unable To Create New Data CSV File!");
//        }
//
//        FileWriter guestWriter, itemWriter, paymentWriter;
//        try {
//            guestWriter = new FileWriter("Guests.csv");
//            itemWriter = new FileWriter("Items.csv");
//            paymentWriter = new FileWriter("Transactions.csv");
//
//            guestWriter.write(guestFileDataAsString);
//            itemWriter.write(itemFileDataAsString);
//            paymentWriter.write(paymentFileDataAsString);
//
//            guestWriter.close();
//            itemWriter.close();
//            paymentWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Error While Writing To CSV Files!");
//        }
//    }
//
//}
