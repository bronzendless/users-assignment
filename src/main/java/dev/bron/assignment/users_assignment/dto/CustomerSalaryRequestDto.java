package dev.bron.assignment.users_assignment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CustomerSalaryRequestDto {
    private BigDecimal salary;
}
