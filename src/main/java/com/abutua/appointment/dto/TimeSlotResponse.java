package com.abutua.appointment.dto;

import java.time.LocalTime;

public record TimeSlotResponse (
    LocalTime startTime,
    LocalTime endTime,
    boolean available) {
    
}
