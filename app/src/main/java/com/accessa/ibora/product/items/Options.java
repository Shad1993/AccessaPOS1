package com.accessa.ibora.product.items;

public class Options {
    private long OptionsId;

    private String Optionsname;


    public Options(long OptionsId, String Optionsname) {
        this.OptionsId = OptionsId;
        this.Optionsname = Optionsname;

    }

    public long getOptionId() {
        return OptionsId;
    }

    public String getOptionname() {
        return Optionsname;
    }


}

