package com.abutua.appointment.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abutua.appointment.domain.entities.WorkScheduleItem;

public interface WorkScheduleItemRepository extends JpaRepository<WorkScheduleItem, Long> {
    
}
