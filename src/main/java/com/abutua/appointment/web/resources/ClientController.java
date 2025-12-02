package com.abutua.appointment.web.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abutua.appointment.domain.services.ClientService;
import com.abutua.appointment.dto.ClientRequest;
import com.abutua.appointment.dto.ClientResponse;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> getClients(
            @RequestParam(name = "name_like", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(clientService.findByNameContainingIgnoreCase(name, page, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable long id) {
        var client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public ResponseEntity<ClientResponse> saveClient(@Validated @RequestBody ClientRequest clientRequest) {
        var clientResponse = clientService.save(clientRequest);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clientResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(clientResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateClient(@PathVariable int id, @Validated @RequestBody ClientRequest clientRequest) {
        clientService.update(id, clientRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
