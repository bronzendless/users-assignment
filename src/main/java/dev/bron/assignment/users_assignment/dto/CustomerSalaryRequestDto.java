package dev.bron.assignment.users_assignment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerSalaryRequestDto {
    @NotNull(message = "Salary is not null")
    private BigDecimal salary;
}