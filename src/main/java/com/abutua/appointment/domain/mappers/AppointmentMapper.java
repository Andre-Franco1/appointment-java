package com.abutua.appointment.domain.mappers;

import org.springframework.beans.BeanUtils;

import com.abutua.appointment.domain.entities.Appointment;
import com.abutua.appointment.domain.entities.AppointmentType;
import com.abutua.appointment.domain.entities.Area;
import com.abutua.appointment.domain.entities.Client;
import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.dto.AppointmentRequest;
import com.abutua.appointment.dto.AppointmentResponse;
import com.abutua.appointment.dto.IntegerDTO;
import com.abutua.appointment.dto.LongDTO;

public class AppointmentMapper {

    public static AppointmentResponse toAppointmentResponseDTO(Appointment appointment) {

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getComments(),
                appointment.getStatus(),
                new IntegerDTO(appointment.getAppointmentType().getId()),
                new IntegerDTO(appointment.getArea().getId()),
                new LongDTO(appointment.getProfessional().getId()),
                new LongDTO(appointment.getClient().getId()));
    }

    public static Appointment fromAppointmentRequestDTO(AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment();

        BeanUtils.copyProperties(appointmentRequest, appointment);
        appointment.setArea(new Area(appointmentRequest.area().id()));
        appointment.setAppointmentType(new AppointmentType(appointmentRequest.type().id()));
        appointment.setProfessional(new Professional(appointmentRequest.professional().id()));
        appointment.setClient(new Client(appointmentRequest.client().id()));

        return appointment;
    }

}
