package com.abutua.appointment.domain.mappers;

import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.dto.ProfessionalResponse;

public class ProfessionalMapper {
    
    public static ProfessionalResponse toProfessionalResponseDTO(Professional professional) {
        return new ProfessionalResponse(professional.getId(), professional.getName(), professional.getPhone(), professional.isActive());
    }
}
