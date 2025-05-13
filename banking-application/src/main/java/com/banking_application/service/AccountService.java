package com.banking_application.service;

import com.banking_application.dao.AccountDAO;
import com.banking_application.dao.CustomerDAO;
import com.banking_application.dao.TransactionDAO;
import com.banking_application.entity.Account;
import com.banking_application.entity.Customer;
import com.banking_application.entity.Transaction;
import com.banking_application.exception.ExceptionResponse;
import com.banking_application.utils.TransactionTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    TransactionDAO transactionDAO;

    public Account createAccount(Account account){

        if (account.getCustomer() == null || account.getCustomer().getCustomer_id() == null) {
            throw new ExceptionResponse(400, "Customer ID is required to create an account");
        }

        Long customerId = account.getCustomer().getCustomer_id();
        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new ExceptionResponse(404, "Customer not found with ID: " + customerId));

        account.setCustomer(customer);

        return accountDAO.save(account);
    }

    public Account getAccount(Long accountId){

        if (accountId == null) {
            throw new ExceptionResponse(400, "Account ID entered is incorrect or doesn't exist");
        }

        return accountDAO.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Double getBalance(Long accountId){

        if (accountId == null) {
            throw new ExceptionResponse(400, "Account ID entered is incorrect or doesn't exist");
        }

        Account accountRef = accountDAO.findById(accountId).orElseThrow(() -> new ExceptionResponse( 404, "Account not found with ID: " + accountId));
        return accountRef.getAccount_balance();
    }

    @Transactional
    public Transaction deposit(double amount, long accountId){

        if (amount <= 0) {
            throw new ExceptionResponse(404, "Deposit amount must be greater than zero");
        }

        Account accountRef = accountDAO.findById(accountId).orElseThrow(() -> new ExceptionResponse(404, "Account not found with ID: " + accountId));
        double accountBalance = accountRef.getAccount_balance();
        accountRef.setAccount_balance(accountBalance + amount);
        accountDAO.save(accountRef);

        Transaction transaction = new Transaction();
        transaction.setFrom_account_id(accountRef);
        transaction.setTo_account_id(accountRef);
        transaction.setAmount(amount);
        transaction.setTransaction_type(TransactionTypes.DEPOSIT);
        transactionDAO.save(transaction);

        return transaction;
    }

    @Transactional
    public Transaction withdraw(double amount, long accountId){

        if (amount <= 0) {
            throw new ExceptionResponse(400, "Withdrawal amount must be greater than zero");
        }

        Account accountRef = accountDAO.findById(accountId).orElseThrow(() -> new ExceptionResponse(404, "AccountID entered is incorrect or doesn't exist"));
        double accountBalance = accountRef.getAccount_balance();

        if (amount > accountBalance) {
            throw new ExceptionResponse(400, "Insufficient funds in account for withdrawal");
        }

        accountRef.setAccount_balance(accountBalance - amount);
        accountDAO.save(accountRef);

        Transaction transaction = new Transaction();
        transaction.setFrom_account_id(accountRef);
        transaction.setTo_account_id(accountRef);
        transaction.setAmount(amount);
        transaction.setTransaction_type(TransactionTypes.WITHDRAWAL);
        transactionDAO.save(transaction);

        return transaction;
    }

    public List<Transaction> getAccountTransactions(Long accountId){

        if(accountId == null)
            throw new ExceptionResponse(400, "Account ID entered is incorrect or doesn't exist");

        accountDAO.findById(accountId).orElseThrow(() -> new ExceptionResponse(404, "AccountID entered is incorrect or doesn't exist"));

        return transactionDAO.findAllTransactionsByAccountId(accountId);
    }
}
