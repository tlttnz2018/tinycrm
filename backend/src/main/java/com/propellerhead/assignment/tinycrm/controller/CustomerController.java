/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.controller;

import com.propellerhead.assignment.tinycrm.business.search.customer.CustomerSearchSpecificationBuilder;
import com.propellerhead.assignment.tinycrm.business.exception.ResourceNotFoundException;
import com.propellerhead.assignment.tinycrm.domain.Customer;
import com.propellerhead.assignment.tinycrm.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * REST controller for all operations related to customer
 */
@RestController
@RequestMapping("/api")
public class CustomerController {
    public static final String CUSTOMER_STATUS_DB_FIELD = "status";
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Get all customers with conditions specified below
     * @param field which field of customer to search
     * @param queryText search text for a field in customer
     * @param filter which status of customer need to retain
     * @param pageable allow pagination for customer list
     * @return a page of customer
     */
    @GetMapping("/customers")
    public Page<Customer> getAllCustomers(
            @RequestParam(value = "field", required = false) String field,
            @RequestParam(value = "query", required = false) String queryText,
            @RequestParam(value = "filter", required = false) String filter,
            Pageable pageable) {

        //Transform from query string to search specification
        CustomerSearchSpecificationBuilder customerSearchSpecificationBuilder = new CustomerSearchSpecificationBuilder();
        if(!isEmpty(queryText) && !isEmpty(field)) {
            customerSearchSpecificationBuilder.with(field, ":", queryText);
        }

        if (!isEmpty(filter)) {
            customerSearchSpecificationBuilder.with(CUSTOMER_STATUS_DB_FIELD, ":", filter);
        }

        return customerRepository.findAll(customerSearchSpecificationBuilder.build(), pageable);
    }

    /**
     * Get a specific detail of a customer
     *
     * @param customerId id of customer need to get detail
     * @return customer detail information
     */
    @GetMapping("/customers/{customerId}")
    public Customer getCustomer(@PathVariable (value = "customerId") Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("CustomerId " + customerId + " not found"));
    }

    /**
     * Create customer based on specification below
     *
     * @param customer information need to store
     * @return a persisted customer. It will contain update customerID
     */
    @PostMapping("/customers")
    public Customer createCustomer(@Valid @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Update customer based on specification below
     *
     * @param customerId id of customer need to update information
     * @param customerRequest information which are changed need to be updated.
     * @return customer with updated information
     */
    @PutMapping("/customers/{customerId}")
    public Customer updateCustomer(@PathVariable Long customerId, @Valid @RequestBody Customer customerRequest) {
        return customerRepository.findById(customerId).map(customer -> {
            customer.setId(customerRequest.getId());
            customer.setName(customerRequest.getName());
            customer.setStatus(customerRequest.getStatus());
            customer.setCreatedAt(customerRequest.getCreatedAt());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new ResourceNotFoundException("CustomerId " + customerId + " not found"));
    }
}
