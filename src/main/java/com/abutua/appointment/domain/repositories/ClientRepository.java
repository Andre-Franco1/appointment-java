package com.abutua.appointment.domain.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abutua.appointment.domain.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // @Query(value = "SELECT * FROM TBL_CLIENT AS c JOIN TBL_PERSON AS p ON c.person_id = p.id", nativeQuery = true)
    // public List<Client> myFindAll();

    // @Query(value = "SELECT * FROM TBL_CLIENT AS c JOIN TBL_PERSON AS p ON c.person_id = p.id WHERE UPPER(p.name) LIKE %:name%", nativeQuery = true)
    // public List<Client> myFindAll(@Param("name")String name);

    Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
