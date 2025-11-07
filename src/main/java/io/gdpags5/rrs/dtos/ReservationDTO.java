package io.gdpags5.rrs.dtos;

import io.gdpags5.rrs.enums.NotificationMode;
import io.gdpags5.rrs.enums.ReservationStatus;
import io.gdpags5.rrs.validators.ValidTimeRange;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationDTO(
        @Schema(description = """
                              The reservation identification number. If you are creating a new reservation, it will be
                              generated automatically. If the reservation is existing, it will display the
                              identification number.
                              """)
        @Positive(message = "Reservation identification number must be a positive number.")
        Long id,

        @Schema(description = "Customer information associated with the reservation.")
        @NotNull(message = "Customer information is required")
        @Valid
        CustomerDTO customerDTO,

        @Schema(description = "Number of guests for the reservation. Maximum 20 guests allowed.", example = "4")
        @NotNull(message = "Number of guests is required")
        @Min(value = 1, message = "At least one guest is required")
        @Max(value = 20, message = "Maximum of 20 guests allowed")
        Integer numberOfGuests,

        @Schema(description = "Date of the reservation. Reservation date must be today or later.",
                example = "2025-11-10")
        @NotNull(message = "Reservation date is required")
        @FutureOrPresent(message = "Reservation date must be today or later")
        LocalDate reservationDate,

        @Schema(description = "Time of the reservation. Should be between 10:00AM to 8:00PM. Time is in 24-hour format",
                example = "18:30")
        @NotNull(message = "Reservation time is required")
        @ValidTimeRange(message = "Reservation time should be between 10:00 to 20:00", start = "10:00", end = "20:00")
        LocalTime reservationTime,

        @Schema(description = "Reservation status. The acceptable statuses are PENDING, RESERVED, CANCELLED, REJECTED",
                example = "RESERVED")
        @NotNull(message = "Reservation status is required")
        ReservationStatus status,

        @Schema(description = "Method used to notify the customer", example = "SMS")
        @NotNull(message = "Method of notification is required")
        NotificationMode methodOfNotification,

        @Schema(description = """
                              Displays the date and time when the reservation was last updated. Manual changes will
                              not reflect in the database.
                              """)
        LocalDateTime dateAndTimeUpdated
) {}
