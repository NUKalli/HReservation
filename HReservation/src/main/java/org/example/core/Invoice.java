package org.example.core;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int invoiceID;
    private double total;
    private double balance;
    private String status;
    private List<LineItem> lineItems = new ArrayList<LineItem>();

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }
    public int getInvoiceID() {
        return this.invoiceID;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    public double getTotal() {
        return this.total;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public double getBalance() {
        return this.balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }

    public void addLineItems(String itemName, double itemPrice) {
        LineItem lineItem = new LineItem(itemName, itemPrice);
        this.lineItems.add(lineItem);
    }
    public List<LineItem> getLineItems(){
        return this.lineItems;
    }

    public void removeLineItem(int lineItemID) {
        this.lineItems.remove(lineItemID);
    }

    public void makePayment(double amount) {
        try {
            if (this.balance - amount < 0){
                System.out.println("[Error] Over payment. Remaining balance: $" + this.balance);
            }
            else {
                this.balance = this.balance - amount;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
