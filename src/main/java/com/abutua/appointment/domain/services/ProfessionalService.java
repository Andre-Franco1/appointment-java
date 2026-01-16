package com.abutua.appointment.domain.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abutua.appointment.domain.entities.Area;
import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.domain.mappers.ProfessionalMapper;
import com.abutua.appointment.domain.mappers.TimeSlotMapper;
import com.abutua.appointment.domain.repositories.AreaRepository;
import com.abutua.appointment.domain.repositories.ProfessionalRepository;
import com.abutua.appointment.domain.services.exceptions.DatabaseException;
import com.abutua.appointment.domain.services.exceptions.ParameterException;
import com.abutua.appointment.domain.services.usecases.read.SearchProfessionalAvailabilityDaysUseCase;
import com.abutua.appointment.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;
import com.abutua.appointment.dto.IntegerDTO;
import com.abutua.appointment.dto.ProfessionalRequest;
import com.abutua.appointment.dto.ProfessionalResponse;
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

    @Autowired
    private AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public Page<ProfessionalResponse> findByNameContainingIgnoreCase(String name, int page, int size) {
        var pageRequest = PageRequest.of(page, size);
        var pageProfessional = professionalRepository.findByNameContainingIgnoreCase(name, pageRequest);
        return pageProfessional.map(c -> ProfessionalMapper.toProfessionalResponseDTO(c));
    }

    @Transactional(readOnly = true)
    public ProfessionalResponse getById(long id) {
        var professional = professionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));

        return ProfessionalMapper.toProfessionalResponseDTO(professional);
    }

    @Transactional
    public ProfessionalResponse save(ProfessionalRequest professionalRequest) {
        var professional = ProfessionalMapper.fromProfessionalRequestDTO(professionalRequest);
        Set<Area> areas = setAreasInProfessional(professionalRequest.areas());
        professional.setAreas(areas);
        var savedProfessional = professionalRepository.save(professional);
        return ProfessionalMapper.toProfessionalResponseDTO(savedProfessional);
    }

    @Transactional
    public void update(long id, ProfessionalRequest professionalRequest) {
        try {
            var professional = professionalRepository.getReferenceById(id);

            professional.setName(professionalRequest.name());
            professional.setPhone(professionalRequest.phone());
            professional.setActive(professionalRequest.active());

            Set<Area> areas = setAreasInProfessional(professionalRequest.areas());
            professional.setAreas(areas);

            professionalRepository.save(professional);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Profissional não encontrado.");
        }
    }

    @Transactional
    public void deleteById(long id) {
        try {
            if (professionalRepository.existsById(id))
                professionalRepository.deleteById(id);
            else {
                throw new EntityNotFoundException("Profissional não encontrado.");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Conflito ao remover o profissional.");
        }
    }

    @Transactional(readOnly = true)
    public List<TimeSlotResponse> getAvailabilityTimesFromProfessional(long professionalId, LocalDate date) {

        var professional = getProfessional(professionalId);

        var timeSlots = this.searchProfessionalAvailabilityTimesUseCase.executeUseCase(professional.getId(), date);

        return timeSlots.stream().map(ts -> TimeSlotMapper.toTimeSlotResponseDTO(ts)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Integer> getAvailabilityDaysFromProfessional(long professionalId, int month, int year) {

        checkProfessionalExistsOrThrowsException(professionalId);
        checkMonthIsValidOrThrowsException(month);
        checkYearIsValidOrThrowsException(year);
        checkMonthAndCurrentYearIsValidOrThrowsException(month, year);

        LocalDate start = LocalDate.of(year, month, 1);

        if (start.isBefore(LocalDate.now())) {
            start = LocalDate.now();
        }
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return this.searchProfessionalAvailabilityDays.executeUseCase(professionalId, start, end);
    }

    private Professional getProfessional(long professionalId) {
        return professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado."));
    }

    private void checkProfessionalExistsOrThrowsException(long professionalId) {
        if (!professionalRepository.existsById(professionalId)) {
            throw new EntityNotFoundException("Profissional não encontrado.");
        }
    }

    private void checkMonthAndCurrentYearIsValidOrThrowsException(int month, int year) {
        if (year == LocalDate.now().getYear() && month < LocalDate.now().getMonthValue()) {
            throw new ParameterException("Mês inválido. O mês deve ser maior ou igual ao mês corrente.");
        }
    }

    private void checkYearIsValidOrThrowsException(int year) {
        if (year < LocalDate.now().getYear()) {
            throw new ParameterException("Ano inválido. O ano deve ser maior ou igual ao ano corrente.");
        }
    }

    private void checkMonthIsValidOrThrowsException(int month) {
        if (month < 1 || month > 12) {
            throw new ParameterException("Mês inválido. O mês deve ser de 1 a 12.");
        }
    }

    private Set<Area> setAreasInProfessional(Set<IntegerDTO> areaDtos) {

    if (areaDtos == null || areaDtos.isEmpty()) {
        return new HashSet<>();
    }

    Set<Integer> areaIds = areaDtos.stream()
        .map(IntegerDTO::id)
        .collect(Collectors.toSet());

    Set<Area> areas = new HashSet<>(areaRepository.findAllById(areaIds));

    if (areas.size() != areaIds.size()) {
        throw new EntityNotFoundException("Uma ou mais áreas não existem");
    }

    return areas;
}


}
