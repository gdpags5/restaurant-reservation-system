package io.gdpags5.rrs.controllers;

import io.gdpags5.rrs.dtos.ReservationDTO;
import io.gdpags5.rrs.enums.ReservationStatus;
import io.gdpags5.rrs.services.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Create a new reservation",
               description = "Creates a reservation for a customer. Requires all reservation details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                         description = "Reservation successfully created and is in pending status.",
                         content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ReservationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid reservation details. Please check your input."),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred while processing the request.")
    })
    @PostMapping
    public ResponseEntity<ReservationDTO> newReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO newlyCreatedReservation = reservationService.saveOrUpdate(reservationDTO);
        return ResponseEntity.ok(newlyCreatedReservation);
    }

    @Operation(summary = "Cancel an existing reservation",
               description = """
                             Cancel a reservation using its ID. The reservation status will be updated and a
                             confirmation will be sent.
                             """)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                         description = "Reservation successfully cancelled. A notification has been sent."),
            @ApiResponse(responseCode = "404", description = "No reservation found with the given ID."),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred while processing the request.")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long id) {
        Optional<ReservationDTO> existingReservation = reservationService.findById(id);

        if (existingReservation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ReservationDTO reservationToBeCancelled = existingReservation.get();
        ReservationDTO cancelledReservation = new ReservationDTO(
                reservationToBeCancelled.id(),
                reservationToBeCancelled.customerDTO(),
                reservationToBeCancelled.numberOfGuests(),
                reservationToBeCancelled.reservationDate(),
                reservationToBeCancelled.reservationTime(),
                ReservationStatus.CANCELLED,
                reservationToBeCancelled.methodOfNotification(),
                reservationToBeCancelled.dateAndTimeUpdated()
        );

        ReservationDTO updatedReservation = reservationService.saveOrUpdate(cancelledReservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @Operation(summary = "Update reservation details",
               description = "Update the time and number of guests for an existing reservation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                         description = "Reservation updated successfully. A notification has been sent."),
            @ApiResponse(responseCode = "400", description = "Invalid reservation details. Please check your input."),
            @ApiResponse(responseCode = "404", description = "No reservation found with the given ID.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id,
                                                            @Valid @RequestBody ReservationDTO reservationDTO) {
        Optional<ReservationDTO> existingReservation = reservationService.findById(id);
        if (existingReservation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ReservationDTO current = existingReservation.get();
        ReservationDTO reservationToBeUpdated = new ReservationDTO(
                current.id(),
                current.customerDTO(),
                reservationDTO.numberOfGuests(),
                current.reservationDate(),
                reservationDTO.reservationTime(),
                current.status(),
                current.methodOfNotification(),
                current.dateAndTimeUpdated()
        );

        ReservationDTO updatedReservation = reservationService.saveOrUpdate(reservationToBeUpdated);
        return ResponseEntity.ok(updatedReservation);
    }

    @Operation(summary = "Get all reservations for a customer",
               description = "Returns a list of reservations made by the specified customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of reservations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No reservations found for the given customer ID")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReservationDTO>> findReservationsByCustomerId(@PathVariable Long customerId) {
        List<ReservationDTO> listOfCustomerReservations = reservationService.findReservationsByCustomerId(customerId);
        return listOfCustomerReservations.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(listOfCustomerReservations);
    }
}
