package com.abutua.appointment.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abutua.appointment.domain.entities.Professional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    
}
