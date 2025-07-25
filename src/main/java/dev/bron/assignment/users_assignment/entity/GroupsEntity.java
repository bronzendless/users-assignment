package dev.bron.assignment.users_assignment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter
@Setter
public class GroupsEntity {
    @Id
    public Integer id;

    @Column(name = "name")
    public String name;

    @ManyToMany(mappedBy = "groups")
    private Set<CustomersEntity> customers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupsEntity groupsEntity = (GroupsEntity) o;
        return Objects.equals(name, groupsEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
