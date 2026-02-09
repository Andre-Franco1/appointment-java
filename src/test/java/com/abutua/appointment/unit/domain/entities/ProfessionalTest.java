package com.abutua.appointment.unit.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.abutua.appointment.domain.entities.Area;
import com.abutua.appointment.domain.entities.Professional;

public class ProfessionalTest {

    @Test
    void setActiveShouldChangeStatus() {
        Professional p = new Professional();

        assertFalse(p.isActive());

        p.setActive(true);
        assertTrue(p.isActive());
    }

    @Test
    void setAreasShouldReplaceCurrentAreas() {
        Professional p = new Professional();

        Set<Area> areas = new HashSet<>();
        areas.add(new Area(1));
        areas.add(new Area(2));

        p.setAreas(areas);

        assertEquals(2, p.getAreas().size());
    }

    @Test
    void constructorShouldSetAllAttributes() {

        String expName = "Test Professional";
        String expPhone = "1191912-2948";
        boolean expActive = true;

        Professional p = new Professional(expName, expPhone, expActive);

        assertEquals(expName, p.getName());
        assertEquals(expPhone, p.getPhone());
        assertEquals(expActive, p.isActive());
        assertNull(p.getId());
    }

}
