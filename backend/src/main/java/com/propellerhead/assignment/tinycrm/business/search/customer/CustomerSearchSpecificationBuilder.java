/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.business.search.customer;

import com.propellerhead.assignment.tinycrm.domain.Customer;
import com.propellerhead.assignment.tinycrm.domain.LeadStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A builder to add and merge all conditions into one.
 */
public class CustomerSearchSpecificationBuilder {
    private final List<CustomerSearchCriteria> params;

    public CustomerSearchSpecificationBuilder() {
        params = new ArrayList<>();
    }

    /**
     * Add new search condition
     * @param key is field need to search
     * @param operation is operation on searching
     * @param value is value for looking up
     * @return builder for fluent interface.
     */
    public CustomerSearchSpecificationBuilder with(String key, String operation, Object value) {
        Object valueSearch = value;
        if (key.equalsIgnoreCase("status")) {
            valueSearch = LeadStatus.valueOf(String.valueOf(value));
        }
        params.add(new CustomerSearchCriteria(key, operation, valueSearch));
        return this;
    }

    /**
     * Merge all conditions into one.
     *
     * @return all conditions in one.
     */
    public Specification<Customer> build() {
        if (params.size() == 0) {
            return null;
        }

        List<CustomerSearchSpecification> specification = params.stream().map(customerSearchCriteria -> new CustomerSearchSpecification(customerSearchCriteria)).collect(Collectors.toList());

        Specification<Customer> result = specification.get(0);
        for (int i = 1; i < specification.size(); i++) {
            result = result.and(specification.get(i));
        }

        return result;
    }
}
