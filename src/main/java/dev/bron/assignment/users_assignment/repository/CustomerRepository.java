package dev.bron.assignment.users_assignment.repository;

import dev.bron.assignment.users_assignment.entity.CustomersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomersEntity, UUID> {
    boolean existsByName(String name);
    @Query(value = "SELECT c.* FROM customers c " +
            "JOIN customers_groups cg ON cg.customer_id = c.id " +
            "JOIN groups g ON cg.group_id = g.id " +
            "WHERE g.name IN (:groupNames)", nativeQuery = true)
    List<CustomersEntity> findByGroupNames(@Param("groupNames") List<String> groupNames);
}