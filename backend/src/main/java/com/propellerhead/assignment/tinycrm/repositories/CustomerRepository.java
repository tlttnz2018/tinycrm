/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.repositories;

import com.propellerhead.assignment.tinycrm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository for persisting Customer to database.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
}
