package com.abutua.appointment.dto;

import java.time.LocalTime;

public record TimeSlotResponse (
    LocalTime starTime,
    LocalTime endTime,
    boolean available) {
    
}
