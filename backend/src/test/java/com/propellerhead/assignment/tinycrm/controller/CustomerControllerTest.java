package com.propellerhead.assignment.tinycrm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.propellerhead.assignment.tinycrm.business.search.customer.CustomerSearchSpecification;
import com.propellerhead.assignment.tinycrm.domain.Customer;
import com.propellerhead.assignment.tinycrm.repositories.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.Optional;

import static com.propellerhead.assignment.tinycrm.domain.LeadStatus.PROSPECT;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@EnableSpringDataWebSupport
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void testGetAllCustomers() throws Exception {
        //prepare
        Customer customer = createCustomerData();
        Page foundPage = new PageImpl<>(Arrays.asList(customer));

        when( customerRepository.findAll(any(CustomerSearchSpecification.class), any(Pageable.class)) ).thenReturn(foundPage);

        //run and assert
        mockMvc.perform(get("/api/customers?field=name&query=es")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.content[0].name", is(customer.getName())));

        verify(customerRepository, times(1)).findAll(any(CustomerSearchSpecification.class), any(Pageable.class));
    }

    @Test
    public void testGetCustomer() throws Exception {
        //prepare
        Customer customer = createCustomerData();

        when( customerRepository.findById(1L) ).thenReturn(Optional.of(customer));

        // run and assert
        mockMvc.perform(get("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name", is(customer.getName())));

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateCustomer() throws Exception {
        //prepare
        Customer customer = createCustomerData();

        when( customerRepository.save(customer) ).thenReturn(customer);

        //run and assert
        mockMvc.perform(
                post("/api/customers")
                        .content(toJson(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name", is(customer.getName())));

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        //prepare
        Customer customer = createCustomerData();
        Customer customerUpdated = createCustomerData();
        customerUpdated.setName("Updated name");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer) ).thenReturn(customerUpdated);

        //run and assert
        mockMvc.perform(
                put("/api/customers/1")
                        .content(toJson(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name", is(customerUpdated.getName())));

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testUpdateCustomerFailedDueToCustomerNotFound() throws Exception {
        //prepare
        Customer customer = createCustomerData();

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        //run and assert
        mockMvc.perform(
                put("/api/customers/1")
                        .content(toJson(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(customerRepository, times(0)).save(customer);
    }

    private Customer createCustomerData() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test customer");
        customer.setStatus(PROSPECT);
        customer.setEmail("customer@test.com");
        return customer;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }
}