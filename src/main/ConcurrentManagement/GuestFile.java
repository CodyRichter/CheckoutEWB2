package main.ConcurrentManagement;

import main.ConcurrentDataManager;
import main.FXMLAddOn.AddOnContainer;
import main.FXMLAddOn.AddOnItem;
import main.Guest;
import main.Item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static main.ConcurrentDataManager.*;

/**
 * File object to store the current Guest's data. Can directly handle many actions
 * relating to a specific guest
 */
public class GuestFile extends File {

    public double number = -1;
    public static final GuestFile noOwner = new GuestFile(); //Constant containing GuestFile to represent no owner for item

    public GuestFile(String pathname) {
        super(pathname);
        String numberAsString;
        if (this.getName().indexOf(' ') >= 0) {
            numberAsString = this.getName().substring(0, this.getName().indexOf(' '));
        } else {
            numberAsString = this.toString();
        }
        try {
            number = Double.parseDouble(numberAsString);
        } catch (Exception e) {
            System.out.println("[Data Loading Error]: Unable To Parse Guest Number on file " + pathname + ". Number As String Parsed As: ["+this.toString()+"].");
        }
    }

    public GuestFile(Guest g) {
        super(ConcurrentDataManager.networkLocation+"/"+ConcurrentDataManager.folders[0]+"/"+g.toString()+".csv");
        number = g.getNumber();
    }

    private GuestFile() {
        super("");
    }


    /**
     * Loads data from the GuestFile into a Guest object. If
     *
     * @return Guest object containing data from file
     */
    public Guest load() {
        if (this == noOwner) return null;
        Guest g = new Guest(true);
        g.setNumber(number);
        if (!this.exists()) return g;
        ArrayList<String> guestFileData = new ArrayList<>(Arrays.asList(readFile(this).split("\n")));
        ArrayList<String> header = new ArrayList<>(Arrays.asList(guestFileData.get(0).trim().split(",")));
//        System.out.println("Header: " + guestFileData.get(0));
//        System.out.println("Contnt: " + guestFileData.get(1));


        int firstHashmapIndex = 1;
        int firstNonHashmapIndex = header.indexOf("items");
        ArrayList<String> line = new ArrayList<>(Arrays.asList(guestFileData.get(1).split(",")));

        // Replace all special characters from loaded data
        line.replaceAll(a -> a.replace("\n", ""));
        line.replaceAll(a -> a.replace("\t", ""));
        line.replaceAll(a -> a.replace("\r", ""));



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
            for (String itemNum : itemNumberListAsString) {
                int num = Integer.parseInt(itemNum);
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

        if (firstNonHashmapIndex + 1 < line.size() && firstNonHashmapIndex + 1 < header.size() && !line.get(firstNonHashmapIndex + 1).equals("")) { //If there is add on item data to load
            ArrayList<String> addOnItemList = new ArrayList<>(Arrays.asList(line.get(firstNonHashmapIndex + 1).split(";")));

            for (String currItemString : addOnItemList) {
                if (currItemString.trim().isEmpty()) continue;
                ArrayList<String> addOnItem = new ArrayList<>(Arrays.asList(currItemString.split("_")));
                if (addOnItem.isEmpty()) continue; //Don't operate on a badly loaded list
                AddOnItem itemType = AddOnItem.stringToAddOnItem(addOnItem.get(0)); //Get Item Type
                double cost;
                String description = "";
                try {
                    if (addOnItem.size() >= 2) {
                        cost = Double.parseDouble(addOnItem.get(1));
                    } else { //Don't load in information with payment data
                        continue;
                    }
                } catch (Exception e) {
                    cost = itemType.getCost();
                }

                if (addOnItem.size() >= 3) {
                    description = addOnItem.get(2);
                }

                AddOnContainer aoc = new AddOnContainer(itemType, cost, description, g);
                g.addItem(aoc);
            }

        }

        //Finally, add the new guest to the list.

        if (g.getNumber() == -1) return null;
        return g;
    }

    public GuestFile save(Guest g) {
        if (this == noOwner) return null;
        GuestFile newFile = new GuestFile(ConcurrentDataManager.networkLocation + "/" + folders[0] + "/" + (g.toString() + ".csv"));

        if (newFile.exists()) {
            newFile = this;
        }

        ArrayList<String> guestFileData = new ArrayList<>(); //Stores all of the data in the guest file, line by line

        //
        // Convert Guest Object to ArrayLists
        //
        ArrayList<String> header = new ArrayList<>();
        header.add("number");
        header.addAll(g.getHeader());
        header.add("items");
        header.add("addOnItems");
        guestFileData.add(arrayListToDelimitedString(header, ","));
        ArrayList<String> lineToAdd = new ArrayList<>();
        lineToAdd.add("" + g.getNumber());
        lineToAdd.addAll(cleanAll(g.getAll())); //Note: must clean all to remove problematic characters
        lineToAdd.add(arrayListToDelimitedString(g.getItemNumbersAsList(), ";")); //Add All Guest Items
        lineToAdd.add(arrayListToDelimitedString(g.getAddOnItemsAsList(), ";")); //All All Guest Add-On Items
        guestFileData.add(arrayListToDelimitedString(lineToAdd, ",")); //Add all HashMap values from guest to the List


        //
        // Manipulating Loaded ArrayLists into Strings To Be Written
        //
        String guestFileDataAsString;

        StringBuilder sb = new StringBuilder();
        for (String s : guestFileData) {
            sb.append(s);
            if (!guestFileData.get(guestFileData.size() - 1).equals(s)) { //Prevent Newline @ End of File
                sb.append("\n");
            }
        }
        guestFileDataAsString = sb.toString();

        //
        // Writing Data To File
        //
        File guestFile = new File("Guests.csv");

        try { //Ensure Files Are Created
            if (!guestFile.exists())  //Ensure file for guests exists
                //noinspection ResultOfMethodCallIgnored
                guestFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[Data Saving Error] Unable To Create New Data CSV File!");
        }

        FileWriter guestWriter;
        try {
            guestWriter = new FileWriter(newFile);
            guestWriter.write(guestFileDataAsString);
            guestWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Data Saving Error] Error While Writing To CSV Files!");
        }
        if (newFile != this) {
            this.delete();
            ConcurrentDataManager.guests.remove(this);
            ConcurrentDataManager.guests.add(newFile);
        }
        return newFile;
    }

    /**
     * Will create a lock file for the current Guest. A lock file tells the program that this guest
     * is currently being modified from elsewhere and should not be modified
     */
    public void lock() {
//        File lockFile = new File(networkLocation + "/" + folders[0] + "/" + number + ".lck");
//        try {
//            lockFile.createNewFile();
//        } catch (IOException e) {
//            System.out.println("[Critical Error]: Unable To Create Lock File For Guest Number: " + number);
//            e.printStackTrace();
//        }
    }

    /**
     * Will delete the lock file for the current Guest. Unlocking the guest will allow the file to be accessed from
     * other instances of the program.
     */
    public void unlock() {
//        File lockFile = new File(networkLocation + "/" + folders[0] + "/" + number + ".lck");
//        if (!lockFile.exists()) return;
//        lockFile.delete();
    }

    /**
     * Will check if the current guest is locked
     *
     * @return True if the guest is locked, false if the guest is unlocked.
     */
    public boolean isLocked() {
        File lockFile = new File(networkLocation + "/" + folders[0] + "/" + number + ".lck");
        return lockFile.exists();
    }

    /**
     * Will return the name of the file without the extension. This is what will appear in the
     * Drop-Down menu for the Guest Selector in the Guest FXML page
     *
     * @return File name nicely formatted to a string
     */
    @Override
    public String toString() {
        if (this == noOwner) return "No Owner";
        if (super.toString().length() < 4) return "[Unable To Parse File Name]";
        String fileName = super.toString();
        return fileName.substring(fileName.lastIndexOf('\\') + 1, fileName.length() - 4); //Return File Name Without Extension
    }

    /**
     * Compares two files on the basis of number
     * @param that GuestFile to compare this to
     * @return 1 if this is greater, -1 if that is greater, 0 if equal
     */
    public int compareTo(GuestFile that) {
        if (this.number > that.number) return 1;
        else if (this.number < that.number) return -1;
        return 0;
    }

    /**
     * Will check all guest files to see if a number has been used
     * @param numberToCheck Number to check availability of
     * @return true: available, false: used already
     */
    public static boolean isNumberAvailable(double numberToCheck) {
        if (numberToCheck < 0) return false;
        for (GuestFile g : ConcurrentDataManager.guests) {
            if (g.number == numberToCheck) return false;
        }
        return true;
    }

}
