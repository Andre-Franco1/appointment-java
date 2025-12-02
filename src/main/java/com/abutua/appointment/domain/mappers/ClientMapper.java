package com.abutua.appointment.domain.mappers;

import com.abutua.appointment.domain.entities.Client;
import com.abutua.appointment.dto.ClientRequest;
import com.abutua.appointment.dto.ClientResponse;

public class ClientMapper {

    public static ClientResponse toClientResponseDTO(Client client) {

        ClientResponse clientResponse = new ClientResponse(
                client.getId(),
                client.getName(),
                client.getPhone(),
                client.getDateOfBirth());
        return clientResponse;
    }

    public static Client fromClientRequestDTO(ClientRequest clientRequest) {
        return new Client(
            clientRequest.name(),
            clientRequest.phone(),
            clientRequest.dateOfBirth());
    }

}
