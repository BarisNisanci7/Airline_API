package com.airlineapi.repository;

import com.airlineapi.model.Ticket;
import com.airlineapi.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByFlightId(Long flightId);

    List<Ticket> findByFlightIdAndStatus(Long flightId, TicketStatus status);
}
