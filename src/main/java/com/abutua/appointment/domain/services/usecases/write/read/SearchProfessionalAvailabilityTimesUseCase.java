package com.abutua.appointment.domain.services.usecases.write.read;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abutua.appointment.domain.models.TimeSlot;
import com.abutua.appointment.domain.repositories.AppointmentRepository;

@Service
public class SearchProfessionalAvailabilityTimesUseCase {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional(readOnly = true)
    public List<TimeSlot> executeUseCase(long professionalId, LocalDate date) {
        return this.appointmentRepository.getAvailableTimesFromProfessional(professionalId, date);
    }
}
