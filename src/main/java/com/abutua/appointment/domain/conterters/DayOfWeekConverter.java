package com.abutua.appointment.domain.conterters;

import java.time.DayOfWeek;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer>{

    @Autowired
    private DayOfWeekInterface converter;

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        return converter.convertToDatabaseColumn(dayOfWeek);
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer dayOfWeekibute) {
        return converter.convertToEntityAttribute(dayOfWeekibute);
    }
    
    
}
