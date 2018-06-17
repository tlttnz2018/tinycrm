/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.business.search.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Value object for storing search string like filter=PROSPECT
 */
@Data
@AllArgsConstructor
public class CustomerSearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
