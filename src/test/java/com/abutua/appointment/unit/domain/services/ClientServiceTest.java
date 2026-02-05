package com.abutua.appointment.unit.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abutua.appointment.domain.entities.Client;
import com.abutua.appointment.domain.repositories.ClientRepository;
import com.abutua.appointment.domain.services.ClientService;
import com.abutua.appointment.dto.ClientRequest;
import com.abutua.appointment.dto.ClientResponse;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void saveShouldPersistClient() {
        //TODO add comments attribute
        Client newClient = new Client("Ana", "11929343234", LocalDate.parse("2000-10-01"));
        Client savedClient = new Client(1L, "Ana", "11929343234", LocalDate.parse("2000-10-01"));

        given(clientRepository.save(newClient)).willReturn(savedClient);

        ClientRequest newClientRequest = new ClientRequest("Ana", "11929343234", LocalDate.parse("2000-10-01"));
        ClientResponse expClientResponse = new ClientResponse(1L, "Ana", "11929343234", LocalDate.parse("2000-10-01"));

        var foundClientResponse = clientService.save(newClientRequest);

        verify(clientRepository).save(any(Client.class));

        assertNotNull(foundClientResponse);
        assertNotNull(foundClientResponse.id());

        assertEquals(expClientResponse.id(), foundClientResponse.id());
        assertEquals(expClientResponse.name(), foundClientResponse.name());
        assertEquals(expClientResponse.phone(), foundClientResponse.phone());
        assertEquals(expClientResponse.dateOfBirth(), foundClientResponse.dateOfBirth());
        //assertEquals(expClientResponse.comments(), foundClientResponse.comments());
    }

    @Test
    void updateShouldPersistClient() {
        Client client = mock(Client.class);
    
        given(clientRepository.getReferenceById(anyLong())).willReturn(client);
        given(clientRepository.save(client)).willReturn(client);

        ClientRequest clientRequest = new ClientRequest("Ana", "11929343234", LocalDate.parse("2000-10-01"));

        clientService.update(anyLong(), clientRequest);

        verify(client).setName(anyString());
        verify(client).setPhone(anyString());
        //verify(client).setComments(anyString());
        verify(client).setDateOfBirth(any(LocalDate.class));

        verify(clientRepository).save(any(Client.class));
        verify(clientRepository).getReferenceById(anyLong());

    }

    @Test
    void updateShouldThrowEntityNotFoundException() {
        given(clientRepository.getReferenceById(anyLong())).willThrow(EntityNotFoundException.class);
        ClientRequest clientRequest = mock(ClientRequest.class);
        assertThrows(EntityNotFoundException.class, () -> clientService.update(anyLong(), clientRequest));
        verify(clientRepository).getReferenceById(any(Long.class));
    }
}
