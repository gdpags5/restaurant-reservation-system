package io.gdpags5.rrs.services;

import io.gdpags5.rrs.dtos.ReservationDTO;
import io.gdpags5.rrs.utils.EmailUtil;
import io.gdpags5.rrs.utils.SmsUtil;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {
    private SmsUtil smsUtil;
    private EmailUtil emailUtil;

    public NotificationService() {
        smsUtil = new SmsUtil();
        emailUtil = new EmailUtil();
    }

    public void sendReservationNotification(ReservationDTO reservationDTO) {
        String message = String.format("Hi %s, your reservation on %s at %s has been %s.",
                reservationDTO.customerDTO().fullName(),
                DateTimeFormatter.ofPattern("MMMM dd, yyyy").format(reservationDTO.reservationDate()),
                DateTimeFormatter.ofPattern("hh:mm a").format(reservationDTO.reservationTime()),
                reservationDTO.status());

        switch (reservationDTO.methodOfNotification()) {
            case SMS -> smsUtil.sendSms(reservationDTO.customerDTO().phoneNumber(), message);
            case EMAIL -> emailUtil.sendEmail(reservationDTO.customerDTO().email(), "Reservation " + reservationDTO.status(), message);
        }
    }

    public void sendReminderNotification(ReservationDTO reservationDTO) {
        String message = String.format("Hi %s, this is a reminder for your reservation today at on %s.",
                reservationDTO.customerDTO().fullName(),
                reservationDTO.reservationTime());

        smsUtil.sendSms(reservationDTO.customerDTO().phoneNumber(), message);
        emailUtil.sendEmail(reservationDTO.customerDTO().email(), "Reservation Reminder", message);
    }
}
