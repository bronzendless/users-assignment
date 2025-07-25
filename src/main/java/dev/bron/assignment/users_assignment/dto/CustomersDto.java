package dev.bron.assignment.users_assignment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CustomersDto {
    private String name;
    private BigDecimal salary;
    private Set<String> customerGroups;
}
