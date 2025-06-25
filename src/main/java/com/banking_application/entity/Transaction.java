package com.banking_application.entity;

import com.banking_application.utils.TransactionTypes;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GenericGenerator(name = "transaction-id", strategy = "com.banking_application.utils.CustomIdGenerator")
    @GeneratedValue(generator = "transaction-id")
    @Column(name = "transaction_id")
    private Long transaction_id;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account from_account_id;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account to_account_id;

    @Column(name = "amount", precision = 20, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionTypes transaction_type;

    @Column(name = "timestamps", length = 19)
    private LocalDateTime timestamp;

    public Long getTransaction_id() {
        return transaction_id;
    }

    public Account getFrom_account_id() {
        return from_account_id;
    }

    public void setFrom_account_id(Account from_account_id) {
        this.from_account_id = from_account_id;
    }

    public Account getTo_account_id() {
        return to_account_id;
    }

    public void setTo_account_id(Account to_account_id) {
        this.to_account_id = to_account_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionTypes getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(TransactionTypes transaction_type) {
        this.transaction_type = transaction_type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @PrePersist
    private void createTimestamp() {
        this.timestamp = LocalDateTime.now();
    }
}
