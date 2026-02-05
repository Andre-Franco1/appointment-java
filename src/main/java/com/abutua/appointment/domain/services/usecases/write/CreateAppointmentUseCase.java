package com.abutua.appointment.domain.services.usecases.write;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.abutua.appointment.domain.entities.Appointment;
import com.abutua.appointment.domain.entities.AppointmentType;
import com.abutua.appointment.domain.entities.Area;
import com.abutua.appointment.domain.entities.Client;
import com.abutua.appointment.domain.entities.Professional;
import com.abutua.appointment.domain.repositories.AppointmentRepository;
import com.abutua.appointment.domain.repositories.AppointmentTypeRepository;
import com.abutua.appointment.domain.repositories.AreaRepository;
import com.abutua.appointment.domain.repositories.ClientRepository;
import com.abutua.appointment.domain.repositories.ProfessionalRepository;
import com.abutua.appointment.domain.services.exceptions.BusinessException;
import com.abutua.appointment.domain.services.usecases.read.SearchProfessionalAvailabilityTimesUseCase;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CreateAppointmentUseCase {

    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private SearchProfessionalAvailabilityTimesUseCase searchProfessionalAvailabilityTimesUseCase;

    @Transactional
    public Appointment executeUseCase(Appointment appointment) {

        checkAppointmentTypeExistsOrThrowsException(appointment.getAppointmentType());
        checkAreaExistsOrThrowsException(appointment.getArea());

        Professional professional = getProfessionalIfExistsOrThrowsException(appointment.getProfessional());
        checkProfessionalIsActiveOrThrowsException(professional);
        checkAssociationBetweenProfessionalAndAreaOrThrowsException(professional, appointment.getArea());
        
        checkProfessionalHasAvailableScheduleOrThrowsException(professional, appointment);

        checkAppointmentIsNowOrFutureOrThrowsException(appointment.getDate(), appointment.getStartTime());

        Client client = getClientIfExistsOrThrowsException(appointment.getClient());
        
        return save(appointment, client, professional);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private Appointment save(Appointment appointment, Client client, Professional professional){
        CheckProfessionalCanCreateAppointmentAtDateAndTimeOrThrowsException(professional, appointment);
        CheckClientCanCreateAppointmentAtDateAndTimeOrThrowsException(client, appointment);
        return this.appointmentRepository.save(appointment);
    }

    private void checkAppointmentTypeExistsOrThrowsException(AppointmentType appointmentType) {
        if (!this.appointmentTypeRepository.existsById(appointmentType.getId())) {
            throw new EntityNotFoundException("Tipo de agendamento não cadastrado.");
        }
    }

    private void checkAreaExistsOrThrowsException(Area area) {
        if (!this.areaRepository.existsById(area.getId())) {
            throw new EntityNotFoundException("Área não cadastrada.");
        }
    }

    private Professional getProfessionalIfExistsOrThrowsException(Professional professional) {
        return this.professionalRepository.findById(professional.getId())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não cadastrado."));
    }

    private Client getClientIfExistsOrThrowsException(Client client) {
        return this.clientRepository.findById(client.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não cadastrado."));
    }

    private void checkProfessionalIsActiveOrThrowsException(Professional professional) {
        if (!professional.isActive()) {
            throw new BusinessException("Profissional inativo.");
        }
    }

    private void checkAssociationBetweenProfessionalAndAreaOrThrowsException(Professional professional, Area area) {
        if (!this.professionalRepository.existsAssociationWithArea(professional.getId(), area.getId())) {
            throw new BusinessException("O profissional não atua na área selecionada.");
        }
    }

    private void checkAppointmentIsNowOrFutureOrThrowsException(LocalDate date, LocalTime startTime) {
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException("A data do agendamento está no passado.");
        } else {
            if (date.equals(LocalDate.now()) && startTime.isBefore(LocalTime.now())) {
                throw new BusinessException("O horário do agendamento está no passado.");
            }
        }
    }

    private void CheckClientCanCreateAppointmentAtDateAndTimeOrThrowsException(Client client, Appointment appointment) {
        if (this.appointmentRepository.existsOpenOrPresentAppointmentsForClient(client, appointment.getDate(),
                appointment.getStartTime(), appointment.getEndTime())) {
            throw new BusinessException("O cliente possui agendamentos em aberto para o dia e horário selecionado.");
        }
    }

    private void CheckProfessionalCanCreateAppointmentAtDateAndTimeOrThrowsException(Professional professional,
            Appointment appointment) {
        if (this.appointmentRepository.existsOpenOrPresentAppointmentsForProfessional(professional,
                appointment.getDate(),
                appointment.getStartTime(), appointment.getEndTime())) {
            throw new BusinessException(
                    "O profissional possui agendamentos em aberto para o dia e horário selecionado.");
        }
    }

    private void checkProfessionalHasAvailableScheduleOrThrowsException(Professional professional,
            Appointment appointment) {
        var timeSlots = searchProfessionalAvailabilityTimesUseCase.executeUseCase(professional.getId(), appointment.getDate());

        if (timeSlots.isEmpty()) {
            throw new BusinessException("O profissional não trabalha na data selecionada.");
        } else {
            var timeSlot = timeSlots.stream().filter(ts -> ts.getStartTime().toLocalTime().equals(appointment.getStartTime())
                    && ts.getEndTime().toLocalTime().equals(appointment.getEndTime())).findFirst();
            if(timeSlot.isEmpty()){
                throw new BusinessException("O profissional não trabalha no horário selecionado.");
            }
            if(!timeSlot.get().isAvailable()){
                throw new BusinessException("O profissional não tem disponibilidade para o horário selecionado.");
            }
        }

    }

}
