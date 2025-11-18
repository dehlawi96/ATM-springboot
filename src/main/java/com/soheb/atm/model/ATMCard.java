package com.soheb.atm.model;

public class ATMCard {
    private String atmNumber;
    private BankAccount linkedAccount;
    private String expiryDate;
    private int atmPin;

    public ATMCard(String atmNumber, BankAccount linkedAccount, String expiryDate, int atmPin) {
        this.atmNumber = atmNumber;
        this.linkedAccount = linkedAccount;
        this.expiryDate = expiryDate;
        this.atmPin = atmPin;
    }

    public BankAccount getLinkedAccount() {
        return linkedAccount;
    }
}
