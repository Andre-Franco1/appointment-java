package com.abutua.appointment.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abutua.appointment.domain.entities.Appointment;
import com.abutua.appointment.domain.mappers.AppointmentMapper;
import com.abutua.appointment.domain.services.usecases.write.CreateAppointmentUseCase;
import com.abutua.appointment.dto.AppointmentRequest;
import com.abutua.appointment.dto.AppointmentResponse;

@Service
public class AppointmentService {

    @Autowired
    private CreateAppointmentUseCase createAppointmentUseCase;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {

        Appointment appointment = createAppointmentUseCase.execusteUseCase(AppointmentMapper.fromAppointmentRequestDTO(appointmentRequest));
        return AppointmentMapper.toAppointmentResponseDTO(appointment);
    }

}
