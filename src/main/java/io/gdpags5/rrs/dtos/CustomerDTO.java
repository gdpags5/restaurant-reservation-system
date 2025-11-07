package io.gdpags5.rrs.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CustomerDTO(
        @Schema(description = """
                              The customer identification number. If you are creating a new customer, it will generate
                              an identification number. If the customer is existing, it will display the identification
                              number.
                              """)
        @Positive(message = "Customer identification number must be a positive number")
        Long id,

        @Schema(description = "Customer's full name", example = "Juan dela Cruz")
        @NotBlank
        @Size(min = 5, max = 100)
        String fullName,

        @Schema(description = "Customer's mobile phone number. Omit +63 or 0 prefix. ", example = "9123456789")
        @NotBlank(message = "Mobile number is required.")
        @Pattern(regexp = "^9\\d{9}$", message = "Mobile number must start with 9 and be 10 digits long (no +63 or 0)")
        String phoneNumber,

        @Schema(description = "Customer's email address.", example = "juandelaxruz@email.com")
        @NotBlank(message = "Email address is required.")
        @Email(message = "Invalid email format.")
        String email,

        @Schema(description = """
                              Displays the date and time where the customer record was last updated. Any attempt to
                              modify the date and time manually not reflect in the database.
                              """)
        LocalDateTime dateAndTimeUpdated
) {}
