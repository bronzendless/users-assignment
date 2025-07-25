package dev.bron.assignment.users_assignment.service;

import dev.bron.assignment.users_assignment.constant.CustomerGroups;
import dev.bron.assignment.users_assignment.constant.SalaryThresholds;
import dev.bron.assignment.users_assignment.dto.CustomerGroupNamesRequestDto;
import dev.bron.assignment.users_assignment.dto.CustomerSalaryRequestDto;
import dev.bron.assignment.users_assignment.dto.CustomersDto;
import dev.bron.assignment.users_assignment.dto.CustomersRequestDto;
import dev.bron.assignment.users_assignment.entity.CustomersEntity;
import dev.bron.assignment.users_assignment.entity.GroupsEntity;
import dev.bron.assignment.users_assignment.exception.CustomerAlreadyExistsException;
import dev.bron.assignment.users_assignment.exception.CustomerNotFoundException;
import dev.bron.assignment.users_assignment.exception.GroupNotFoundException;
import dev.bron.assignment.users_assignment.mapper.CustomersMapper;
import dev.bron.assignment.users_assignment.repository.CustomerRepository;
import dev.bron.assignment.users_assignment.repository.GroupsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomersService {
    private final CustomerRepository customerRepository;
    private final GroupsRepository groupsRepository;
    private final CustomersMapper customerMapper;

    @Transactional
    public CustomersDto createCustomer(CustomersRequestDto request) {
        if (customerRepository.existsByName(request.getName())) {
            throw new CustomerAlreadyExistsException("name");
        }

        CustomerGroups group = getGruopBySalary(request.getSalary());
        Set<GroupsEntity> groupsEntities = groupsRepository.findByNameIn(List.of(CustomerGroups.CUSTOMER.toString(), group.toString()));

        if (groupsEntities.size() < 2) {
            throw new GroupNotFoundException("group");
        }

        CustomersEntity customersEntity = customerMapper.toCreateEntity(request);
        customersEntity.setGroups(groupsEntities);
        return customerMapper.toDto(customerRepository.save(customersEntity));
    }

    @Transactional
    public CustomersDto updateCustomer(UUID customerId, CustomerSalaryRequestDto request) {
        CustomersEntity customersEntity = getCustomerDataById(customerId);
        Set<GroupsEntity> groupsEntity = customersEntity.getGroups();
        CustomerGroups customerGroups = getGruopBySalary(request.getSalary());

        boolean isExistsGroup = groupsEntity.stream().anyMatch(item -> item.getName().equals(customerGroups.toString()));

        if (!isExistsGroup) {
            Optional<GroupsEntity> groupEntity = groupsRepository.findByName(customerGroups.toString());
            if (groupEntity.isEmpty()) {
                throw new GroupNotFoundException("group");
            }
            groupsEntity.removeIf(item -> !item.getName().equals(CustomerGroups.CUSTOMER.toString()));
            groupsEntity.add(groupEntity.get());
        }

        customersEntity.setSalary(request.getSalary());

        return customerMapper.toDto(customerRepository.save(customersEntity));
    }

    @Transactional(readOnly = true)
    public List<CustomersDto> getCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomersDto getCustomerById(UUID customerId) {
        CustomersEntity customersEntity = getCustomerDataById(customerId);
        return customerMapper.toDto(customersEntity);
    }

    @Transactional(readOnly = true)
    public List<CustomersDto> getCustomerByGroupNames(CustomerGroupNamesRequestDto request) {
        List<CustomersEntity> customersEntities = customerRepository.findByGroupNames(request.getGroupNames());
        return customersEntities.stream()
                .map(customerMapper::toDto)
                .toList();
    }

    private CustomersEntity getCustomerDataById(UUID customerId) {
        Optional<CustomersEntity> customersEntityOpt = customerRepository.findById(customerId);
        if (customersEntityOpt.isEmpty()) {
            throw new CustomerNotFoundException("id");
        }
        return customersEntityOpt.get();
    }

    private CustomerGroups getGruopBySalary(BigDecimal salary) {
        if (salary.compareTo(SalaryThresholds.SALARY_100K) > 0) {
            return CustomerGroups.PLATINUM;
        } else if (salary.compareTo(SalaryThresholds.SALARY_100K) <= 0 && salary.compareTo(SalaryThresholds.SALARY_50K) >= 0) {
            return CustomerGroups.GOLD;
        } else if (salary.compareTo(SalaryThresholds.SALARY_50K) < 0 && salary.compareTo(SalaryThresholds.SALARY_30K) >= 0) {
            return CustomerGroups.SILVER;
        } else {
            throw new GroupNotFoundException("group");
        }
    }
}