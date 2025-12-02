package com.abutua.appointment.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abutua.appointment.domain.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    
}
