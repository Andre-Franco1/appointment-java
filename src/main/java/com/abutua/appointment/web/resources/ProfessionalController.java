package com.abutua.appointment.web.resources;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abutua.appointment.domain.services.ProfessionalService;
import com.abutua.appointment.dto.TimeSlotResponse;

@RestController
@RequestMapping("professionals")
public class ProfessionalController {

    @Autowired
    private ProfessionalService professionalService;

    @GetMapping("{id}/availability-times")
    public ResponseEntity<List<TimeSlotResponse>> getAvailabilityTimes(@PathVariable long id,
            @RequestParam(name = "date") LocalDate date) {
                
        var timeSlots = professionalService.getAvailabilityTimes(id, date);

        return ResponseEntity.ok(timeSlots);
    }

}
