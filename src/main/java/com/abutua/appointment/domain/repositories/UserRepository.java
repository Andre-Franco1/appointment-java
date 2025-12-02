package com.abutua.appointment.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abutua.appointment.domain.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
