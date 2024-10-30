package com.banking.calculator;

import com.banking.calculator.model.Account;
import com.banking.calculator.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
@SpringBootApplication
public class CalculatorApplication implements CommandLineRunner{

	@Autowired
	private AccountService accountService;
	public static void main(String[] args) {
		SpringApplication.run(CalculatorApplication.class, args);
	}

	@Override
	public void run(String... args){
		Scanner scanner = new Scanner(System.in);
		String command;

		System.out.println("Welcome to my Calculator system, Type 'exit' to quit. ");

		while(true){
			System.out.println(">");
			command = scanner.nextLine();

			if(command.equalsIgnoreCase("exit")){
				break;
			}
			else{
				handleCommand(command, scanner);
			}
		}
		scanner.close();
	}

	private handleCommand(String command, Scanner scanner){
		String [] parts = command.split(" ");
		String action = parts[0];

		switch (action.toLowerCase()){
			case "create":
				createAccount(parts, scanner);
				break;
			case "deposit":
				deposit(parts, scanner);
				break;
			case "withdraw":
				withdraw(parts, scanner);
				break;
			case "balance":
				checkBalance(parts);
				break;
			case "list":
				listAccounts();
				break;
			default:
				System.out.println("Unknown command");

		}
	}

	private void createAccount(String[] parts, Scanner scanner){
		if(parts.length<3){
			System.out.println("Usage: create <name> <password>");
			return;
		}

		String accountHolder =parts[1];
		String password = parts[2];
		accountService.creatAccount(accountHolder, password);
		System.out.println("Account created for "+ accountHolder);
	}

	private void deposit(String[] parts, Scanner scanner){
		if(parts.length<3){
			System.out.println("Usage: deposit <name> <amount>");
			return;
		}
		String accountHolder = parts[1];
		double amount = Double.parseDouble(parts[2]);
		Account account = accountService.getAccount(accountHolder);

		if(account !=null){
			account.deposit(amount);
			System.out.println("Deposited "+ amount + " to "+ accountHolder);

		}
		else{
				System.out.println("Account not found");
		}
	}

	private void withdraw(String[] parts, Scanner scanner){
		if(parts.length<3){
			System.out.println("Usage: withderaw <name> <amount> ");
			return;
		}
		String accountHolder = parts[1];
		double amount = Double.parseDouble(parts[2]);
		Account account = accountService.getAccount(accountHolder);

		if(account !=null){
			if(account.withdraw(amount)){
				System.out.println("Withdrew "+ amount+ " from "+ accountHolder);
			}
			else {
				System.out.println("Insufficient funds.");
			}
			else {
				System.out.println("Account not found");
			}
		}

		private void checkBalance(String[] parts){
			if(parts.length < 2){
				System.out.println("Usage: balance <name>");
				return;
			}

			String accountHolder = parts[1];
			Account account = accountService.getAccount(accountHolder);

			if(account !=null){
				System.out.println("Balance for "+ accountHolder+ ": "+ account.getBalance());
			}else{
				System.out.println("Account not found");
			}
		}

		private void listAccounts(){
			for(Account account: accountService.getAllAccounts()){
				System.out.println("Account Holder: "+ account.getAccountHolder() +", Balance: "+ account.getBalance() );
			}
		}
	}

}
