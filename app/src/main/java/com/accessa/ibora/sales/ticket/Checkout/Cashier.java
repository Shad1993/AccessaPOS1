package com.accessa.ibora.sales.ticket.Checkout;

public class Cashier {
    private int id;
    private String name;

    public Cashier(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // This makes the spinner display the name
    }
}

