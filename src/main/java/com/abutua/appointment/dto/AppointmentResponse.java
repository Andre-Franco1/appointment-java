package com.abutua.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.abutua.appointment.domain.entities.AppointmentStatus;

public record AppointmentResponse (
    long id,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    String comments,
    AppointmentStatus status,
    IntegerDTO type,
    IntegerDTO area,
    LongDTO professional,
    LongDTO client){
    
}

// {
//     "id": 35,
//     "date": "2025-06-26",
//     "startTime": "15:30:00",
//     "endTime": "16:00:00",
//     "comments": "no comments",
//     "status": "OPEN",
//     "type": {
//         "id": 1
//     },
//     "area": {
//         "id": 2
//     },
//     "professional": {
//         "id": 5
//     },
//     "client": {
//         "id": 2
//     }
// }