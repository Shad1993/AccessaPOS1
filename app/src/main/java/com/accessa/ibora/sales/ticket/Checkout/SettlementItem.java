package com.accessa.ibora.sales.ticket.Checkout;

import android.os.Parcel;
import android.os.Parcelable;

public class SettlementItem implements Parcelable {
    private String paymentName;
    private double settlementAmount;

    public SettlementItem(String paymentName, double settlementAmount) {
        this.paymentName = paymentName;
        this.settlementAmount = settlementAmount;
    }

    protected SettlementItem(Parcel in) {
        paymentName = in.readString();
        settlementAmount = in.readDouble();
    }

    public static final Parcelable.Creator<SettlementItem> CREATOR = new Creator<SettlementItem>() {
        @Override
        public SettlementItem createFromParcel(Parcel in) {
            return new SettlementItem(in);
        }

        @Override
        public SettlementItem[] newArray(int size) {
            return new SettlementItem[size];
        }
    };

    public String getPaymentName() {
        return paymentName;
    }

    public double getSettlementAmount() {
        return settlementAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentName);
        dest.writeDouble(settlementAmount);
    }
}

