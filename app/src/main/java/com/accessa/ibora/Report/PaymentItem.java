package com.accessa.ibora.Report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentItem {
    private String paymentName;
    private double amountPaid;
    private Date transactionDate;

    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public PaymentItem(String paymentName, double amountPaid, Date transactionDate) {
        this.paymentName = paymentName;
        this.amountPaid = amountPaid;
        this.transactionDate = transactionDate;
    }


    public String getPaymentName() {
        return paymentName;
    }

    public double getAmountPaid() {
        return amountPaid;
    }
    public Date getTransactionDate() {
        return transactionDate;
    }
}
