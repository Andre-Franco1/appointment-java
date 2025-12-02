package com.abutua.appointment.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abutua.appointment.domain.entities.AppointmentType;

public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Integer>{
    
}
