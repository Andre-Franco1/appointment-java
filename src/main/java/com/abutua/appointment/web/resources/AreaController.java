package com.abutua.appointment.web.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abutua.appointment.domain.services.AreaService;
import com.abutua.appointment.dto.AreaResponse;
import com.abutua.appointment.dto.ProfessionalResponse;


@RestController
@RequestMapping("areas")
public class AreaController {
    
    @Autowired
    private AreaService areaService;

    @GetMapping
    public ResponseEntity<List<AreaResponse>> getAreas() {
        var areas = areaService.getAreas();
        return ResponseEntity.ok(areas);
    }

    @GetMapping("{id}/professionals")
    public ResponseEntity<List<ProfessionalResponse>> getProfessionalsByArea(@PathVariable int id) {
        return ResponseEntity.ok(areaService.getProfessionalsByArea(id));
    }
    
    
}
