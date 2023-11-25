package com.accessa.ibora.product.Rooms;

public class Table {
    private long id;
    private long roomId;
    private int tableNumber;
    private String tableNumbers;
    private String seatCount;
    private String waiterName;

    // Constructor, getters, and setters
    public Table(int tableNumber, String seatCount) {
        this.tableNumber = tableNumber;
        this.seatCount = seatCount;
    }
    public Table(String tableNumbers) {
        this.tableNumbers = tableNumbers;

    }



    public int getTableNumber() {
        return tableNumber;
    }
    public String getTableNumbers() {
        return tableNumbers;
    }
    public String getSeatCount() {
        return seatCount;
    }

}
