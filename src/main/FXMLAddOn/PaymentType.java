package main.FXMLAddOn;

public enum PaymentType { //Ways that a user can pay
    PAYMENT,DONATION;

    @Override
    public String toString() {
        if (this == PaymentType.PAYMENT) return "Payment";
        if (this == PaymentType.DONATION) return "Donation";
        return "[INVALID PAYMENT TYPE]";
    }

    /**
     * Given a string, returns the correct enumerated type matching the string
     * @param s String to parse
     * @return Correct Type that string is describing
     */
    public static PaymentType stringToPaymentType(String s) {

        if (s.equalsIgnoreCase("payment")) return PaymentType.PAYMENT;
        if (s.equalsIgnoreCase("donation")) return PaymentType.DONATION;

        return PaymentType.PAYMENT; //Default to this type if not able to parse string
    }

}
