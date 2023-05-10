package com.accessa.ibora.product.category;

public class Category {
    private int id;
    private String CatName;
    private String Color;


    public Category(int id, String CatName, String Color) {
        this.id = id;
        this.CatName = CatName;
        this.Color = Color;

    }

    public Category(String title, String description, int quantity) {
    }

    public Category(String id, String CatName, String Color) {
    }

    public  int getId() {
        return id;
    }

    public String getCatName() {
        return CatName;
    }

    public String getColor() {
        return Color;
    }


    public void setCatName(String name) {
        this.CatName = CatName;
    }

    public void setColor(String description) {
        this.Color = Color;
    }

}

