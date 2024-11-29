package com.airlineapi.controller;

import com.airlineapi.dto.FlightDTO;
import com.airlineapi.model.Flight;
import com.airlineapi.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;
    private final ModelMapper modelMapper;

    @PostMapping("/insert")
    public ResponseEntity<String> insertFlight(@RequestBody FlightDTO flightDTO) {
        Flight flight = modelMapper.map(flightDTO, Flight.class);
        flightService.insertFlight(flight);
        return ResponseEntity.ok("Flight successfully added!");
    }

    @GetMapping("/query")
    public ResponseEntity<List<FlightDTO>> queryFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String date) {
        LocalDate flightDate = LocalDate.parse(date);
        List<Flight> flights = flightService.queryFlights(from, to, flightDate);
        List<FlightDTO> flightDTOs = flights.stream()
                .map(flight -> modelMapper.map(flight, FlightDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(flightDTOs);
    }


    @GetMapping("/report")
    public ResponseEntity<List<FlightDTO>> reportFlights(
            @RequestParam String from,
            @RequestParam String to) {
        List<Flight> flights = flightService.reportFlights(from, to);
        List<FlightDTO> flightDTOs = flights.stream()
                .map(flight -> modelMapper.map(flight, FlightDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(flightDTOs);
    }
}
