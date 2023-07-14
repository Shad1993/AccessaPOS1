package com.accessa.ibora.Report;

public class PaymentItem {
    private String paymentName;
    private double amountPaid;

    public PaymentItem(String paymentName, double amountPaid) {
        this.paymentName = paymentName;
        this.amountPaid = amountPaid;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public double getAmountPaid() {
        return amountPaid;
    }
}
