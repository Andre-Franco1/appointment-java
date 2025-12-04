package com.abutua.appointment.web.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abutua.appointment.domain.services.AppointmentService;
import com.abutua.appointment.dto.AppointmentRequest;
import com.abutua.appointment.dto.AppointmentResponse;

@RestController
@RequestMapping("appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Validated @RequestBody AppointmentRequest appointmentRequest) {

        var appointmentResponse = appointmentService.createAppointment(appointmentRequest);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(appointmentResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(appointmentResponse);
    }

}
