package main.ConcurrentManagement;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import main.ConcurrentDataManager;
import main.FXMLAddOn.*;
import main.Guest;
import main.Item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static main.ConcurrentDataManager.*;
import static main.ConcurrentDataManager.folders;

public class TransactionFile extends File {

    double transactionOwner = -1;

    public TransactionFile(double guestNum) {
        super(ConcurrentDataManager.networkLocation + "/" + ConcurrentDataManager.folders[1] + "/" + guestNum + " Transactions.csv");
        String numberAsString = this.getName().substring(0, this.getName().indexOf(' '));
        transactionOwner = Double.parseDouble(numberAsString);
    }

    /**
     * Loads data from the GuestFile into a Guest object. If
     */
    public ArrayList<PaymentContainer> load() {
        ArrayList<PaymentContainer> transactions = new ArrayList<>();
        if (!this.exists()) return transactions;
        ArrayList<String> paymentFileData = new ArrayList<>(Arrays.asList(ConcurrentDataManager.readFile(this).split("\n")));

        for (int k = 1; k < paymentFileData.size(); k++) { //Loop through everything but header row
            String lineAsString = paymentFileData.get(k);
            ArrayList<String> line = new ArrayList<>(Arrays.asList(lineAsString.split(",")));
            for (int n = 0; n < line.size(); n++) { //Remove all special characters from loaded data
                String[] toReplace = {"\n", "\t", "\r"};
                for (String s : toReplace) {
                    line.set(n, line.get(n).replaceAll(s, ""));
                }
            }

            // Amount Paid
            // Index: 1

            double paid = 0;
            try {
                paid = Double.parseDouble(line.get(1));
            } catch (Exception e) {
                System.out.println("Error Loading Data: Unable To Parse Amount Paid From Payment");
            }

            // Change Given
            // Index: 2

            double change = 0;
            try {
                change = Double.parseDouble(line.get(2));
            } catch (Exception e) {
                System.out.println("Error Loading Data: Unable To Parse Change Given From Payment");
            }

            // Payment Method
            // Index: 3

            PaymentMethod method = PaymentMethod.stringToPaymentMethod(line.get(3));

            // Payment Type
            // Index: 4

            PaymentType type = PaymentType.stringToPaymentType(line.get(4));

            // Description
            // Index: 5
            String description = "";
            if (5 < line.size())
                description = line.get(5);

            PaymentContainer p = new PaymentContainer(paid, change, method, type, description);
            transactions.add(p);
        }


        return transactions;
    }

    public void save(ArrayList<PaymentContainer> transactions) {
        ArrayList<String> transactionFileData = new ArrayList<>(); //Stores all of the data in the transaction list file, line by line
        String header = "number,amountPaid,changeGiven,paymentMethod,paymentType,description";
        transactionFileData.add(header);
        for (PaymentContainer p : transactions) { //Loop through all payments for the specified guest
            String description = p.getDescription();
            double paid = p.getPaid();
            double change = p.getChange();
            PaymentMethod paymentMethod = p.getPaymentMethod();
            PaymentType paymentType = p.getPaymentType();
            ArrayList<String> line = new ArrayList<>();

            line.add("" + transactionOwner); //Number
            line.add("" + paid); //Amount Paid
            line.add("" + change); //Change
            line.add("" + paymentMethod); //Payment Method
            line.add("" + paymentType); //Payment Type
            line.add(description);

            String lineToAdd = ConcurrentDataManager.arrayListToDelimitedString(line, ",");
            transactionFileData.add(lineToAdd);
        }

        String paymentFileDataAsString;
        StringBuilder sb = new StringBuilder();
        for (String s : transactionFileData) {
            sb.append(s);
            if (!transactionFileData.get(transactionFileData.size() - 1).equals(s)) { //Prevent Newline @ End of File
                sb.append("\n");
            }
        }
        paymentFileDataAsString = sb.toString();

        try { //Ensure Files Are Created
            if (!this.exists())   //Ensure file for payments exists
                //noinspection ResultOfMethodCallIgnored
                this.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error! Unable To Create New Data Transaction Data CSV File!");
        }

        FileWriter paymentWriter;

        try {
            paymentWriter = new FileWriter(this);
            paymentWriter.write(paymentFileDataAsString);
            paymentWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error While Writing To Transaction CSV Files!");
        }

    }

    /**
     * Will return the name of the file without the extension. This is what will appear in the
     * Drop-Down menu for the Guest Selector in the Guest FXML page
     *
     * @return File name nicely formatted to a string
     */
    @Override
    public String toString() {
        if (super.toString().length() < 4) return "[Unable To Parse Transaction File Name]";
        String fileName = super.toString();
        return fileName.substring(fileName.lastIndexOf('\\') + 1, fileName.length() - 4); //Return File Name Without Extension
    }
}
