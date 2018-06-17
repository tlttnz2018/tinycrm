/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Note information
 */
@Data
@Entity
public class Note {
    private @Id @GeneratedValue Long id;

    // Only for generating database with customer_id
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @NotNull(message = "Note subject must not be null")
    @Size(min = 1, message = "Note subject must have at least 1 character")
    @Size(max = 255, message = "Note subject cannot be larger than 255 characters")
    private String noteSubject;

    @Lob
    private String noteBody; //Can be HTML or Markdown

    private Date createdAt;

    @PrePersist
    void createAt() {
        this.createdAt = new Date();
    }
}
