package com.abutua.appointment.domain.conterters;

import java.time.DayOfWeek;

import jakarta.persistence.AttributeConverter;

public interface DayOfWeekInterface extends AttributeConverter<DayOfWeek, Integer> {
    
}
