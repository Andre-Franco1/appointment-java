package com.abutua.appointment.domain.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.domain.mappers.TimeSlotMapper;
import com.abutua.appointment.domain.repositories.ProfessionalRepository;
import com.abutua.appointment.domain.services.exceptions.ParameterException;
import com.abutua.appointment.domain.services.usecases.write.read.SearchProfessionalAvailabilityDaysUseCase;
import com.abutua.appointment.domain.services.usecases.write.read.SearchProfessionalAvailabilityTimesUseCase;
import com.abutua.appointment.dto.TimeSlotResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfessionalService {
    
    @Autowired
    private SearchProfessionalAvailabilityTimesUseCase searchProfessionalAvailabilityTimesUseCase;

    @Autowired
    private SearchProfessionalAvailabilityDaysUseCase searchProfessionalAvailabilityDays;

    @Autowired
    private ProfessionalRepository professionalRepository;

    public List<TimeSlotResponse> getAvailabilityTimesFromProfessional(long professionalId, LocalDate date){

        var professional = getProfessional(professionalId);

        var timeSlots = this.searchProfessionalAvailabilityTimesUseCase.executeUseCase(professional, date);

        return timeSlots.stream().map(ts -> TimeSlotMapper.toTimeSlotResponseDTO(ts)).collect(Collectors.toList());
    }

    public List<Integer> getAvailabilityDaysFromProfessional(long professionalId, int month, int year) {
        
        checkProfessionalExistsOrThrowsException(professionalId);
        checkMonthIsValidOrThrowsException(month);
        checkYearIsValidOrThrowsException(year);
        checkMonthAndCurrentYearIsValidOrThrowsException(month, year);

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return this.searchProfessionalAvailabilityDays.executeUseCase(professionalId, start, end);
    }

    private Professional getProfessional(long professionalId) {
        return professionalRepository.findById(professionalId).orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));
    }

    private void checkProfessionalExistsOrThrowsException(long professionalId) {
        if(!professionalRepository.existsById(professionalId)){
            throw new EntityNotFoundException("Profissional não encontrado.");
        }
    }

    private void checkMonthAndCurrentYearIsValidOrThrowsException(int month, int year) {
        if(year == LocalDate.now().getYear() && month < LocalDate.now().getMonthValue()){
            throw new ParameterException("Mês inválido. O mês deve ser maior ou igual ao mês corrente.");
        }
    }

    private void checkYearIsValidOrThrowsException(int year) {
        if(year < LocalDate.now().getYear()){
            throw new ParameterException("Ano inválido. O ano deve ser maior ou igual ao ano corrente.");
        }
    }

    private void checkMonthIsValidOrThrowsException(int month) {
        if(month < 1 || month > 12){
            throw new ParameterException("Mês inválido. O mês deve ser de 1 a 12.");
        }
    }

}
