package com.accessa.ibora.MRA;

import java.io.Serializable;

public class ItemData implements Serializable {
    private String id;
    private String name;

    public ItemData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
