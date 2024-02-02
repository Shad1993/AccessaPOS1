package com.accessa.ibora.MRA;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonData {
    private String newTransactionId;
    private String room;
    private String tableId;
    private double amountSplitted;

    public PersonData(String newTransactionId, String room, String tableId, double amountSplitted) {
        this.newTransactionId = newTransactionId;
        this.room = room;
        this.tableId = tableId;
        this.amountSplitted = amountSplitted;
    }

    public String getNewTransactionId() {
        return newTransactionId;
    }

    public String getRoom() {
        return room;
    }

    public String getTableId() {
        return tableId;
    }

    public double getAmountSplitted() {
        return amountSplitted;
    }
}


