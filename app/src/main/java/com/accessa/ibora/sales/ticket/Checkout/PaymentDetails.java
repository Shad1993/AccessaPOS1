package com.accessa.ibora.sales.ticket.Checkout;

public class PaymentDetails {
    private String paymentName;
    private double amount;

    public PaymentDetails(String paymentName) {
        this.paymentName = paymentName;

    }

    public String getPaymentName() {
        return paymentName;
    }

    public double getAmount() {
        return amount;
    }
}
