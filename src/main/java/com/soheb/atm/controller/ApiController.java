package com.soheb.atm.controller;

import com.soheb.atm.model.Transaction;
import com.soheb.atm.model.BankAccount;
import com.soheb.atm.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final AccountService svc;

    public ApiController(AccountService svc) {
        this.svc = svc;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        String accountNumber = body.get("cardNumber").toString();
        int pin = Integer.parseInt(body.get("pin").toString());

        Optional<String> tokenOpt = svc.login(accountNumber, pin);
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok(Map.<String, Object>of("token", tokenOpt.get()));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
        }
    }

    private String getTokenFromHeader(String auth) {
        if (auth == null) return null;
        if (auth.startsWith("Bearer ")) return auth.substring(7);
        return null;
    }

    @GetMapping("/account/balance")
    public ResponseEntity<?> balance(@RequestHeader(value = "Authorization", required = false) String auth) {
        String token = getTokenFromHeader(auth);
        Optional<BankAccount> accOpt = svc.getByToken(token);

        if (accOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
        }

        BankAccount acc = accOpt.get();
        // Return numeric balance as JSON number (Map<String, Object>)
        return ResponseEntity.ok(Map.<String, Object>of("balance", acc.checkBalance()));
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> deposit(@RequestHeader(value = "Authorization", required = false) String auth,
                                     @RequestBody Map<String, Object> body) {
        String token = getTokenFromHeader(auth);
        Optional<BankAccount> accOpt = svc.getByToken(token);
        if (accOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
        }

        double amount;
        try {
            amount = Double.parseDouble(body.get("amount").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid amount"));
        }

        BankAccount acc = accOpt.get();
        acc.deposit(amount);
        return ResponseEntity.ok(Map.<String, Object>of("balance", acc.checkBalance()));
    }

    @PostMapping("/account/withdraw")
    public ResponseEntity<?> withdraw(@RequestHeader(value = "Authorization", required = false) String auth,
                                      @RequestBody Map<String, Object> body) {
        String token = getTokenFromHeader(auth);
        Optional<BankAccount> accOpt = svc.getByToken(token);
        if (accOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
        }

        double amount;
        try {
            amount = Double.parseDouble(body.get("amount").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid amount"));
        }

        BankAccount acc = accOpt.get();
        boolean ok = acc.withdraw(amount);
        if (!ok) {
            return ResponseEntity.badRequest().body(Map.of("error", "insufficient"));
        }
        return ResponseEntity.ok(Map.<String, Object>of("balance", acc.checkBalance()));
    }

    @GetMapping("/account/transactions")
    public ResponseEntity<?> transactions(@RequestHeader(value = "Authorization", required = false) String auth) {
        String token = getTokenFromHeader(auth);
        Optional<BankAccount> accOpt = svc.getByToken(token);
        if (accOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "unauthorized"));
        }

        List<Transaction> history = accOpt.get().getHistory();
        return ResponseEntity.ok(history);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String auth) {
        String token = getTokenFromHeader(auth);
        svc.logout(token);
        return ResponseEntity.noContent().build();
    }
}
