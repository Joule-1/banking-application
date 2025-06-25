package com.banking_application.controller;

import com.banking_application.entity.Account;
import com.banking_application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> addAccount (@RequestBody Account account) {
        return ResponseEntity.status(201).body(accountService.createAccount(account));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") Long id){
        return ResponseEntity.status(200).body(accountService.getAccount(id));
    }

    @GetMapping(path = "/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("id") Long id){
        return ResponseEntity.status(200).body(accountService.getBalance(id));
    }
}
