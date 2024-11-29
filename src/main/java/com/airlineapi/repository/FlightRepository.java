package com.airlineapi.repository;

import com.airlineapi.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByFromAndToAndAvailableDates(String from, String to, LocalDate availableDates);
    List<Flight> findByFromAndTo(String from, String to);
}
