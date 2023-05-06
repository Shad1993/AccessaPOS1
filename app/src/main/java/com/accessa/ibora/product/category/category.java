package com.accessa.ibora.product.category;

public class category {
    private int id;
    private String name;
    private String description;

    public category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public category(String title, String description, int quantity) {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

