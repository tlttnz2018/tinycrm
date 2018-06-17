/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.business.search.customer;

import com.propellerhead.assignment.tinycrm.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Implementing specification for searching customer.
 */
@Data
@AllArgsConstructor
public class CustomerSearchSpecification implements Specification<Customer> {
    private CustomerSearchCriteria customerSearchCriteria;

    /**
     * Convert from something like field=name&query=en to search predicate
     *
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder a builder to build search predicate
     * @return search predicate from CustomerSearchCriteria
     */
    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (customerSearchCriteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get(customerSearchCriteria.getKey()), customerSearchCriteria.getValue().toString());
        }
        else if (customerSearchCriteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get(customerSearchCriteria.getKey()), customerSearchCriteria.getValue().toString());
        }
        else if (customerSearchCriteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(customerSearchCriteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        root.get(customerSearchCriteria.getKey()), "%" + customerSearchCriteria.getValue() + "%");
            } else {
                return criteriaBuilder.equal(root.get(customerSearchCriteria.getKey()), customerSearchCriteria.getValue());
            }
        }
        return null;
    }
}
