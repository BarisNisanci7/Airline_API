package com.airlineapi.service;

import com.airlineapi.model.Flight;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    void insertFlight(Flight flight);
    List<Flight> queryFlights(String from, String to, LocalDate date);
    List<Flight> reportFlights(String from, String to);
}
