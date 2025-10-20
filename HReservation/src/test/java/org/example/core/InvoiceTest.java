package org.example.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

    @Test
    void addLineItemsTest(){
        String itemName = "Room Rate";
        double itemPrice = 147.00;

        Invoice invoice = new Invoice();
        invoice.addLineItems(itemName, itemPrice);

        LineItem lineItem = invoice.getLineItems().get(0);

        Assertions.assertEquals(itemName, lineItem.getItemName());
        Assertions.assertEquals(itemPrice, lineItem.getItemPrice());
        System.out.println("[SUCCESS] addLineItems unit test passed.");
    }

    @Test
    void setInvoiceAttributesTest(){
        int id = 123;
        double balance = 13.99;
        double total = 17.99;
        String status = "Paid";

        Invoice invoice = new Invoice();
        invoice.setInvoiceID(id);
        invoice.setBalance(balance);
        invoice.setStatus(status);
        invoice.setTotal(total);

        Assertions.assertEquals(id, invoice.getInvoiceID());
        Assertions.assertEquals(balance, invoice.getBalance());
        Assertions.assertEquals(status, invoice.getStatus());
        Assertions.assertEquals(total, invoice.getTotal());
        System.out.println("[SUCCESS] setInvoiceAttributesTest unit test passed.");
    }

    @Test
    void makeOverPaymentTest() {
        double balance = 99.01;
        double payment = 100.00;

        Invoice invoice = new Invoice();
        invoice.setBalance(balance);

        invoice.makePayment(payment);

        Assertions.assertEquals(balance, invoice.getBalance());
        System.out.println("[Success] makeOverPaymentTest unit test passed.\n\tExpected Balance:\t$" + balance + "\n\tBalance:\t\t\t$" + invoice.getBalance());
    }

    @Test
    void makeUnderPaymentTest() {
        double balance = 99.01;
        double payment = 99.00;

        Invoice invoice = new Invoice();
        invoice.setBalance(balance);

        invoice.makePayment(payment);

        Assertions.assertEquals(balance - payment, invoice.getBalance());
        System.out.println("[Success] makeUnderPaymentTest unit test passed.\n\tExpected Balance:\t$" + (balance - payment) + "\n\tBalance:\t\t\t$" + invoice.getBalance());
    }

    @Test
    void makeFullPaymentTest(){
        double balance = 99.01;
        double payment = 99.01;

        Invoice invoice = new Invoice();
        invoice.setBalance(balance);

        invoice.makePayment(payment);

        Assertions.assertEquals(0,invoice.getBalance());
        System.out.println("[Success] makeFullPaymentTest unit test passed.\n\tExpected Balance:\t$0.00\n\tBalance:\t\t\t$" + invoice.getBalance());
    }

}