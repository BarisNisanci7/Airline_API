package com.airlineapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tickets")
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Passenger name cannot be null")
    @Size(min = 3, max = 50, message = "Passenger name must be between 3 and 50 characters")
    private String passengerName;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Ticket status cannot be null")
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
}
