package main.FXMLAddOn;

public enum PaymentType { //Ways that a user can pay
    PAYMENT,DONATION;

    @Override
    public String toString() {
        if (this == PaymentType.PAYMENT) return "Payment";
        if (this == PaymentType.DONATION) return "Donation";
        return "[INVALID PAYMENT TYPE]";
    }
}
