package com.accessa.ibora.product.Rooms;

public class Room {
    private int id;
    private String roomName;
    private int tableCount;

    public Room(int id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }

    public int getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        return roomName; // This will be displayed in the spinner
    }
}
