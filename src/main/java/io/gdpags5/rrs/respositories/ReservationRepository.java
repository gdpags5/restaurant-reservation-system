package io.gdpags5.rrs.respositories;

import io.gdpags5.rrs.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = """
                   SELECT r
                   FROM Reservation r
                   WHERE r.customer.id = :id
                   ORDER BY r.reservationDate, r.reservationTime ASC
                   """)
    List<Reservation> findByCustomerId(@Param("id") Long customerId);

    @Query(value = """
                   SELECT r
                   FROM Reservation r 
                   WHERE r.reservationDate = :date 
                   AND r.reservationTime BETWEEN :startTime AND :endTime
                   """)
    List<Reservation> findReservationByReminderWindow(@Param("date") LocalDate date,
                                                      @Param("startTime") LocalTime startTime,
                                                      @Param("endTime") LocalTime endTime);

}
