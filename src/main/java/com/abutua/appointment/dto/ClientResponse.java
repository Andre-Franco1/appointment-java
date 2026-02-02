package com.abutua.appointment.dto;

import java.time.LocalDate;

public record ClientResponse(
    Long id,
    String name,
    String phone,
    LocalDate dateOfBirth,
    String comments) {

}
