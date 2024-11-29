package com.airlineapi.repository;

import com.airlineapi.model.Ticket;
import com.airlineapi.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Belirli bir uçuşa ait tüm biletleri getir
    List<Ticket> findByFlightId(Long flightId);

    // Belirli bir uçuşa ait belirli durumdaki biletleri getir
    List<Ticket> findByFlightIdAndStatus(Long flightId, TicketStatus status);
}
