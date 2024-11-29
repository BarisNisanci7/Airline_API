package com.airlineapi.dto;

import com.airlineapi.model.FlightDays;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    private Long id;
    private String from;
    private String to;
    private LocalDate date;
    private int capacity;
    private int availableSeats;
    private Set<FlightDays> daysOfWeek; // Enum kullanılarak güncellendi
}
