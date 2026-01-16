package com.abutua.appointment.dto;

import java.util.Set;

public record ProfessionalResponse(
    long id,
    String name,
    String phone,
    Set<AreaResponse> areas,
    boolean active) {
    
}
