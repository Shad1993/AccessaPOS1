package com.accessa.ibora.sales.ticket;

public class Ticket {
    private String ticketNumber;
    private String ticketTitle;

    public Ticket(String ticketNumber, String ticketTitle) {
        this.ticketNumber = ticketNumber;
        this.ticketTitle = ticketTitle;
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
