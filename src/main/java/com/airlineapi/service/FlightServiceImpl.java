package com.airlineapi.service;

import com.airlineapi.model.Flight;
import com.airlineapi.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public void insertFlight(Flight flight) {
        flightRepository.save(flight);
    }

    @Override
    public List<Flight> queryFlights(String from, String to, LocalDate date) {
        return flightRepository.findByFromAndToAndAvailableDates(from, to, date);
    }

    @Override
    public List<Flight> reportFlights(String from, String to) {
        return flightRepository.findByFromAndTo(from, to);
    }
}
