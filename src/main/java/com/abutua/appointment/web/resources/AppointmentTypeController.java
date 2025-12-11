package com.abutua.appointment.web.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abutua.appointment.domain.services.AppointmentService;
import com.abutua.appointment.dto.AppointmentTypeResponse;

@RestController
@RequestMapping("appointment-types")
public class AppointmentTypeController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentTypeResponse>> getAppointmentTypes() {
        return ResponseEntity.ok().body(appointmentService.getAllTypes());
    }

}
