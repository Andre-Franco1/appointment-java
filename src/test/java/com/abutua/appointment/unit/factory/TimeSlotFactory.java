package com.abutua.appointment.unit.factory;

import java.time.OffsetTime;

import com.abutua.appointment.domain.models.TimeSlot;

public class TimeSlotFactory {
    public static TimeSlot createTimeSlot(String startTime, String endTime, boolean available) {
        return new TimeSlot() {

            @Override
            public OffsetTime getStartTime() {
                return OffsetTime.parse(startTime);
            }

            @Override
            public OffsetTime getEndTime() {
                return OffsetTime.parse(endTime);
            }

            @Override
            public boolean isAvailable() {
                return available;
            }
        };
    }
}
