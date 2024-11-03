package com.banking.calculator.service;

import com.banking.calculator.model.Account;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    // Specify the type of the list to ensure type safety
    private final List<Account> accounts = new ArrayList<>();

    // Correct method name to createAccount
    public Account createAccount(String accountHolder, String password) {
        Account account = new Account(accountHolder, password);
        accounts.add(account);
        return account;
    }

    // Method to retrieve an account by account holder's name
    public Account getAccount(String accountHolder) {
        return accounts.stream()
                .filter(account -> account.getAccountHolder().equals(accountHolder))
                .findFirst()
                .orElse(null);
    }

    // Method to retrieve all accounts
    public List<Account> getAllAccounts() {
        return accounts;
    }
}
