package com.abutua.appointment.domain.mappers;

import com.abutua.appointment.domain.entities.Area;
import com.abutua.appointment.dto.AreaResponse;

public class AreaMapper {
    
    public static AreaResponse toAreaResponseDTO(Area area){
        return new AreaResponse(area.getId(), area.getName());
    }
}
