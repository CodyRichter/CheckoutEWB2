package main.FXMLAddOn;

/**
 * CheckoutEWB2 - main.FXMLAddOn
 *
 * @author AddOnItem
 * @version 1.0
 */
public enum  AddOnItem {
    SHIRT,GLASS;

    @Override
    public String toString() {
        if (this == AddOnItem.SHIRT) return "Shirt";
        if (this == AddOnItem.GLASS) return "Glass";
        return "[INVALID ITEM TYPE]";
    }

    /**
     * Returns the cost of the given add-on item.
     * @return Double of price of item
     */
    public double getCost() {
        if (this == AddOnItem.SHIRT) return 10;
        if (this == AddOnItem.GLASS) return 7;
        return -1;
    }
}
