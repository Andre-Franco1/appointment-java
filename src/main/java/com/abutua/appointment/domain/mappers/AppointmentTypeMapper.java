package com.abutua.appointment.domain.mappers;

import com.abutua.appointment.domain.entities.AppointmentType;
import com.abutua.appointment.dto.AppointmentTypeResponse;

public class AppointmentTypeMapper {
    public static AppointmentTypeResponse toAppointmentTypeResponseDTO(AppointmentType appointmentType) {
        return new AppointmentTypeResponse(appointmentType.getId(),appointmentType.getType());
    }
}
