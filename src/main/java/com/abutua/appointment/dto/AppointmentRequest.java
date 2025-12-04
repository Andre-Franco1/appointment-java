package com.abutua.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest (
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    String comments,
    IntegerDTO type,
    IntegerDTO area,
    LongDTO professional,
    LongDTO client) {
    
}


// {
//     "date": "2025-06-26",
//     "startTime": "15:30:00",
//     "endTime": "16:00:00",
//     "comments": "no comments",
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