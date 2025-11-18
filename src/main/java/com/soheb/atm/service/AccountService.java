package com.soheb.atm.service;

import com.soheb.atm.model.BankAccount;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {

    // accountNumber -> BankAccount
    private final Map<String, BankAccount> accounts = new ConcurrentHashMap<>();

    // token -> accountNumber
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public AccountService() {
        // seed sample account: accountNumber = "12345678", PIN = 1111
        BankAccount a = new BankAccount("12345678", "Soheb", 1111, 5000.0);
        accounts.put(a.getAccountNumber(), a);
    }

    public Optional<String> login(String accountNumber, int pin) {
        BankAccount acc = accounts.get(accountNumber);
        if (acc != null && acc.getPin() == pin) {
            String token = UUID.randomUUID().toString();
            sessions.put(token, accountNumber);
            return Optional.of(token);
        }
        return Optional.empty();
    }

    public void logout(String token) {
        sessions.remove(token);
    }

    public Optional<BankAccount> getByToken(String token) {
        String acct = sessions.get(token);
        if (acct == null) return Optional.empty();
        return Optional.ofNullable(accounts.get(acct));
    }
}
