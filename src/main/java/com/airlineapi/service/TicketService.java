package com.airlineapi.service;

import com.airlineapi.dto.TicketDTO;

public interface TicketService {
    TicketDTO buyTicket(TicketDTO ticketDTO);
    boolean checkIn(Long ticketId);
}
