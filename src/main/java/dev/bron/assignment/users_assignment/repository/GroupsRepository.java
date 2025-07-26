package dev.bron.assignment.users_assignment.repository;

import dev.bron.assignment.users_assignment.entity.GroupsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GroupsRepository extends CrudRepository<GroupsEntity, UUID> {
    Set<GroupsEntity> findByNameIn(List<String> names);
    Optional<GroupsEntity> findByName(String name);
}
