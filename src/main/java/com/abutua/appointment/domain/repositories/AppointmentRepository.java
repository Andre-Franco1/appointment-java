package com.abutua.appointment.domain.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.abutua.appointment.domain.entities.Appointment;
import com.abutua.appointment.domain.entities.Client;
import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.domain.models.TimeSlot;

@NoRepositoryBean
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    @Query("SELECT COUNT(a) > 0 FROM Appointment a " + 
           " WHERE a.client = :client " +
           " AND   a.date = :date "      +
           " AND   a.startTime < :endTime " +
           " AND   a.endTime   > :startTime " + 
           " AND ( "+
           "     a.status = com.abutua.appointment.domain.entities.AppointmentStatus.OPEN    OR    " +        
           "     a.status = com.abutua.appointment.domain.entities.AppointmentStatus.PRESENT       " +        
           ")" 
          )
    boolean existsOpenOrPresentAppointmentsForClient(@Param("client")Client client, @Param("date")LocalDate date, @Param("startTime")LocalTime startTime, @Param("endTime")LocalTime endTime);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a " + 
           " WHERE a.professional = :professional " +
           " AND   a.date = :date "      +
           " AND   a.startTime < :endTime " +
           " AND   a.endTime   > :startTime " + 
           " AND ( "+
           "     a.status = com.abutua.appointment.domain.entities.AppointmentStatus.OPEN    OR    " +        
           "     a.status = com.abutua.appointment.domain.entities.AppointmentStatus.PRESENT       " +        
           ")" 
          )
    boolean existsOpenOrPresentAppointmentsForProfessional(@Param("professional")Professional professional, @Param("date")LocalDate date, @Param("startTime")LocalTime startTime, @Param("endTime")LocalTime endTime);

    List<Appointment> findByProfessionalIdAndDate(Long id, LocalDate date);

    @Query(value="SELECT EXTRACT(DOW FROM DATE(:start)) ", nativeQuery=true)
    Integer testeNative( LocalDate start);
   

    //Native Queries
    public List<Integer> getAvailableDaysFromProfessional(long professionalId, LocalDate start, LocalDate end);
    public List<TimeSlot> getAvailableTimesFromProfessional(long professionalId, LocalDate date);


}
