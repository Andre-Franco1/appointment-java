package com.abutua.appointment.domain.repositories;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.domain.entities.WorkScheduleItem;

public interface WorkScheduleItemRepository extends JpaRepository<WorkScheduleItem, Long> {

    @Query("SELECT w FROM WorkScheduleItem w " +
            "WHERE w.professional = :professional AND " +
            " w.dayOfWeek = :dayOfWeek " +
            "ORDER BY w.startTime")
    List<WorkScheduleItem> getWorkScheduleFromProfessionalByDayOfWeekOrderByStartTime(Professional professional,
            DayOfWeek dayOfWeek);

}
