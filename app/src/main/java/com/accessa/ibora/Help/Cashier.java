package com.accessa.ibora.Help;

public class Cashier {
    private String id;
    private String name;

    public Cashier(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // Display the name in the spinner
    }
}
