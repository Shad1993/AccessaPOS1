package com.accessa.ibora.product.Rooms;

public class Table {
    private int  id;
    private String tableName; // Add table name field

    private long roomId;
    private int tableNumber;
    private String tableNumbers;
    private String seatCount;
    private String waiterName;
    private String status;

    // Constructor, getters, and setters
    public Table(int tableNumber, String seatCount,String status) {
        this.tableNumber = tableNumber;
        this.seatCount = seatCount;
        this.status = status;
    }
    public Table(String tableNumbers) {
        this.tableNumbers = tableNumbers;

    }

    public Table(int id, String status) {
        this.id = id;

        this.status = status;
    }
    public Table(String tableNumber, int seatCount, String status, String roomId) {
        this.tableNumber = Integer.parseInt(tableNumber);
        this.seatCount = String.valueOf(seatCount);
        this.status = status;
        this.roomId = Long.parseLong(roomId);  // Initialize roomId
    }
    public int getId() {
        return id;
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
    public String getStatus() {
        return status;
    }
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
