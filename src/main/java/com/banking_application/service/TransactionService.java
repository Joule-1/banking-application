package com.banking_application.service;

import com.banking_application.dao.AccountDAO;
import com.banking_application.dao.TransactionDAO;
import com.banking_application.entity.Account;
import com.banking_application.entity.Transaction;
import com.banking_application.exception.ApiException;
import com.banking_application.utils.TransactionTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    TransactionDAO transactionDAO;

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount){

        if (fromAccountId == null || toAccountId == null) {
            throw new ApiException(400, "Both sender and receiver account IDs must be provided.");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new ApiException(400, "Sender and receiver accounts must be different.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(400, "Transfer amount must be greater than zero.");
        }

        Account sender = accountDAO.findById(fromAccountId)
                .orElseThrow(() -> new ApiException(404, "Sender account not found with ID: " + fromAccountId));

        if (sender.getAccount_balance().compareTo(amount) < 0) {
            throw new ApiException(400, "Insufficient funds in sender account.");
        }

        Account receiver = accountDAO.findById(toAccountId)
                .orElseThrow(() -> new ApiException(404, "Receiver account not found with ID: " + toAccountId));


        sender.setAccount_balance(sender.getAccount_balance().subtract(amount));
        receiver.setAccount_balance(receiver.getAccount_balance().add(amount));

        accountDAO.save(sender);
        accountDAO.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setFrom_account_id(sender);
        transaction.setTo_account_id(receiver);
        transaction.setAmount(amount);
        transaction.setTransaction_type(TransactionTypes.TRANSFER);
        transactionDAO.save(transaction);

        return transaction;


    }

}
