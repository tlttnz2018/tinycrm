package com.propellerhead.assignment.tinycrm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.propellerhead.assignment.tinycrm.domain.Customer;
import com.propellerhead.assignment.tinycrm.domain.Note;
import com.propellerhead.assignment.tinycrm.repositories.CustomerRepository;
import com.propellerhead.assignment.tinycrm.repositories.NoteRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NoteController.class)
@EnableSpringDataWebSupport
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    public void testGetAllNotesByCustomerId() throws Exception {
        //prepare
        Customer customer = createCustomerData();
        Note note = createNote();
        note.setCustomer(customer);

        Page foundPage = new PageImpl<>(Arrays.asList(note));

        when(noteRepository.findByCustomerId(eq(1L), any(Pageable.class)) ).thenReturn(foundPage);

        //run and assert
        mockMvc.perform(get("/api/customers/1/notes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.content[0].noteSubject", is(note.getNoteSubject())));

        verify(noteRepository, times(1)).findByCustomerId(eq(1L), any(Pageable.class));
    }

    @Test
    public void testCreateNote() throws Exception {
        //prepare
        Customer customer = createCustomerData();
        Note note = createNote();
        note.setCustomer(customer);

        when(customerRepository.findById(eq(1L))).thenReturn(Optional.of(customer));
        when(noteRepository.save(note) ).thenReturn(note);

        //run and assert
        mockMvc.perform(
                post("/api/customers/1/notes")
                        .content(toJson(note))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.noteSubject", is(note.getNoteSubject())));

        verify(customerRepository, times(1)).findById(eq(1L));
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    public void testCreateNoteButClientNotFoundMustFail() throws Exception {
        //prepare
        Customer customer = createCustomerData();
        Note note = createNote();
        note.setCustomer(customer);

        when(customerRepository.findById(eq(1L))).thenReturn(Optional.empty());
        when(noteRepository.save(note) ).thenReturn(note);

        //run and assert
        mockMvc.perform(
                post("/api/customers/1/notes")
                        .content(toJson(note))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(customerRepository, times(1)).findById(eq(1L));
        verify(noteRepository, times(0)).save(note);
    }

    @Test
    public void updateNote() throws Exception {
        //prepare
        Customer customer = createCustomerData();
        Note note = createNote();
        note.setCustomer(customer);

        Note updatedNote = createNote();
        updatedNote.setCustomer(customer);
        updatedNote.setNoteSubject("Updated note");

        when(customerRepository.existsById(eq(1L))).thenReturn(true);
        when( noteRepository.findById(eq(1L)) ).thenReturn(Optional.of(note));
        when( noteRepository.save(updatedNote) ).thenReturn(updatedNote);

        //run and assert
        mockMvc.perform(
                put("/api/customers/1/notes/1", customer)
                        .content(toJson(updatedNote))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.noteSubject", is(updatedNote.getNoteSubject())));

        verify(customerRepository, times(1)).existsById(eq(1L));
        verify(noteRepository, times(1)).findById(eq(1L));
        verify(noteRepository, times(1)).save(updatedNote);
    }

    @Test
    public void updateNoteButNoClientFoundMustFail() throws Exception {
        //prepare
        Customer customer = createCustomerData();
        Note updatedNote = createNote();
        updatedNote.setCustomer(customer);
        updatedNote.setNoteSubject("Updated note");

        when(customerRepository.existsById(eq(1L))).thenReturn(false);

        //run and assert
        mockMvc.perform(
                put("/api/customers/1/notes/1", customer)
                        .content(toJson(updatedNote))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(customerRepository, times(1)).existsById(eq(1L));
        verify(noteRepository, times(0)).findById(eq(1L));
        verify(noteRepository, times(0)).save(updatedNote);
    }

    @Test
    public void updateNoteButNoExistingNoteFoundMustFail() throws Exception {
        //prepare
        Customer customer = createCustomerData();
        Note note = createNote();
        note.setCustomer(customer);

        Note updatedNote = createNote();
        updatedNote.setCustomer(customer);
        updatedNote.setNoteSubject("Updated note");

        when(customerRepository.existsById(eq(1L))).thenReturn(true);
        when( noteRepository.findById(eq(1L)) ).thenReturn(Optional.empty());
        when( noteRepository.save(updatedNote) ).thenReturn(updatedNote);

        //run and assert
        mockMvc.perform(
                put("/api/customers/1/notes/1", customer)
                        .content(toJson(updatedNote))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(customerRepository, times(1)).existsById(eq(1L));
        verify(noteRepository, times(1)).findById(eq(1L));
        verify(noteRepository, times(0)).save(updatedNote);
    }

    private Customer createCustomerData() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test customer");
        customer.setStatus(PROSPECT);
        customer.setEmail("customer@test.com");
        return customer;
    }

    private Note createNote() {
        Note note = new Note();
        note.setId(1L);
        note.setNoteSubject("Subject");
        note.setNoteBody("Body");
        return note;
    }
    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }
}