package com.banking_application.controller;

import com.banking_application.dto.AmountDTO;
import com.banking_application.entity.Transaction;
import com.banking_application.service.AccountService;
import com.banking_application.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/accounts")
public class TransactionController {

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @PostMapping(path = "/{id}/deposit")
    public ResponseEntity<Transaction> commitDeposit(@RequestBody AmountDTO amountDTO, @PathVariable("id") Long id){
        return ResponseEntity.status(201).body(accountService.deposit(amountDTO.getAmount(), id));
    }

    @PostMapping(path = "/{id}/withdraw")
    public ResponseEntity<Transaction> commitWithdraw(@RequestBody AmountDTO amountDTO, @PathVariable("id") Long id){
        return ResponseEntity.status(201).body(accountService.withdraw(amountDTO.getAmount(), id));
    }

    @PostMapping(path = "/{fromId}/transfer/{toId}")
    public ResponseEntity<Transaction> commitTransfer(@PathVariable("fromId") Long fromId, @PathVariable("toId") Long toId, @RequestBody AmountDTO amountDTO){
        return ResponseEntity.status(201).body(transactionService.transfer(fromId, toId, amountDTO.getAmount()));
    }

    @GetMapping(path = "/{id}/transactions")
    public ResponseEntity<List<Transaction>> showTransactions(@PathVariable("id") Long accountId){
        return ResponseEntity.status(200).body(accountService.getAccountTransactionsByAccountId(accountId));
    }

}
