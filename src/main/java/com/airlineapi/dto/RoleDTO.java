package com.airlineapi.dto;

import com.airlineapi.model.AppRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Long id;
    private AppRole name; // Enum kullanılarak güncellendi
}
