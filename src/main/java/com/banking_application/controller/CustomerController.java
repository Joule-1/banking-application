package com.banking_application.controller;

import com.banking_application.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking_application.entity.Customer;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> addCustomer (@RequestBody Customer customer){
        return ResponseEntity.status(201).body(customerService.createCustomer(customer));
    }

    @GetMapping (path = "/{id}")
    public ResponseEntity<Customer> getCustomer (@PathVariable("id") Long id){
        return ResponseEntity.status(200).body(customerService.getCustomer(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<Customer>> getAllCustomers () {
        return ResponseEntity.status(200).body(customerService.getAllCustomers());
    }
}