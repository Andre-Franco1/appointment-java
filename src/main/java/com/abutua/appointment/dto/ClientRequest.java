package com.abutua.appointment.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientRequest (
    @NotBlank(message = "Nome requerido") String name,
    @NotBlank(message = "Telefone requerido") String phone,
    @NotNull(message = "Data de nascimento requerida") LocalDate dateOfBirth, 
    String comments) {
    
}
