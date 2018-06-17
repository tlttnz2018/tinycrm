/**
 * Copyright 2018
 */
package com.propellerhead.assignment.tinycrm.repositories;

import com.propellerhead.assignment.tinycrm.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for persisting note
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findByCustomerId(Long customerId, Pageable pageable);
}
