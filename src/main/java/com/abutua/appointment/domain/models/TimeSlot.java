package com.abutua.appointment.domain.models;

import java.time.LocalTime;

public class TimeSlot {

    private LocalTime starTime;
    private LocalTime endTime;
    private boolean available;

    public TimeSlot() {
    }

    public TimeSlot(LocalTime starTime, LocalTime endTime, boolean available) {
        this.starTime = starTime;
        this.endTime = endTime;
        this.available = available;
    }

    public LocalTime getStarTime() {
        return starTime;
    }

    public void setStarTime(LocalTime starTime) {
        this.starTime = starTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}
