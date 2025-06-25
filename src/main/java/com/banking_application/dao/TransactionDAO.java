package com.banking_application.dao;

import com.banking_application.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionDAO extends CrudRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.from_account_id.account_id = :accountId OR t.to_account_id.account_id = :accountId")
    List<Transaction> findAllTransactionsByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.amount >= :amount")
    List<Transaction> findAllTransactionsByAmount(@Param("amount") BigDecimal amount);
}
