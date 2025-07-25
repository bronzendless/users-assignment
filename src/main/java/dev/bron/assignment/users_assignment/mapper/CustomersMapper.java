package dev.bron.assignment.users_assignment.mapper;

import dev.bron.assignment.users_assignment.dto.CustomersDto;
import dev.bron.assignment.users_assignment.dto.CustomersRequestDto;
import dev.bron.assignment.users_assignment.entity.CustomersEntity;
import dev.bron.assignment.users_assignment.entity.GroupsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomersMapper {
    @Mapping(target = "customerGroups", source = "groups")
    CustomersDto toDto(CustomersEntity entity);

    @Mapping(target = "customerGroups", source = "groups")
    List<CustomersDto> toDtos(List<CustomersEntity> entities);

    @Mapping(target = "id", ignore = true)
    CustomersEntity toCreateEntity(CustomersRequestDto dto);

    default Set<String> mapGroupsToNames(Set<GroupsEntity> groups) {
        if (groups == null) return new HashSet<>();
        return groups.stream()
                .map(GroupsEntity::getName)
                .collect(Collectors.toSet());
    }
}
