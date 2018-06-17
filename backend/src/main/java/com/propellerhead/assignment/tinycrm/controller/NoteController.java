/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.controller;

import com.propellerhead.assignment.tinycrm.business.exception.ResourceNotFoundException;
import com.propellerhead.assignment.tinycrm.domain.Note;
import com.propellerhead.assignment.tinycrm.repositories.CustomerRepository;
import com.propellerhead.assignment.tinycrm.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST controller for all operations related to notes of a customer
 */
@RestController
@RequestMapping("/api")
public class NoteController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private NoteRepository noteRepository;

    /**
     * Get a list of notes for one specific customer. Support paging
     *
     * @param customerId id of customer need to get list of notes attached
     * @param pageable paging information
     * @return a page of notes for specific customer
     */
    @GetMapping("/customers/{customerId}/notes")
    public Page<Note> getAllNotesByCustomerId(@PathVariable (value = "customerId") Long customerId, Pageable pageable) {
        return noteRepository.findByCustomerId(customerId, pageable);
    }

    /**
     * Attach new note to existing customer
     *
     * @param customerId id of customer to attach note. Customer must be existed.
     * @param note note information to attach
     * @return note information attached.
     */
    @PostMapping("/customers/{customerId}/notes")
    public Note createNote(@PathVariable (value = "customerId") Long customerId, @Valid @RequestBody Note note) {
        return customerRepository.findById(customerId).map(customer -> {
            note.setCustomer(customer);
            return noteRepository.save(note);
        }).orElseThrow(() -> new ResourceNotFoundException("CustomerId " + customerId + " not found"));
    }

    /**
     * Update note content for existing customer
     *
     * @param customerId id of existing customer for updating note
     * @param noteId id of note need to be updated
     * @param noteRequest new information need to be updated
     * @return note updated
     */
    @PutMapping("/customers/{customerId}/notes/{noteId}")
    public Note updateNote(@PathVariable (value = "customerId") Long customerId, @PathVariable (value = "noteId") Long noteId, @Valid @RequestBody Note noteRequest) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("CustomerId " + customerId + " not found");
        }

        return noteRepository.findById(noteId).map(note -> {
            note.setNoteSubject(noteRequest.getNoteSubject());
            note.setNoteBody(noteRequest.getNoteBody());
            return noteRepository.save(note);
        }).orElseThrow(() -> new ResourceNotFoundException("NoteId " + noteId + " not found"));
    }
}
