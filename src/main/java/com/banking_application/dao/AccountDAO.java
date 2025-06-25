package com.banking_application.dao;

import org.springframework.data.repository.CrudRepository;

import com.banking_application.entity.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDAO extends CrudRepository<Account, Long> {
}
