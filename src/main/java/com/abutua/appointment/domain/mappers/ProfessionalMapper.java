package com.abutua.appointment.domain.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.dto.AreaResponse;
import com.abutua.appointment.dto.ProfessionalRequest;
import com.abutua.appointment.dto.ProfessionalResponse;

public class ProfessionalMapper {
    
    public static ProfessionalResponse toProfessionalResponseDTO(Professional professional) {

        Set<AreaResponse> areas = professional.getAreas().stream()
            .map(area -> new AreaResponse(
                    area.getId(),
                    area.getName()
            ))
            .collect(Collectors.toSet());

        ProfessionalResponse professionalResponse = new ProfessionalResponse(
                professional.getId(),
                professional.getName(),
                professional.getPhone(),
                areas,
                professional.isActive());
        return professionalResponse;
    }

    public static Professional fromProfessionalRequestDTO(ProfessionalRequest professionalRequest) {
        return new Professional(
            professionalRequest.name(),
            professionalRequest.phone(),
            professionalRequest.active());
    }
}
