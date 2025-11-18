package com.soheb.atm.model;

public class Transaction {
    private String type;
    private double amount;
    private String dateTime;

    public Transaction(String type, double amount, String dateTime) {
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return dateTime + " | " + type + " | Amount: " + amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDateTime() {
        return dateTime;
    }
}
