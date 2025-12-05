package com.abutua.appointment.domain.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abutua.appointment.domain.mappers.TimeSlotMapper;
import com.abutua.appointment.domain.repositories.ProfessionalRepository;
import com.abutua.appointment.domain.services.usecases.write.read.SearchProfessionalAvailabilityTimes;
import com.abutua.appointment.dto.TimeSlotResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfessionalService {
    
    @Autowired
    private SearchProfessionalAvailabilityTimes searchProfessionalAvailabilityTimes;

    @Autowired
    private ProfessionalRepository professionalRepository;

    public List<TimeSlotResponse> getAvailabilityTimes(long professionalId, LocalDate date){

        var professional = professionalRepository.findById(professionalId).orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));

        var timeSlots = this.searchProfessionalAvailabilityTimes.executeUseCase(professional, date);

        return timeSlots.stream().map(ts -> TimeSlotMapper.toTimeSlotResponseDTO(ts)).collect(Collectors.toList());
    }

}
