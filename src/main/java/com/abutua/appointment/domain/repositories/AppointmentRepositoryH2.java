package com.abutua.appointment.domain.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abutua.appointment.domain.models.TimeSlot;

@Repository
@Profile({"test","runner"})
public interface AppointmentRepositoryH2 extends AppointmentRepository{

    /* 
     
            WITH RECURSIVE SequencialCTE(cont)  AS (
                SELECT 0
                UNION ALL
                SELECT cont+1 FROM  SequencialCTE WHERE cont < 31
            ),
            WorkSchedule (_start_time, _start_time_plus_inc, _slot_size, _end_time, _day_of_week) AS (
                SELECT 
                        start_time, 
                        DATEADD(MINUTE, slot_size, start_time) ,
                        slot_size,
                        end_time,
                        day_of_week
                FROM 
                        TBL_WORK_SCHEDULE_ITEM 
                WHERE 
                        professional_id = 4 
                    
                UNION ALL

                SELECT 
                        DATEADD(MINUTE, _slot_size, _start_time), 
                        DATEADD(MINUTE, _slot_size, _start_time_plus_inc), 
                        _slot_size,
                        _end_time,
                        _day_of_week
                FROM
                        WorkSchedule
                WHERE 
                        _start_time <   DATEADD(MINUTE, -_slot_size, _end_time)  
            )
            SELECT 
                    DISTINCT DAY(work_day_available)
            FROM 
                        WorkSchedule
            JOIN (
                    SELECT 
                                DATEADD('DAY', cont, '2024-04-01') AS work_day_available
                    FROM (
                                SELECT 
                                        cont
                                FROM 
                                    SequencialCTE
                    )
                    WHERE 
                            cont <=  DATEDIFF('DAY', '2024-04-01', '2024-04-30')
            ) 
            ON DAY_OF_WEEK(work_day_available) = _day_of_week

            LEFT JOIN 
                TBL_APPOINTMENT a
            ON 
                a.professional_id = 4                             AND
                a.start_time <  _start_time_plus_inc     AND
                a.end_time  > _start_time                     AND
                a.date          =  work_day_available      AND
                (a.status = 'OPEN' OR a.status = 'PRESENT')
            WHERE ID IS NULL 

    */
    @Query(value=
      "WITH RECURSIVE SequencialCTE(cont)  AS ( "+
      "          SELECT 0 "+
      "          UNION ALL "+
      "          SELECT cont+1 FROM  SequencialCTE WHERE cont < 31 "+
      "      ), "+
      "      WorkSchedule (_start_time, _start_time_plus_inc, _slot_size, _end_time, _day_of_week) AS ( "+
      "          SELECT  "+
      "                  start_time,  "+
      "                  DATEADD(MINUTE, slot_size, start_time) , "+
      "                  slot_size, "+
      "                  end_time, "+
      "                  day_of_week "+
      "          FROM  "+
      "                  TBL_WORK_SCHEDULE_ITEM  "+
      "          WHERE  "+
      "                  professional_id = :professionalId  "+
      "               "+
      "          UNION ALL "+
" "+
      "          SELECT  "+
      "                  DATEADD(MINUTE, _slot_size, _start_time),  "+
      "                  DATEADD(MINUTE, _slot_size, _start_time_plus_inc),  "+
      "                  _slot_size, "+
      "                  _end_time, "+
      "                  _day_of_week "+
      "          FROM "+
      "                  WorkSchedule "+
      "          WHERE  "+
      "                  _start_time <   DATEADD(MINUTE, -_slot_size, _end_time)   "+
      "      ) "+
      "      SELECT  "+
      "              DISTINCT DAY(work_day_available) "+
      "      FROM  "+
      "                  WorkSchedule "+
      "      JOIN ( "+
      "              SELECT  "+
      "                          DATEADD('DAY', cont, TRIM(:start) ) AS work_day_available "+
      "              FROM ( "+
      "                          SELECT  "+
      "                                  cont "+
      "                          FROM  "+
      "                              SequencialCTE "+
      "              ) "+
      "              WHERE  "+
      "                      cont <=  DATEDIFF('DAY', :start, :end ) "+
      "      )  "+
      "      ON DAY_OF_WEEK(work_day_available) = _day_of_week "+
      " "+
      "      LEFT JOIN  "+
      "          TBL_APPOINTMENT a "+
      "      ON  "+
      "          a.professional_id = :professionalId           AND "+
      "          a.start_time <  _start_time_plus_inc          AND "+
      "          a.end_time  > _start_time                     AND "+
      "          a.date          =  work_day_available         AND "+
      "          (a.status = 'OPEN' OR a.status = 'PRESENT') "+
      "      WHERE ID IS NULL  "
    , nativeQuery = true)
    public List<Integer> getAvailableDaysFromProfessional(long professionalId, LocalDate start, LocalDate end);


    /*
                
                WITH RECURSIVE 
                        WorkSchedule (_start_time, _start_time_plus_inc, _slot_size, _end_time, _day_of_week) AS (
                SELECT
                        start_time,
                        DATEADD(MINUTE, slot_size, start_time) AS _start_time_plus_inc,
                        slot_size,
                        end_time,
                        day_of_week
                FROM
                        TBL_WORK_SCHEDULE_ITEM
                WHERE
                        professional_id = 4 AND
                        day_of_week = 3

                UNION ALL

                SELECT
                        DATEADD(MINUTE, _slot_size, _start_time),
                        DATEADD(MINUTE, _slot_size, _start_time_plus_inc),
                        _slot_size,
                        _end_time,
                        _day_of_week
                FROM
                        WorkSchedule
                WHERE
                        _start_time < DATEADD(MINUTE, -_slot_size, _end_time)
                )

                SELECT 
                        _start_time as startTime,
                        _start_time_plus_inc as endTime,
                        CASE WHEN 
                                date IS NULL THEN true
                        ELSE 
                                false
                        END as available
                FROM 
                        WorkSchedule 
                LEFT JOIN 
                        TBL_APPOINTMENT a 
                ON
                        a.professional_id = 4                                           AND
                        a.date = '2024-04-16'                                          AND
                        a.start_time < _start_time_plus_inc                   AND 
                        a.end_time >  _start_time                                  AND 
                        (a.status = 'OPEN' OR a.status = 'PRESENT' )
                ORDER BY startTime
      
      
     */
    @Query(value=

        "    WITH RECURSIVE  " +
        "    WorkSchedule (_start_time, _start_time_plus_inc, _slot_size, _end_time, _day_of_week) AS ( " +
        "SELECT " +
        "    start_time, " +
        "    DATEADD(MINUTE, slot_size, start_time) AS _start_time_plus_inc, " +
        "    slot_size, " +
        "    end_time, " +
        "    day_of_week " +
        "FROM " +
        "    TBL_WORK_SCHEDULE_ITEM " +
        "WHERE " +
        "    professional_id = :professionalId AND " +
        "    day_of_week = DAY_OF_WEEK(:date) " +
        " " +
        "UNION ALL " +
        " " +
        "SELECT " +
        "    DATEADD(MINUTE, _slot_size, _start_time), " +
        "    DATEADD(MINUTE, _slot_size, _start_time_plus_inc), " +
        "    _slot_size, " +
        "    _end_time, " +
        "    _day_of_week " +
        "FROM " +
        "    WorkSchedule " +
        "WHERE " +
        "    _start_time < DATEADD(MINUTE, -_slot_size, _end_time) " +
        ") " +
        " " +
        "SELECT  " +
        "    _start_time as startTime, " +
        "    _start_time_plus_inc as endTime, " +
        "    CASE WHEN  " +
        "            date IS NULL THEN true " +
        "    ELSE  " +
        "            false " +
        "    END as available " +
        "FROM  " +
        "    WorkSchedule  " +
        "LEFT JOIN  " +
        "    TBL_APPOINTMENT a  " +
        "ON " +
        "    a.professional_id = :professionalId                   AND " +
        "    a.date = :date                                        AND " +
        "    a.start_time < _start_time_plus_inc                   AND  " +
        "    a.end_time >  _start_time                             AND  " +
        "    (a.status = 'OPEN' OR a.status = 'PRESENT' ) " +
        "ORDER BY startTime "
    , nativeQuery = true)
    public List<TimeSlot> getAvailableTimesFromProfessional(long professionalId, LocalDate date);


}
