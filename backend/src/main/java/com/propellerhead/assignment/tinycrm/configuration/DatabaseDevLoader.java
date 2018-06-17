/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.configuration;

import com.propellerhead.assignment.tinycrm.domain.Customer;
import com.propellerhead.assignment.tinycrm.domain.Note;
import com.propellerhead.assignment.tinycrm.repositories.CustomerRepository;
import com.propellerhead.assignment.tinycrm.repositories.NoteRepository;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * For generating database randomly for testing in developing mode only.
 *
 * Need to add -Dspring.profiles.active=dev to enable
 */
@Component
@Profile("dev")
public class DatabaseDevLoader implements ApplicationRunner {

    private final CustomerRepository customerRepository;
    private final NoteRepository noteRepository;

    @Autowired
    public DatabaseDevLoader(CustomerRepository customerRepository, NoteRepository noteRepository) {
        this.customerRepository = customerRepository;
        this.noteRepository = noteRepository;
    }

    /**
     * Load randomized data for demoing
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        EnhancedRandom randomizer = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().stringLengthRange(10, 200).build();
        Stream<Customer> customerStream = randomizer.randomStreamOf(100, Customer.class);
        customerStream.forEach(customer -> {
            Customer savedCustomer = this.customerRepository.save(customer);
            Stream<Note> noteStream = randomizer.randomStreamOf(100, Note.class);
            noteStream.forEach(note -> {
                note.setCustomer(savedCustomer);
                this.noteRepository.save(note);
            });
        });
    }
}