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

        Set<GroupsEntity> groupsEntities = getGruopBySalary(request.getSalary());

        if (groupsEntities.size() < 2) {
            throw new GroupNotFoundException("group");
        }

        CustomersEntity customersEntity = customerMapper.toCreateEntity(request);
        customersEntity.setGroups(groupsEntities);

        CustomersEntity customersSaved = customerRepository.save(customersEntity);
        return customerMapper.toDto(customersSaved);
    }

    @Transactional
    public CustomersDto updateCustomer(UUID customerId, CustomerSalaryRequestDto request) {
        CustomersEntity customersEntity = customerRepository.getReferenceById(customerId);

//        updateCustomerGroups(customersEntity, request.getSalary());
        replaceCustomerGroups(customersEntity, request.getSalary());

        customersEntity.setSalary(request.getSalary());
        CustomersEntity savedCustomer = customerRepository.save(customersEntity);
        return customerMapper.toDto(savedCustomer);
    }

    @Transactional(readOnly = true)
    public List<CustomersDto> getCustomers() {
        List<CustomersEntity> customers = customerRepository.findAll();
        return customerMapper.toDtos(customers);
    }

    @Transactional(readOnly = true)
    public CustomersDto getCustomerById(UUID customerId) {
        Optional<CustomersEntity> customersEntityOpt = customerRepository.findById(customerId);
        CustomersEntity customersEntity = customersEntityOpt
                .orElseThrow(() -> new CustomerNotFoundException("group"));
        return customerMapper.toDto(customersEntity);
    }

    @Transactional(readOnly = true)
    public List<CustomersDto> getCustomerByGroupName(CustomerGroupNamesRequestDto request) {
        Optional<GroupsEntity> groupsEntityOtp = groupsRepository.findByName(request.getGroupName());
        GroupsEntity groupsEntity = groupsEntityOtp
                .orElseThrow(() -> new GroupNotFoundException("group"));

        List<CustomersEntity> customersEntity = groupsEntity.getCustomers().stream()
                .toList();
        return customerMapper.toDtos(customersEntity);
//        List<CustomersEntity> customersEntities = customerRepository.findByGroupNames(request.getGroupNames());
//        return customersEntities.stream()
//                .map(customerMapper::toDto)
//                .toList();
    }

    // NOTE - use this logic when Set/List have a big size
    private void updateCustomerGroups(
            CustomersEntity customersEntity,
            BigDecimal salary
    ) {
        Set<GroupsEntity> groupsEntity = customersEntity.getGroups();
        CustomerGroups customerGroups = getCustomerGroupsBySalary(salary);

        boolean isExistsGroup = groupsEntity.stream()
                .anyMatch(item -> item.getName().equals(customerGroups.toString()));

        if (!isExistsGroup) {
            Optional<GroupsEntity> groupEntity = groupsRepository.findByName(customerGroups.toString());
            if (groupEntity.isEmpty()) {
                throw new GroupNotFoundException("group");
            }
            groupsEntity.removeIf(item -> !item.getName().equals(CustomerGroups.CUSTOMER.toString()));
            groupsEntity.add(groupEntity.get());
        }

        customersEntity.setGroups(groupsEntity);
    }

    // NOTE - use this logic when Set/List have a small size
    private void replaceCustomerGroups(
            CustomersEntity customersEntity,
            BigDecimal salary
    ) {
        Set<GroupsEntity> groupsEntities = getGruopBySalary(salary);
        customersEntity.setGroups(groupsEntities);
    }

    private Set<GroupsEntity> getGruopBySalary(BigDecimal salary) {
        CustomerGroups group = getCustomerGroupsBySalary(salary);
        List<String> groupNames = List.of(CustomerGroups.CUSTOMER.toString(), group.toString());
        return groupsRepository.findByNameIn(groupNames);
    }

    private CustomerGroups getCustomerGroupsBySalary(BigDecimal salary) {
        if (salary.compareTo(SalaryThresholds.SALARY_100K) > 0) {
            return CustomerGroups.PLATINUM;
        } else if (salary.compareTo(SalaryThresholds.SALARY_100K) <= 0 &&
                salary.compareTo(SalaryThresholds.SALARY_50K) >= 0) {
            return CustomerGroups.GOLD;
        } else if (salary.compareTo(SalaryThresholds.SALARY_50K) < 0 &&
                salary.compareTo(SalaryThresholds.SALARY_30K) >= 0) {
            return CustomerGroups.SILVER;
        } else {
            throw new GroupNotFoundException("group");
        }
    }
}