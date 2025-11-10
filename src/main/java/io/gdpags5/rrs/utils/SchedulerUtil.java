package io.gdpags5.rrs.utils;

import io.gdpags5.rrs.dtos.ReservationDTO;
import io.gdpags5.rrs.services.NotificationService;
import io.gdpags5.rrs.services.ReservationService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class SchedulerUtil {
    private final ReservationService reservationService;
    private final NotificationService notificationService;

    public SchedulerUtil(ReservationService reservationService, NotificationService notificationService) {
        this.reservationService = reservationService;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 60000)
    public void sendReminderNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime startReminderTime = now.toLocalTime().plusHours(4).minusMinutes(1);
        LocalTime endReminderTime = now.toLocalTime().plusHours(4).plusMinutes(1);

        List<ReservationDTO> upcomingReservations = reservationService.findReservationsByReminderWindow(now.toLocalDate(), startReminderTime, endReminderTime);
        for (ReservationDTO reservatioDTO : upcomingReservations) {
            notificationService.sendReminderNotification(reservatioDTO);
        }
    }
}
