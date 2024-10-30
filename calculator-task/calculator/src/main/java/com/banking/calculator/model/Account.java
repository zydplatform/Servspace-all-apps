package com.banking.calculator.model;

public class Account {

    private String accountHolder;
    private double balance;
    private String password;
}

public Account(String accountHolder, String password){
    this.accountHolder =accountHolder;
    this.password = password;
    this.balance = 0.0;
}

public String getAccountHolder(){
    return accountHolder;
}

public double getBalance(){
    return balance;
}

public void deposit(double amount){
    if(amount>0){
        balance+=amount;
    }
}

public boolean withdraw(double amount){
    if(amount > 0 && amount<= balance){
        balance -=amount;
        return true;
    }
    return false;
}

public boolean authenticate(String password){

    return this.password.equals(password);

}