package com.accessa.ibora.ItemsReport;

public class PaymentMethodDataModel {

    private String paymentName;
    private int paymentCount;
    private double totalAmount;

    public PaymentMethodDataModel(String paymentName, int paymentCount, double totalAmount) {
        this.paymentName = paymentName;
        this.paymentCount = paymentCount;
        this.totalAmount = totalAmount;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public int getPaymentCount() {
        return paymentCount;
    }

    public void setPaymentCount(int paymentCount) {
        this.paymentCount = paymentCount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}


