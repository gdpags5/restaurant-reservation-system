package io.gdpags5.rrs.services.impls;

import io.gdpags5.rrs.dtos.ReservationDTO;
import io.gdpags5.rrs.entities.Customer;
import io.gdpags5.rrs.entities.Reservation;
import io.gdpags5.rrs.respositories.CustomerRepository;
import io.gdpags5.rrs.respositories.ReservationRepository;
import io.gdpags5.rrs.services.CustomerService;
import io.gdpags5.rrs.services.NotificationService;
import io.gdpags5.rrs.services.ReservationService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final NotificationService notificationService;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  CustomerRepository customerRepository,
                                  CustomerService customerService, NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.notificationService = notificationService;
    }

    @Override
    public ReservationDTO saveOrUpdate(ReservationDTO dto) {
        Customer customer;

        if (dto.customerDTO().id() == null) {
            Customer customerToBedSaved = new Customer();
            customerToBedSaved.setFullName(dto.customerDTO().fullName());
            customerToBedSaved.setPhoneNumber(Long.valueOf(dto.customerDTO().phoneNumber()));
            customerToBedSaved.setEmail(dto.customerDTO().email());
            customerToBedSaved.setDateAndTimeCreated(LocalDateTime.now());
            customerToBedSaved.setDateAndTimeUpdated(LocalDateTime.now());

            customer = customerRepository.save(customerToBedSaved);
        } else {
            customer = customerRepository.getReferenceById(dto.customerDTO().id());
        }

        Reservation reservationToBeSavedOrUpdated = dto.id() != null
                ? reservationRepository.getReferenceById(dto.id())
                : new Reservation();
        reservationToBeSavedOrUpdated.setCustomer(customer);
        reservationToBeSavedOrUpdated.setNumberOfGuests(dto.numberOfGuests());
        reservationToBeSavedOrUpdated.setReservationDate(dto.reservationDate());
        reservationToBeSavedOrUpdated.setReservationTime(dto.reservationTime());
        reservationToBeSavedOrUpdated.setStatus(dto.status());
        reservationToBeSavedOrUpdated.setMethodOfNotification(dto.methodOfNotification());
        reservationToBeSavedOrUpdated.setDateAndTimeUpdated(LocalDateTime.now());
        if (dto.id() == null) reservationToBeSavedOrUpdated.setDateAndTimeCreated(LocalDateTime.now());

        Reservation savedOrUpdatedReservation = reservationRepository.save(reservationToBeSavedOrUpdated);
        ReservationDTO savedOrUpdateReservationDTO =  new ReservationDTO(savedOrUpdatedReservation.getId(),
                                customerService.findById(savedOrUpdatedReservation.getCustomer().getId()).orElseThrow(),
                                savedOrUpdatedReservation.getNumberOfGuests(),
                                savedOrUpdatedReservation.getReservationDate(),
                                savedOrUpdatedReservation.getReservationTime(),
                                savedOrUpdatedReservation.getStatus(),
                                savedOrUpdatedReservation.getMethodOfNotification(),
                                savedOrUpdatedReservation.getDateAndTimeUpdated());

        // Call the notification service and send the reservation notification.
        notificationService.sendReservationNotification(savedOrUpdateReservationDTO);

        return savedOrUpdateReservationDTO;
    }

    @Override
    public Optional<ReservationDTO> findById(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.map(reservation -> new ReservationDTO(reservation.getId(),
                    customerService.findById(reservation.getCustomer().getId()).orElseThrow(),
                    reservation.getNumberOfGuests(),
                    reservation.getReservationDate(),
                    reservation.getReservationTime(),
                    reservation.getStatus(),
                    reservation.getMethodOfNotification(),
                    reservation.getDateAndTimeUpdated()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(ReservationDTO dto) {
        reservationRepository.deleteById(dto.id());
    }

    @Override
    public List<ReservationDTO> findAll() {
        return reservationRepository.findAll().stream().map(reservation -> new ReservationDTO(reservation.getId(),
                customerService.findById(reservation.getCustomer().getId()).orElseThrow(),
                reservation.getNumberOfGuests(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getStatus(),
                reservation.getMethodOfNotification(),
                reservation.getDateAndTimeUpdated())).toList();
    }

    @Override
    public List<ReservationDTO> findReservationsByCustomerId(Long customerId) {
        return reservationRepository.findByCustomerId(customerId).stream()
                .map(reservation -> new ReservationDTO(reservation.getId(),
                        customerService.findById(reservation.getCustomer().getId()).orElseThrow(),
                        reservation.getNumberOfGuests(),
                        reservation.getReservationDate(),
                        reservation.getReservationTime(),
                        reservation.getStatus(),
                        reservation.getMethodOfNotification(),
                        reservation.getDateAndTimeUpdated())).toList();
    }

    @Override
    public List<ReservationDTO> findReservationsByReminderWindow(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return reservationRepository.findReservationByReminderWindow(date, startTime, endTime).stream()
                .map(reservation -> new ReservationDTO(reservation.getId(),
                        customerService.findById(reservation.getCustomer().getId()).orElseThrow(),
                        reservation.getNumberOfGuests(),
                        reservation.getReservationDate(),
                        reservation.getReservationTime(),
                        reservation.getStatus(),
                        reservation.getMethodOfNotification(),
                        reservation.getDateAndTimeUpdated())).toList();
    }
}
