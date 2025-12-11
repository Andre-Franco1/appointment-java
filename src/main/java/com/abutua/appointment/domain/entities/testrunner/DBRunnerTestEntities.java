package com.abutua.appointment.domain.entities.testrunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.abutua.appointment.domain.entities.Appointment;
import com.abutua.appointment.domain.entities.AppointmentType;
import com.abutua.appointment.domain.entities.Area;
import com.abutua.appointment.domain.entities.Client;
import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.domain.entities.WorkScheduleItem;
import com.abutua.appointment.domain.repositories.AppointmentRepository;
import com.abutua.appointment.domain.repositories.AppointmentTypeRepository;
import com.abutua.appointment.domain.repositories.AreaRepository;
import com.abutua.appointment.domain.repositories.ClientRepository;
import com.abutua.appointment.domain.repositories.ProfessionalRepository;
import com.abutua.appointment.domain.repositories.WorkScheduleItemRepository;


@Component
@Profile("runner")
public class DBRunnerTestEntities implements ApplicationRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private WorkScheduleItemRepository workScheduleItemRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        Client c1 = clientRepository.findById(2L).get();
        System.out.println(c1);

        Professional p1 = professionalRepository.findById(4L).get();
        System.out.println(p1);

        Area a1 = areaRepository.findById(1).get();
        System.out.println(a1);

        AppointmentType at1 = appointmentTypeRepository.findById(1).get();
        System.out.println(at1);

        Appointment appointment = new Appointment();

        appointment.setClient(c1);
        appointment.setProfessional(p1);
        appointment.setArea(a1);
        appointment.setAppointmentType(at1);
        appointment.setDate(LocalDate.now());
        appointment.setStartTime(LocalTime.parse("08:00:00"));
        appointment.setEndTime(LocalTime.parse("08:00:00"));
        appointment.setComments("Teste do runner");

        appointmentRepository.save(appointment);

        WorkScheduleItem ws = new WorkScheduleItem(DayOfWeek.MONDAY, LocalTime.parse("08:00:00"), LocalTime.parse("12:00:00"), 8, 30);
        this.workScheduleItemRepository.save(ws);

    }
    
}
