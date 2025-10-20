package org.example.core;

public class LineItem {

    private String itemName;
    private double itemPrice;

    public LineItem(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemName(){
        return this.itemName;
    }

    public double getItemPrice(){
        return this.itemPrice;
    }
}
