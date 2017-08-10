package com.example.sctma.kegeratorv1;


/**
 * Created by SMAYBER8 on 7/24/2017.
 */

public class ChargeItem {
    private double amount;
    private String info;
    private long time;
    private String username;

    public ChargeItem()
    {
    }

    public ChargeItem(double amount, String info, Long time, String username) {
        this.amount = amount;
        this.info = info;
        this.time = time;
        this.username = username;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
