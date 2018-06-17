package com.propellerhead.assignment.tinycrm.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NoteTest {
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
    public void testValidNoteMustPassValidation() {
        //prepare
        Note note = new Note();
        note.setNoteSubject("Subject");
        note.setNoteBody("Body");

        //run
        Set<ConstraintViolation<Note>> violations = validator.validate(note);

        //assert
        assertTrue(violations.toString(), violations.isEmpty());
    }

    @Test
    public void testNoteWithoutSubjectMustFailedValidation() {
        //prepare
        Note note = new Note();

        //run
        Set<ConstraintViolation<Note>> violations = validator.validate(note);

        //assert
        //assert
        assertEquals(violations.size(), 1);
        ConstraintViolation violation = violations.iterator().next();
        assertEquals("noteSubject", violation.getPropertyPath().toString());
        assertEquals("Note subject must not be null", violation.getMessage());

    }

    @Test
    public void testNoteWithEmptySubjectMustFailedValidation() {
        //prepare
        Note note = new Note();
        note.setNoteSubject("");

        //run
        Set<ConstraintViolation<Note>> violations = validator.validate(note);

        //assert
        //assert
        assertEquals(violations.size(), 1);
        ConstraintViolation violation = violations.iterator().next();
        assertEquals("noteSubject", violation.getPropertyPath().toString());
        assertEquals("Note subject must have at least 1 character", violation.getMessage());

    }

    @Test
    public void testNoteWithTooLongSubjectMustFailedValidation() {
        //prepare
        Note note = new Note();
        String tooLongSubject = RandomStringUtils.random(300);
        note.setNoteSubject(tooLongSubject);

        //run
        Set<ConstraintViolation<Note>> violations = validator.validate(note);

        //assert
        //assert
        assertEquals(violations.size(), 1);
        ConstraintViolation violation = violations.iterator().next();
        assertEquals("noteSubject", violation.getPropertyPath().toString());
        assertEquals("Note subject cannot be larger than 255 characters", violation.getMessage());

    }
}