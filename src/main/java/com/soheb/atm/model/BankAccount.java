package com.soheb.atm.model;

import java.util.ArrayList;

public class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private int pin;
    private double balance;
    private ArrayList<Transaction> history = new ArrayList<>();

    public BankAccount(String accountNumber, String accountHolder, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.pin = pin;
        this.balance = balance;
    }

    public double checkBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public int getPin() {
        return pin;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited amount " + amount);
            addTransaction("DEPOSIT", amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Withdraw " + amount);
            addTransaction("WITHDRAW", amount);
            return true;
        }
        return false;
    }

    public void addTransaction(String type, double amount) {
        String time = java.time.LocalDateTime.now().toString();
        Transaction t = new Transaction(type, amount, time);
        history.add(t);
    }

    public void printHistory() {
        if (history.isEmpty()) {
            System.out.println("No Transaction yet.");
            return;
        }

        System.out.println("\n=== TRANSACTION HISTORY ===");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }

    public ArrayList<Transaction> getHistory() {
        return history;
    }
}
