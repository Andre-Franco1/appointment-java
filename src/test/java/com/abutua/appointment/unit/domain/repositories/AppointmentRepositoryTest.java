package com.abutua.appointment.unit.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.abutua.appointment.domain.entities.Client;
import com.abutua.appointment.domain.models.TimeSlot;
import com.abutua.appointment.domain.repositories.AppointmentRepository;
import com.abutua.appointment.unit.factory.TimeSlotFactory;

@DataJpaTest
@ActiveProfiles("test")
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void existsOpenOrPresentAppointmentsForClientShouldReturnTrue() {
        Client client3 = new Client(3L);
        LocalDate date = LocalDate.of(2028, 4, 3);
        LocalTime startTime = LocalTime.of(8, 0, 0);
        LocalTime endTime = LocalTime.of(8, 30, 0);

        var found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);

        assertTrue(found);

        startTime = LocalTime.of(8, 1);
        endTime = LocalTime.of(8, 2);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);
        assertTrue(found);

        startTime = LocalTime.of(7, 0);
        endTime = LocalTime.of(8, 2);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);
        assertTrue(found);

        startTime = LocalTime.of(7, 0);
        endTime = LocalTime.of(9, 2);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);
        assertTrue(found);

        startTime = LocalTime.of(9, 0);
        endTime = LocalTime.of(11, 0);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);
        assertTrue(found);

        startTime = LocalTime.of(6, 0);
        endTime = LocalTime.of(12, 0);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);
        assertTrue(found);

        Client client4 = new Client(4L);
        startTime = LocalTime.of(6, 0);
        endTime = LocalTime.of(12, 0);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client4, date, startTime, endTime);
        assertTrue(found);

        startTime = LocalTime.of(9, 30);
        endTime = LocalTime.of(10, 0);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client4, date, startTime, endTime);
        assertTrue(found);

        startTime = LocalTime.of(9, 15);
        endTime = LocalTime.of(10, 15);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client4, date, startTime, endTime);
        assertTrue(found);
    }

    @Test
    void existsOpenOrPresentAppointmentsForClientShouldReturnFalse() {
        Client client3 = new Client(3L);
        LocalDate date = LocalDate.of(2028, 4, 3);
        LocalTime startTime = LocalTime.of(7, 30, 0);
        LocalTime endTime = LocalTime.of(8, 0, 0);

        var found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);

        assertFalse(found);

        startTime = LocalTime.of(9, 30);
        endTime = LocalTime.of(10, 0);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);
        assertFalse(found);

        startTime = LocalTime.of(7, 59);
        endTime = LocalTime.of(8, 0);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);
        assertFalse(found);

        startTime = LocalTime.of(9, 30);
        endTime = LocalTime.of(9, 31);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client3, date, startTime, endTime);
        assertFalse(found);

        Client client4 = new Client(4L);
        startTime = LocalTime.of(8, 0);
        endTime = LocalTime.of(8, 30);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client4, date, startTime, endTime);
        assertFalse(found);

        startTime = LocalTime.of(8, 0);
        endTime = LocalTime.of(9, 30);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client4, date, startTime, endTime);
        assertFalse(found);

        startTime = LocalTime.of(7, 0);
        endTime = LocalTime.of(9, 30);
        found = appointmentRepository.existsOpenOrPresentAppointmentsForClient(client4, date, startTime, endTime);
        assertFalse(found);
    }

    @Test
    void getAvailableDaysFromProfessionalShouldReturnEmptyList() {
        Long professionalId = 6L;
        LocalDate start = LocalDate.of(2028, 4, 1);
        LocalDate end = LocalDate.of(2028, 4, 3);

        var foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);

        assertTrue(foundDays.isEmpty());

        start = LocalDate.of(2028, 4, 3);
        end = LocalDate.of(2028, 4, 3);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        assertTrue(foundDays.isEmpty());

        start = LocalDate.of(2028, 4, 5);
        end = LocalDate.of(2028, 4, 6);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        assertTrue(foundDays.isEmpty());

        professionalId = 7L;
        start = LocalDate.of(2028, 4, 1);
        end = LocalDate.of(2028, 4, 2);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);

        assertTrue(foundDays.isEmpty());

        start = LocalDate.of(2028, 4, 5);
        end = LocalDate.of(2028, 4, 9);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        assertTrue(foundDays.isEmpty());

        start = LocalDate.of(2028, 4, 9);
        end = LocalDate.of(2028, 4, 9);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        assertTrue(foundDays.isEmpty());
    }

    @Test
    void getAvailableDaysFromProfessionalShouldReturnListOfInteger() {

        Long professionalId = 6L;
        var start = LocalDate.of(2028, 4, 1);
        var end = LocalDate.of(2028, 4, 30);
        var foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        var expectDays = List.of(4, 7, 10, 11, 14, 17, 18, 21, 24, 25, 28);
        assertIterableEquals(expectDays, foundDays);


        start = LocalDate.of(2028, 4, 1);
        end = LocalDate.of(2028, 4, 8);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        expectDays = List.of(4, 7);
        assertIterableEquals(expectDays, foundDays);

        start = LocalDate.of(2028, 4, 3);
        end = LocalDate.of(2028, 4, 15);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        expectDays = List.of(4, 7,10,11,14);
        assertIterableEquals(expectDays, foundDays);

        start = LocalDate.of(2028, 4, 3);
        end = LocalDate.of(2028, 4, 4);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        expectDays = List.of(4);
        assertIterableEquals(expectDays, foundDays);


        
        professionalId = 7L;
        start = LocalDate.of(2028, 4, 1);
        end = LocalDate.of(2028, 4, 30);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        expectDays = List.of(3,4, 10, 11, 17, 18,24, 25);
        assertIterableEquals(expectDays, foundDays);


        start = LocalDate.of(2028, 4, 1);
        end = LocalDate.of(2028, 4, 8);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        expectDays = List.of(3, 4);
        assertIterableEquals(expectDays, foundDays);

        start = LocalDate.of(2028, 4, 3);
        end = LocalDate.of(2028, 4, 15);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        expectDays = List.of(3, 4, 10,11);
        assertIterableEquals(expectDays, foundDays);

        start = LocalDate.of(2028, 4, 3);
        end = LocalDate.of(2028, 4, 4);
        foundDays = appointmentRepository.getAvailableDaysFromProfessional(professionalId, start, end);
        expectDays = List.of(3,4);
        assertIterableEquals(expectDays, foundDays);
        
    }

    @Test
    void getAvailableTimesFromProfessionalShouldReturnEmptyList() {
        Long professionalId = 6L;
        LocalDate date = LocalDate.of(2028,4,5);
        List<TimeSlot> foundTimes = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
        assertTrue(foundTimes.isEmpty());
        
        date = LocalDate.of(2024,4,6);
        foundTimes = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
        assertTrue(foundTimes.isEmpty());

        professionalId = 7L;
        date = LocalDate.of(2024,4,6);
        foundTimes = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
        assertTrue(foundTimes.isEmpty());

        date = LocalDate.of(2024,4,7);
        foundTimes = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
        assertTrue(foundTimes.isEmpty());
    }


    @Test
    void getAvailableTimesFromProfessionalShouldReturnListOfTimeSlots() {
        
        Long professionalId = 6L;
        LocalDate date = LocalDate.of(2028,4,3);
        List<TimeSlot> foundTimeSlots = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);

        List<TimeSlot> expTimeSlots = List.of(
            TimeSlotFactory.createTimeSlot("08:00:00-03:00","08:30:00-03:00", false),
            TimeSlotFactory.createTimeSlot("08:30:00-03:00","09:00:00-03:00", false),
            TimeSlotFactory.createTimeSlot("09:00:00-03:00","09:30:00-03:00", false),
            TimeSlotFactory.createTimeSlot("09:30:00-03:00","10:00:00-03:00", false)
        );
        checkTimeSlotsLists(expTimeSlots, foundTimeSlots);
        

        date = LocalDate.of(2028,4,10);
        foundTimeSlots = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
        expTimeSlots = List.of(
            TimeSlotFactory.createTimeSlot("08:00:00-03:00","08:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("08:30:00-03:00","09:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("09:00:00-03:00","09:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("09:30:00-03:00","10:00:00-03:00", false)
        );
        checkTimeSlotsLists(expTimeSlots, foundTimeSlots);


        date = LocalDate.of(2028,4,17);
        foundTimeSlots = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
        expTimeSlots = List.of(
            TimeSlotFactory.createTimeSlot("08:00:00-03:00","08:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("08:30:00-03:00","09:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("09:00:00-03:00","09:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("09:30:00-03:00","10:00:00-03:00", true)
        );
        checkTimeSlotsLists(expTimeSlots, foundTimeSlots);


        date = LocalDate.of(2028,4,7);
        foundTimeSlots = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
        expTimeSlots = List.of(
            TimeSlotFactory.createTimeSlot("08:00:00-03:00","08:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("08:30:00-03:00","09:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("09:00:00-03:00","09:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("09:30:00-03:00","10:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("10:00:00-03:00","10:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("10:30:00-03:00","11:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("11:00:00-03:00","11:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("11:30:00-03:00","12:00:00-03:00", true)
        );
        checkTimeSlotsLists(expTimeSlots, foundTimeSlots);


        date = LocalDate.of(2028,4,18);
        foundTimeSlots = appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
        expTimeSlots = List.of(
            TimeSlotFactory.createTimeSlot("08:00:00-03:00","08:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("08:30:00-03:00","09:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("09:00:00-03:00","09:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("09:30:00-03:00","10:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("10:00:00-03:00","10:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("10:30:00-03:00","11:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("11:00:00-03:00","11:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("11:30:00-03:00","12:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("14:00:00-03:00","14:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("14:30:00-03:00","15:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("15:00:00-03:00","15:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("15:30:00-03:00","16:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("16:00:00-03:00","16:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("16:30:00-03:00","17:00:00-03:00", true),
            TimeSlotFactory.createTimeSlot("17:00:00-03:00","17:30:00-03:00", true),
            TimeSlotFactory.createTimeSlot("18:30:00-03:00","18:00:00-03:00", true)
        );
        checkTimeSlotsLists(expTimeSlots, foundTimeSlots);
    }


    private void checkTimeSlotsLists(List<TimeSlot> expectTimes, List<TimeSlot> foundTimes) {
        assertTrue(expectTimes.size()==foundTimes.size());

        for(int i=0;  i < expectTimes.size(); i++){
            var timeSlotExpect = expectTimes.get(i);
            var timeSlotFound = expectTimes.get(i);

            assertEquals(timeSlotExpect.getStartTime(), timeSlotFound.getStartTime());
            assertEquals(timeSlotExpect.getEndTime(), timeSlotFound.getEndTime());
            assertEquals(timeSlotExpect.isAvailable(), timeSlotFound.isAvailable());
        }
    } 
    

}