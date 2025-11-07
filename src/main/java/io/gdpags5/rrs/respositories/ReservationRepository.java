package io.gdpags5.rrs.respositories;

import io.gdpags5.rrs.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = """
                   SELECT r
                   FROM Reservation r
                   WHERE r.customer.id = :id
                   ORDER BY r.reservationDate, r.reservationTime ASC
                   """)
    List<Reservation> findByCustomerId(@Param("id") Long customerId);
}
