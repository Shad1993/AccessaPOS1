package com.accessa.ibora.sales.ticket;

import android.content.Context;
import android.database.Cursor;

import com.accessa.ibora.product.items.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private String ticketNumber;
    private String ticketTitle;

    public Ticket(String ticketNumber, String ticketTitle) {
        this.ticketNumber = ticketNumber;
        this.ticketTitle = ticketTitle;
    }

    public Ticket(String itemName, double itemPrice, int itemQuantity) {
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }
}
