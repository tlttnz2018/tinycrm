package com.propellerhead.assignment.tinycrm.domain;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.propellerhead.assignment.tinycrm.domain.LeadStatus.PROSPECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomerTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void testCustomerValidMustPassValidation() {
        //prepare
        Customer customer = new Customer();
        customer.setName("Test customer");
        customer.setStatus(PROSPECT);
        customer.setEmail("testcustomer@test.com");

        //run
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        //assert
        assertTrue(violations.toString(), violations.isEmpty());
    }

    @Test
    public void testCustomerNoNameMustFailedValidation() {
        //prepare
        Customer customer = new Customer();
        customer.setStatus(PROSPECT);
        customer.setEmail("testcustomer@test.com");

        //run
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        //assert
        assertEquals(violations.size(), 1);
        ConstraintViolation violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Customer name must not be null", violation.getMessage());
    }

    @Test
    public void testCustomerEmptyNameMustFailedValidation() {
        //prepare
        Customer customer = new Customer();
        customer.setName("");
        customer.setStatus(PROSPECT);
        customer.setEmail("testcustomer@test.com");

        //run
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        //assert
        assertEquals(violations.size(), 1);
        ConstraintViolation violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Customer name must have at least 1 character", violation.getMessage());
    }

    @Test
    public void testCustomerNoStatusMustFailedValidation() {
        //prepare
        Customer customer = new Customer();
        customer.setName("Test customer");
        customer.setEmail("testcustomer@test.com");

        //run
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        //assert
        assertEquals(violations.size(), 1);
        ConstraintViolation violation = violations.iterator().next();
        assertEquals("status", violation.getPropertyPath().toString());
        assertEquals("Customer status must not be null", violation.getMessage());
    }

    @Test
    public void testCustomerNoEmailMustFailedValidation() {
        //prepare
        Customer customer = new Customer();
        customer.setName("Test customer");
        customer.setStatus(PROSPECT);

        //run
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        //assert
        assertEquals(violations.size(), 1);
        ConstraintViolation violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("Customer email must not be null", violation.getMessage());
    }

    @Test
    public void testCustomerEmptyEmailMustFailedValidation() {
        //prepare
        Customer customer = new Customer();
        customer.setName("Test customer");
        customer.setStatus(PROSPECT);
        customer.setEmail("");

        //run
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        //assert
        assertEquals(violations.size(), 1);
        ConstraintViolation violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("Customer email must have at least 3 characters", violation.getMessage());
    }
}