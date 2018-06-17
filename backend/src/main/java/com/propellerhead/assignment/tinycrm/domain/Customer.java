/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * A simple customer information
 */
@Data
@Entity
public class Customer {
    // id of customer
    private @Id @GeneratedValue Long id;

    @NotNull(message = "Customer name must not be null")
    @Size(min = 1, message = "Customer name must have at least 1 character")
    private String name;

    @NotNull(message = "Customer status must not be null")
    private LeadStatus status;

    // Contact information
    private String address;
    private String phone;

    @NotNull(message = "Customer email must not be null")
    @Size(min = 1, message = "Customer email must have at least 3 characters")
    private String email;

    private String zipCode;

    private Date createdAt;

    @PrePersist
    void createAt() {
        this.createdAt = new Date();
    }
}
