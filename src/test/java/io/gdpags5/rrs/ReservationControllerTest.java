package io.gdpags5.rrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.gdpags5.rrs.controllers.ReservationController;
import io.gdpags5.rrs.dtos.CustomerDTO;
import io.gdpags5.rrs.dtos.ReservationDTO;
import io.gdpags5.rrs.enums.NotificationMode;
import io.gdpags5.rrs.enums.ReservationStatus;
import io.gdpags5.rrs.services.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@Import(ReservationControllerTest.MockServiceConfig.class)
@AutoConfigureMockMvc
public class ReservationControllerTest {
    // Create a test configuration that will call the service inside the controller.
    // This also replaces the deprecated @MockBean
    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public ReservationService reservationService() {
            return mock(ReservationService.class);
        }
    }

    @Autowired private MockMvc mockMvc;
    @Autowired private ReservationService reservationService;
    @Autowired private ObjectMapper objectMapper;
    private ReservationDTO mockNewReservation,
            mockExistingReservation,
            mockCancelledReservation,
            mockUpdatedReservation;
    private CustomerDTO mockCustomer;

    @BeforeEach
    public void setup() {
        // This is for attributes that has LocalDateTime, LocalDate and LocalTime object data types.
        objectMapper.registerModule(new JavaTimeModule());

        // Create a mock customer object.
        mockCustomer = new CustomerDTO(
                101L,
                "Juan dela Cruz",
                "9123456789",
                "juandelacruz@email.com",
                LocalDateTime.of(2025, 11, 8, 9,30,30)
        );

        // Create a mock reservation object for new reservation.
        mockNewReservation = new ReservationDTO(
                null,
                mockCustomer,
                10,
                LocalDate.of(2025, 11, 11),
                LocalTime.of(18, 30),
                ReservationStatus.RESERVED,
                NotificationMode.EMAIL,
                null
        );

        // Create a mock reservation object for existing reservation.
        mockExistingReservation = new ReservationDTO(
                1L,
                mockCustomer,
                10,
                LocalDate.of(2025, 11, 11),
                LocalTime.of(18, 30),
                ReservationStatus.RESERVED,
                NotificationMode.EMAIL,
                LocalDateTime.of(2025, 11, 8, 9,31,30)
        );

        // Create a mock reservation object for cancelled reservation.
        mockCancelledReservation = new ReservationDTO(
                1L,
                mockCustomer,
                10,
                LocalDate.of(2025, 11, 11),
                LocalTime.of(18, 30),
                ReservationStatus.CANCELLED,
                NotificationMode.EMAIL,
                LocalDateTime.of(2025, 11, 8, 9,32,35)
        );

        // Create a mock reservation object for updating reservation.
        mockUpdatedReservation = new ReservationDTO(
                1L,
                mockCustomer,
                5,
                LocalDate.of(2025, 11, 11),
                LocalTime.of(17, 30),
                ReservationStatus.RESERVED,
                NotificationMode.SMS,
                LocalDateTime.of(2025, 11, 8, 9,35,10)
        );
    }

    @Test
    public void shouldCreateNewReservation() throws Exception {
        when(reservationService.saveOrUpdate(mockNewReservation)).thenReturn(mockExistingReservation);
        mockMvc.perform(post("/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockNewReservation)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCancelReservation() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.of(mockExistingReservation));
        when(reservationService.saveOrUpdate(any())).thenReturn(mockCancelledReservation);
        mockMvc.perform(post("/reservations/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldUpdateReservation() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.of(mockExistingReservation));
        when(reservationService.saveOrUpdate(mockExistingReservation)).thenReturn(mockUpdatedReservation);
        mockMvc.perform(put("/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockExistingReservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests").value(5))
                .andExpect(jsonPath("$.reservationTime").value("17:30:00"));
    }

    @Test
    public void shouldFindReservationsByCustomerId() throws Exception {
        when(reservationService.findReservationsByCustomerId(101L)).thenReturn(List.of(mockExistingReservation));
        mockMvc.perform(get("/reservations/customer/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerDTO.fullName").value("Juan dela Cruz"));
    }
}

