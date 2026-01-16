package com.abutua.appointment.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfessionalRequest (
    @NotBlank(message = "Nome requerido") String name,
    @NotBlank(message = "Telefone requerido") String phone,
    boolean active,
    @NotNull(message = "Lista de áreas não pode ser nula") Set<@NotNull(message = "ID da área não pode ser nulo") IntegerDTO> areas) {
    
}
