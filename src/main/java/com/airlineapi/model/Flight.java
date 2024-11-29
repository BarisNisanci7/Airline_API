package com.airlineapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Table(name = "flights")
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Departure location cannot be null")
    @Column(name = "departure")
    private String from;

    @NotNull(message = "Destination location cannot be null")
    @Column(name = "destination")
    private String to;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @Column(name = "capacity")
    private int capacity;

    private int availableSeats;

    @Column(name = "available_dates")
    private LocalDate availableDates;

    @ElementCollection
    @CollectionTable(name = "flight_days", joinColumns = @JoinColumn(name = "flight_id"))
    @Enumerated(EnumType.STRING)
    private Set<FlightDays> daysOfWeek;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets;
}
