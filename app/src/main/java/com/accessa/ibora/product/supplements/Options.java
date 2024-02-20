package com.accessa.ibora.product.supplements;

public class Options {
    private int id;
    private String CatName;
    private String Color;


    public Options(int id, String CatName, String Color) {
        this.id = id;
        this.CatName = CatName;
        this.Color = Color;

    }

    public Options(String title, String description, int quantity) {
    }

    public Options(String id, String CatName, String Color) {
    }

    public  int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return CatName;
    }

    public void setNames(String CatName) {
        this.CatName = CatName;
    }


    public void setCatName(String name) {
        this.CatName = CatName;
    }

    public void setColor(String description) {
        this.Color = Color;
    }

}

