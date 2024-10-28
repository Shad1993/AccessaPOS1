package com.accessa.ibora.Settings.Rooms;

public class Rooms {
    private long id;
    private String roomName;
    private int tableCount;
    private String cashorId; // Declare the missing fields
    private String lastmodified;
    private String lastmodified1;
    private int seatCount;    // Use this field for seat count

    private String Status;

    public Rooms(String roomName, int tableCount) {
        this.roomName = roomName;
        this.tableCount = tableCount;
    }


    public Rooms(String name, String counter, String cashorId, String lastmodified, String lastmodified1) {
        // For example, initializing all fields based on the provided parameters
        this.roomName = name;
        // Assuming counter is a string, if it's an integer, you might need to convert it
        this.tableCount = Integer.parseInt(counter);
        // Initialize other fields similarly

        this.cashorId = cashorId;
         this.lastmodified = lastmodified;
         this.lastmodified1 = lastmodified1;
    }




    // Getter and Setter for id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Getter and Setter for roomName
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    // Getter and Setter for tableCount
    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
    // Getters and Setters for seatCount
    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

}
