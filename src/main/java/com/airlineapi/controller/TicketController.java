package com.airlineapi.controller;

import com.airlineapi.dto.TicketDTO;
import com.airlineapi.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @PostMapping("/buy")
    public ResponseEntity<String> buyTicket(@RequestBody TicketDTO ticketDTO) {
        ticketService.buyTicket(ticketDTO);
        return ResponseEntity.ok("Ticket successfully purchased!");
    }

    @PostMapping("/check-in")
    public ResponseEntity<String> checkInTicket(@RequestParam Long ticketId) {
        ticketService.checkIn(ticketId);
        return ResponseEntity.ok("Check-in successful!");
    }
}
