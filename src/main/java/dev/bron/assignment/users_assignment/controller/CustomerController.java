package dev.bron.assignment.users_assignment.controller;

import dev.bron.assignment.users_assignment.dto.CustomerGroupNamesRequestDto;
import dev.bron.assignment.users_assignment.dto.CustomerSalaryRequestDto;
import dev.bron.assignment.users_assignment.dto.CustomersDto;
import dev.bron.assignment.users_assignment.dto.CustomersRequestDto;
import dev.bron.assignment.users_assignment.service.CustomersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomersService customersService;

    @PostMapping("/customers")
    public ResponseEntity<CustomersDto> createCustomer(
            @RequestBody @Valid CustomersRequestDto request
    ) {
        log.info("===== Start create customer by request : {} =====", request);
        final CustomersDto customersDto = customersService.createCustomer(request);
        log.info("===== End create customer =====");
        return ResponseEntity.ok(customersDto);
    }

    @PatchMapping("/customers/{customerId}")
    public ResponseEntity<CustomersDto> updateCustomerById(
            @PathVariable("customerId") UUID customerId,
            @RequestBody @Valid CustomerSalaryRequestDto request
    ) {
        log.info("===== Start patch salary by customerId : {} =====", customerId);
        final CustomersDto customersDto = customersService.updateCustomer(customerId, request);
        log.info("===== End patch salary =====");
        return ResponseEntity.ok(customersDto);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomersDto> getCustomerById(
            @PathVariable("customerId") UUID customerId
    ) {
        log.info("===== Start get customer by customerId : {} =====", customerId);
        final CustomersDto customer = customersService.getCustomerById(customerId);
        log.info("===== End get customer by customerId : {} =====", customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/customers/groups")
    public ResponseEntity<List<CustomersDto>> getCustomerByGroupName(
            @ModelAttribute @Valid CustomerGroupNamesRequestDto request
    ) {
        log.info("===== Start get customer by groupName : {} =====", request.getGroupName());
        final List<CustomersDto> customers = customersService.getCustomerByGroupName(request);
        log.info("===== End get customer by groupName =====");
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomersDto>> getCustomers() {
        log.info("===== Start get all customer =====");
        final List<CustomersDto> customers = customersService.getCustomers();
        log.info("===== End get all customer =====");
        return ResponseEntity.ok(customers);
    }
}
