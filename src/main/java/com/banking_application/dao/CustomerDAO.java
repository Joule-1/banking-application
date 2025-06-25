package com.banking_application.dao;

import org.springframework.data.repository.CrudRepository;

import com.banking_application.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDAO extends CrudRepository<Customer, Long> {
}
