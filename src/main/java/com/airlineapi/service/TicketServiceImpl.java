package com.airlineapi.service;

import com.airlineapi.dto.TicketDTO;
import com.airlineapi.model.Ticket;
import com.airlineapi.model.TicketStatus;
import com.airlineapi.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    @Override
    public TicketDTO buyTicket(TicketDTO ticketDTO) {
        Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);
        Ticket savedTicket = ticketRepository.save(ticket);
        return modelMapper.map(savedTicket, TicketDTO.class);
    }

    @Override
    public boolean checkIn(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(ticket -> {
                    ticket.setStatus(TicketStatus.BOOKED);
                    ticketRepository.save(ticket);
                    return true;
                })
                .orElse(false);
    }
}
