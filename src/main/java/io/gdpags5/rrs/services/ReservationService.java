package io.gdpags5.rrs.services;

import io.gdpags5.rrs.commons.BaseService;
import io.gdpags5.rrs.dtos.ReservationDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationService extends BaseService<ReservationDTO, Long> {
    List<ReservationDTO> findReservationsByCustomerId(Long customerId);
    List<ReservationDTO> findReservationsByReminderWindow(LocalDate date, LocalTime startTime, LocalTime endTime);
}
