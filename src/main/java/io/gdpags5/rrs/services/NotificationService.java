package io.gdpags5.rrs.services;

import io.gdpags5.rrs.dtos.CustomerDTO;
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

    public void sendReservationNotification(CustomerDTO customerDTO, ReservationDTO reservationDTO) {
        String message = String.format("Hi %s, your reservation on %s at %s has been %s.",
                customerDTO.fullName(),
                DateTimeFormatter.ofPattern("MMMM dd, yyyy").format(reservationDTO.reservationDate()),
                DateTimeFormatter.ofPattern("hh:mm a").format(reservationDTO.reservationTime()),
                reservationDTO.status());

        switch (reservationDTO.methodOfNotification()) {
            case SMS -> smsUtil.sendSms(customerDTO.phoneNumber(), message);
            case EMAIL -> emailUtil.sendEmail(customerDTO.email(), "Reservation " + reservationDTO.status(), message);
        }
    }

}
