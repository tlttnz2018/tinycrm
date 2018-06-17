package com.propellerhead.assignment.tinycrm.repositories;

import com.propellerhead.assignment.tinycrm.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.propellerhead.assignment.tinycrm.domain.LeadStatus.PROSPECT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindById() {
        // prepare
        Customer customer = new Customer();
        customer.setName("Test customer");
        customer.setStatus(PROSPECT);
        customer.setEmail("customer@test.com");

        entityManager.persist(customer);
        entityManager.flush();

        // run
        Customer found = customerRepository.findById(customer.getId()).get();

        // assert
        assertNotNull(found);
        assertThat(found.getName()).isEqualTo(customer.getName());
    }

    @Test
    public void testFindByIdNotFoundMustThrowResourceNotFoundException() {
        // prepare
        Customer customer = new Customer();
        customer.setName("Test customer");
        customer.setStatus(PROSPECT);
        customer.setEmail("customer@test.com");

        entityManager.persist(customer);
        entityManager.flush();

        // run
        Optional<Customer> found = customerRepository.findById(255L);
        assertFalse(found.isPresent());
    }

    @Test
    public void testCallPrePersistWhenSaveCustomer() {
        // prepare
        Customer customer = new Customer();
        customer.setName("Test customer");
        customer.setStatus(PROSPECT);
        customer.setEmail("customer@test.com");

        // run
        entityManager.persist(customer);
        entityManager.flush();
        Customer found = customerRepository.findById(customer.getId()).get();

        // assert
        assertNotNull(found);
        assertNotNull(found.getCreatedAt());
    }
}