package main.FXMLAddOn;

/**
 * CheckoutEWB2 - main.FXMLAddOn
 *
 * @author AddOnItem
 * @version 1.0
 */
public enum  AddOnItem {
    SHIRT,GLASS;

    /**
     * Returns a string with the name of the item type. If for some reason the item
     * isn't valid, it will return "[INVALID ITEM TYPE]"
     * @return String containing add-on-item name
     */
    @Override
    public String toString() {
        if (this == AddOnItem.SHIRT) return "Shirt";
        if (this == AddOnItem.GLASS) return "Glass";
        return "[INVALID ITEM TYPE]";
    }

    /**
     * Returns the cost of the given add-on item.
     * @return Double with value of price of item
     */
    public double getCost() {
        if (this == AddOnItem.SHIRT) return 10;
        if (this == AddOnItem.GLASS) return 7;
        return -1;
    }

    /**
     * Given a string, returns the correct enumerated type matching the string
     * @param s String to parse
     * @return Correct Type that string is describing
     */
    public static AddOnItem stringToAddOnItem(String s) {

        if (s.equalsIgnoreCase("shirt")) return AddOnItem.SHIRT;
        if (s.equalsIgnoreCase("glass")) return AddOnItem.GLASS;

        return AddOnItem.SHIRT; //Default to this type if not able to parse string
    }

}
