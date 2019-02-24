package com.example.profile.model;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.ValidationUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProfileTest {

    Profile profile;
    Validator validator;

    @Before
    public void setUp(){
        this.profile = new Profile();
        profile.setUserId("1");
        profile.setFirstName("Hello");
        profile.setLastName("World");
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Test
    public void testProfileDob_notRequired() {
        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testProfileDob_invalidDob() {
        profile.setDateOfBirth("abc");
        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testProfileDob_validDob() {
        profile.setDateOfBirth("29-02-2000");
        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        assertTrue(violations.isEmpty());
    }
}