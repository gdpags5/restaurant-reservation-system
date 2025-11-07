package io.gdpags5.rrs.services;

import io.gdpags5.rrs.commons.BaseService;
import io.gdpags5.rrs.dtos.ReservationDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationService extends BaseService<ReservationDTO, Long> {
    List<ReservationDTO> findReservationsByCustomerId(@Param("id") Long customerId);
}
