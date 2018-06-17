package com.propellerhead.assignment.tinycrm.repositories;

import com.propellerhead.assignment.tinycrm.domain.Customer;
import com.propellerhead.assignment.tinycrm.domain.Note;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import static com.propellerhead.assignment.tinycrm.domain.LeadStatus.PROSPECT;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NoteRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    public void findByCustomerIdFound() {
        DataProvider dataProvider = new DataProvider().prepare();
        Customer customer = dataProvider.getCustomer();
        Note note = dataProvider.getNote();

        Page<Note> notes = noteRepository.findByCustomerId(customer.getId(), null);
        assertEquals(notes.getTotalPages(), 1);
        assertEquals(notes.getContent().size(), 1);
        assertEquals(notes.getContent().get(0).getNoteBody(), note.getNoteBody());

    }

    @Test
    public void findByCustomerIdNotFound() {
        DataProvider dataProvider = new DataProvider().prepare();
        Note note = dataProvider.getNote();

        Page<Note> notes = noteRepository.findByCustomerId(255L, null);
        assertEquals(notes.getTotalPages(), 1);
        assertEquals(notes.getContent().size(), 0); // Found no note

    }

    private class DataProvider {
        private Customer customer;
        private Note note;

        public Customer getCustomer() {
            return customer;
        }

        public Note getNote() {
            return note;
        }

        public DataProvider prepare() {
            customer = new Customer();
            customer.setName("Test customer");
            customer.setStatus(PROSPECT);
            customer.setEmail("customer@test.com");

            entityManager.persist(customer);
            entityManager.flush();

            note = new Note();
            note.setCustomer(customer);
            note.setNoteSubject("Subject");
            note.setNoteBody("Body");

            entityManager.persist(note);
            entityManager.flush();
            return this;
        }
    }
}