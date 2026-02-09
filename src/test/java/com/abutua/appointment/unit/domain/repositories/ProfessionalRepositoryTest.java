package com.abutua.appointment.unit.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.domain.repositories.ProfessionalRepository;

@DataJpaTest
@ActiveProfiles("test")
public class ProfessionalRepositoryTest {

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Test
    void findByNameContainingIgnoreCaseShouldReturnMatchingProfessionals() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Professional> result = professionalRepository.findByNameContainingIgnoreCase("Mar", pageable);

        assertFalse(result.isEmpty());
        assertTrue(result.getContent().stream().allMatch(p -> p.getName().toLowerCase().contains("mar")));
    }

    @Test
    void findByNameContainingIgnoreCaseShouldReturnEmptyPageWhenNoMatch() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Professional> result = professionalRepository.findByNameContainingIgnoreCase("nomeinexistente", pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void existsAssociationWithAreaShouldReturnTrueWhenAssociationExists() {
        Long professionalId = 6L;
        Integer areaId = 1;

        boolean exists = professionalRepository.existsAssociationWithArea(professionalId, areaId);

        assertTrue(exists);
    }

    @Test
    void existsAssociationWithAreaShouldReturnFalseWhenAssociationDoesNotExist() {
        Long professionalId = 6L;
        Integer areaId = 99;

        boolean exists = professionalRepository.existsAssociationWithArea(professionalId, areaId);

        assertFalse(exists);
    }

}
