package com.abutua.appointment.domain.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.abutua.appointment.domain.entities.Appointment;
import com.abutua.appointment.domain.entities.Client;
import com.abutua.appointment.domain.entities.Professional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT COUNT(a) > 0 FROM Appointment a " + 
        "WHERE a.client = :client " +
        "AND a.date = :date " +
        "AND a.startTime < :endTime " +
        "AND a.endTime > :startTime " +
        "AND ( " +
        "a.status = com.abutua.appointment.domain.entities.AppointmentStatus.OPEN OR " +
        "a.status = com.abutua.appointment.domain.entities.AppointmentStatus.PRESENT " +
        ")" 
        )
    boolean existsOpenOrPresentAppointmentsForClient(Client client, LocalDate date, LocalTime startTime, LocalTime endTime);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a " + 
        "WHERE a.professional = :professional " +
        "AND a.date = :date " +
        "AND a.startTime < :endTime " +
        "AND a.endTime > :startTime " +
        "AND ( " +
        "a.status = com.abutua.appointment.domain.entities.AppointmentStatus.OPEN OR " +
        "a.status = com.abutua.appointment.domain.entities.AppointmentStatus.PRESENT " +
        ")" 
        )
    boolean existsOpenOrPresentAppointmentsForProfessional(Professional professional, LocalDate date, LocalTime startTime, LocalTime endTime);
    
}
