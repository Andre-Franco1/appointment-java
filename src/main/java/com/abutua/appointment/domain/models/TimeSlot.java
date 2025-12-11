package com.abutua.appointment.domain.models;

import java.time.OffsetTime;

public interface TimeSlot {

    OffsetTime getStartTime();
    OffsetTime getEndTime();
    boolean isAvailable();

}
