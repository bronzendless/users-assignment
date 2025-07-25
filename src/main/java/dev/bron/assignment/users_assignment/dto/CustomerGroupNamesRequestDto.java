package dev.bron.assignment.users_assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CustomerGroupNamesRequestDto {
    @NotBlank(message = "group name is required")
    @NotNull(message = "group name is not null")
    private String groupName;
}
