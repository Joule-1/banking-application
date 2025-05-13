package com.banking_application.service;

import com.banking_application.dao.AccountDAO;
import com.banking_application.dao.CustomerDAO;
import com.banking_application.entity.Account;
import com.banking_application.entity.Customer;
import com.banking_application.exception.ExceptionResponse;
import com.banking_application.utils.AccountTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private AccountDAO accountDAO;

    private static final double INITIAL_BALANCE = 1500.00;

    @Transactional
    public Customer createCustomer(Customer customer){

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new ExceptionResponse(400, "Customer name cannot be empty.");
        }

        if (customer.getEmail() == null || !customer.getEmail().trim().matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            throw new ExceptionResponse(400, "Invalid email format.");
        }

        if (customer.getPhone_number() == null || !customer.getPhone_number().matches("^\\+?[0-9]{7,15}$")) {
            throw new ExceptionResponse(400, "Invalid phone number.");
        }

        if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
            throw new ExceptionResponse(400, "Address cannot be empty.");
        }

        Customer savedCustomer = customerDAO.save(customer);

        Account account = new Account();
        account.setAccount_balance(INITIAL_BALANCE);
        account.setAccount_type(AccountTypes.CHECKING);
        account.setCustomer(savedCustomer);
        accountDAO.save(account);

        savedCustomer.setAccounts(List.of(account));

        return savedCustomer;
    }

    public Customer getCustomer(Long customerId){
        return customerDAO.findById(customerId).orElseThrow(() -> new ExceptionResponse(404, "Account not found with ID: " + customerId));
    }

    public Iterable<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

}
