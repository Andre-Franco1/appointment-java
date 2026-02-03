package com.abutua.appointment.unit.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.abutua.appointment.domain.entities.Client;

public class ClientTest {
    
    @Test
    void getDateOfBirthShouldReturnNull() {
        Client c = new Client();
        assertNull(c.getDateOfBirth());
    }

    @Test
    void getDateOfBirthShouldReturnLocalDate() {
        Client c = new Client();
        LocalDate expDate = LocalDate.parse("2024-08-01");

        c.setDateOfBirth(expDate);

        assertEquals(expDate, c.getDateOfBirth());
    }

    //TODO add comments attribute
    @Test
    void getDateOfBirthShouldSetAllAttributes() {
        String expName = "Test Client";
        LocalDate expDate = LocalDate.parse("2024-08-01");
        String expPhone = "1191912-2948";

        Client c = new Client(expName, expPhone, expDate);

        assertEquals(expName, c.getName());
        assertEquals(expPhone, c.getPhone());
        assertEquals(expDate, c.getDateOfBirth());
        assertNull(c.getId());
    }

}
