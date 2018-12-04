package main.FXMLAddOn;

public enum PaymentMethod { //Ways that a user can pay
    CASH,CHECK,OTHER;

    @Override
    public String toString() {
        if (this == PaymentMethod.CASH) return "Cash";
        if (this == PaymentMethod.CHECK) return "Check";
        if (this == PaymentMethod.OTHER) return "Other";
        return "[INVALID PAYMENT TYPE]";
    }
}
