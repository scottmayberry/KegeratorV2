package com.example.sctma.kegeratorv1;


/**
 * Created by SMAYBER8 on 7/24/2017.
 */

public class ChargeItem {
    private double amount;
    private String info;
    private String date;

    public ChargeItem()
    {
    }

    public ChargeItem(double amount, String info) {
        this.amount = amount;
        this.info = info;
    }
    public ChargeItem(double amount, String info, String date) {
        this.amount = amount;
        this.info = info;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
