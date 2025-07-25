package dev.bron.assignment.users_assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomersRequestDto {
    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is not null")
    private String name;

    @NotNull(message = "Salary is not null")
    private BigDecimal salary;
}
