package com.banking_application.service;

import com.banking_application.dao.AccountDAO;
import com.banking_application.dao.CustomerDAO;
import com.banking_application.dao.TransactionDAO;
import com.banking_application.entity.Account;
import com.banking_application.entity.Customer;
import com.banking_application.entity.Transaction;
import com.banking_application.exception.ApiException;
import com.banking_application.utils.AccountTypes;
import com.banking_application.utils.TransactionTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumSet;
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
            throw new ApiException(400, "Customer ID is required to create an account");
        }

        if(account.getAccount_balance() == null || account.getAccount_balance().compareTo(BigDecimal.ZERO) <= 0){
            throw new ApiException(400, "Account balance can't be null or equal or below zero");
        }

        if (!EnumSet.of(AccountTypes.CHECKING, AccountTypes.SAVINGS, AccountTypes.FIXED_DEPOSIT)
                .contains(account.getAccount_type())) {
            throw new ApiException(400, "Invalid account type");
        }

        Long customerId = account.getCustomer().getCustomer_id();
        Customer customer = customerDAO.findById(customerId)
                .orElseThrow(() -> new ApiException(404, "Customer not found with ID: " + customerId));

        account.setCustomer(customer);

        return accountDAO.save(account);
    }

    public Account getAccount(Long accountId){

        if (accountId == null) {
            throw new ApiException(400, "Account ID entered is incorrect or doesn't exist");
        }

        return accountDAO.findById(accountId).orElseThrow(() -> new ApiException(404, "Account ID entered is incorrect or doesn't exist"));
    }

    public BigDecimal getBalance(Long accountId){

        if (accountId == null) {
            throw new ApiException(400, "Account ID entered is incorrect or doesn't exist");
        }

        Account accountRef = accountDAO.findById(accountId).orElseThrow(() -> new ApiException( 404, "Account not found with ID: " + accountId));
        return accountRef.getAccount_balance();
    }

    @Transactional
    public Transaction deposit(BigDecimal amount, long accountId){

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(404, "Deposit amount must be greater than zero");
        }

        Account accountRef = accountDAO.findById(accountId).orElseThrow(() -> new ApiException(404, "Account not found with ID: " + accountId));
        BigDecimal accountBalance = accountRef.getAccount_balance();
        accountRef.setAccount_balance(accountBalance.add(amount));
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
    public Transaction withdraw(BigDecimal amount, long accountId){

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(400, "Withdrawal amount must be greater than zero");
        }

        Account accountRef = accountDAO.findById(accountId).orElseThrow(() -> new ApiException(404, "AccountID entered is incorrect or doesn't exist"));
        BigDecimal accountBalance = accountRef.getAccount_balance();

        if (amount.compareTo(accountBalance) > 0) {
            throw new ApiException(400, "Insufficient funds in account for withdrawal");
        }

        accountRef.setAccount_balance(accountBalance.subtract(amount));
        accountDAO.save(accountRef);

        Transaction transaction = new Transaction();
        transaction.setFrom_account_id(accountRef);
        transaction.setTo_account_id(accountRef);
        transaction.setAmount(amount);
        transaction.setTransaction_type(TransactionTypes.WITHDRAWAL);
        transactionDAO.save(transaction);

        return transaction;
    }

    public List<Transaction> getAccountTransactionsByAccountId(Long accountId){

        if(accountId == null)
            throw new ApiException(400, "Account ID entered is incorrect or doesn't exist");

        accountDAO.findById(accountId).orElseThrow(() -> new ApiException(404, "AccountID entered is incorrect or doesn't exist"));

        return transactionDAO.findAllTransactionsByAccountId(accountId);
    }

    public List<Transaction> getAccountTransactionsByAmount(BigDecimal amount){

        if(amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw new ApiException(400, "Amount must be non-null and non-negative");

        return transactionDAO.findAllTransactionsByAmount(amount);
    }
}
