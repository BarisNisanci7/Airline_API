package com.airlineapi.dto;

import com.airlineapi.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id;
    private String passengerName;
    private TicketStatus status; // Enum kullanılarak güncellendi
    private Long flightId; // Biletin bağlı olduğu uçuşun ID'si
}
