package com.banking.calculator.service;
import com.banking.calculator.model.Account;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private final List<Account> accounts = new ArrayList();

    public Account creatAccount(String accountHolder, String password){
        Account account = new Account(accountHolder, password);
        accounts.add(account);
        return account;
    }

    public Account getAccount(String accountHolder){
        return accounts.stream()
               .filter(account->account.getAccountHolder().equals(accountHolder))
               .findFirst()
               .orElse(null);
    }

    public List<Account> getAllAccounts(){
        return accounts;
    }


}
