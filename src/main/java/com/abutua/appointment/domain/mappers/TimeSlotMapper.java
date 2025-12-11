package com.abutua.appointment.domain.mappers;

import com.abutua.appointment.domain.models.TimeSlot;
import com.abutua.appointment.dto.TimeSlotResponse;

public class TimeSlotMapper {

    public static TimeSlotResponse toTimeSlotResponseDTO(TimeSlot timeSlot) {

        return new TimeSlotResponse(timeSlot.getStartTime().toLocalTime(), timeSlot.getEndTime().toLocalTime(), timeSlot.isAvailable());
    }
}
