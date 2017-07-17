package com.example.sctma.kegeratorv1;

/**
 * Created by sctma on 7/2/2017.
 */

public class Balance {
    private double balance;

    public Balance() {
    }

    public Balance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addToBalance(double adder)
    {
        balance += adder;
    }
}
