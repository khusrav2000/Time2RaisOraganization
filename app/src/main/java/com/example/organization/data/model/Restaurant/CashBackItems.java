package com.example.organization.data.model.Restaurant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CashBackItems {
    @SerializedName("Amount")
    @Expose
    private double amount;

    @SerializedName("Percent")
    @Expose
    private double percent;

    public CashBackItems(double amount, double percent) {
        this.amount = amount;
        this.percent = percent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "CashBackItems{" +
                "amount=" + amount +
                ", percent=" + percent +
                '}';
    }
}
