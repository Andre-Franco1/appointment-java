package com.abutua.appointment.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abutua.appointment.domain.entities.Appointment;
import com.abutua.appointment.domain.mappers.AppointmentMapper;
import com.abutua.appointment.domain.mappers.AppointmentTypeMapper;
import com.abutua.appointment.domain.repositories.AppointmentTypeRepository;
import com.abutua.appointment.domain.services.usecases.write.CreateAppointmentUseCase;
import com.abutua.appointment.dto.AppointmentRequest;
import com.abutua.appointment.dto.AppointmentResponse;
import com.abutua.appointment.dto.AppointmentTypeResponse;

@Service
public class AppointmentService {

    @Autowired
    private CreateAppointmentUseCase createAppointmentUseCase;

    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {

        Appointment appointment = createAppointmentUseCase.execusteUseCase(AppointmentMapper.fromAppointmentRequestDTO(appointmentRequest));
        return AppointmentMapper.toAppointmentResponseDTO(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentTypeResponse> getAllTypes(){
        return appointmentTypeRepository.findAll().stream().map(at -> AppointmentTypeMapper.toAppointmentTypeResponseDTO(at)).collect(Collectors.toList());
    }

}
