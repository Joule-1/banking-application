package com.banking_application.entity;

import com.banking_application.utils.AccountTypes;
import com.banking_application.utils.CustomAccountNumberGenerator;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "account")

public class Account {

    @Id
    @GenericGenerator(name = "custom-id", strategy = "com.banking_application.utils.CustomIdGenerator")
    @GeneratedValue(generator = "custom-id")
    @Column(name = "account_id")
    private Long account_id;

    @Column(name = "account_number")
    private String account_number;

    @Column(name = "balance", precision = 20, scale = 2)
    private BigDecimal account_balance;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountTypes account_type;

    public Long getAccount_id() {
        return account_id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public BigDecimal getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(BigDecimal account_balance) {
        this.account_balance = account_balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AccountTypes getAccount_type() {
        return account_type;
    }

    public void setAccount_type(AccountTypes account_type) {
        this.account_type = account_type;
    }

    @PrePersist
    private void assignAccountNumber(){
        this.account_number = CustomAccountNumberGenerator.accountNumberGenerator();
    }
}
