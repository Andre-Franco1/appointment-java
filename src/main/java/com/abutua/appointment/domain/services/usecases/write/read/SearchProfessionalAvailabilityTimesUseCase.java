package com.abutua.appointment.domain.services.usecases.write.read;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abutua.appointment.domain.entities.Appointment;
import com.abutua.appointment.domain.entities.AppointmentStatus;
import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.domain.entities.WorkScheduleItem;
import com.abutua.appointment.domain.models.TimeSlot;
import com.abutua.appointment.domain.repositories.AppointmentRepository;
import com.abutua.appointment.domain.repositories.WorkScheduleItemRepository;

@Service
public class SearchProfessionalAvailabilityTimesUseCase {

    @Autowired
    private WorkScheduleItemRepository workScheduleItemRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<TimeSlot> executeUseCase(Professional professional, LocalDate date) {

        var timeSlots = new ArrayList<TimeSlot>();
        var workScheduleItems = getWorkScheduleItems(professional, date);
        var appointments = getAppointments(professional, date);

        for (var item : workScheduleItems) {
            timeSlots.addAll(calculateTimeSlots(item, appointments, date));
        }

        return timeSlots;
    }

    private List<TimeSlot> calculateTimeSlots(WorkScheduleItem item, List<Appointment> appointments, LocalDate date) {
        var startTime = item.getStartTime();
        var slotSize = item.getSlotSize();
        var slots = item.getSlots();
        var timeSlots = new ArrayList<TimeSlot>();

        for (int i = 0; i < slots; i++) {
            var start = startTime.plusMinutes(i*slotSize);
            var end = start.plusMinutes(slotSize);

            boolean available = isTimeSlotAvailable(start, end, appointments);
            boolean presentOrFuture = isStartTimeValidIfDateIsToday(start, date);

            timeSlots.add(new TimeSlot(start, end, available && presentOrFuture));
        }
        return timeSlots;
    }

    private boolean isStartTimeValidIfDateIsToday(LocalTime start, LocalDate date) {
        return date.isAfter(LocalDate.now()) || (date.equals(LocalDate.now()) && start.isAfter(LocalTime.now()));
    }

    private boolean isTimeSlotAvailable(LocalTime start, LocalTime end, List<Appointment> appointments) {
        return appointments.stream().noneMatch(a -> a.getStartTime().isBefore(end) && a.getEndTime().isAfter(start)
                && (a.getStatus().equals(AppointmentStatus.OPEN) || a.getStatus().equals(AppointmentStatus.PRESENT)));
    }

    private List<Appointment> getAppointments(Professional professional, LocalDate date) {
        return this.appointmentRepository.findByProfessionalIdAndDate(professional.getId(), date);
    }

    private List<WorkScheduleItem> getWorkScheduleItems(Professional professional, LocalDate date) {
        return this.workScheduleItemRepository.getWorkScheduleFromProfessionalByDayOfWeekOrderByStartTime(professional,
                date.getDayOfWeek());
    }
}
