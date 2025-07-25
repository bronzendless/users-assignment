package dev.bron.assignment.users_assignment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class CustomersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "salary")
    private BigDecimal salary;

    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(
        name = "customers_groups",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<GroupsEntity> groups = new HashSet<>();

//    public void addGroup(GroupsEntity group) {
//        groups.add(group);
//        group.getCustomers().add(this);
//    }
//
//    public void removeGroup(GroupsEntity group) {
//        groups.remove(group);
//        group.getCustomers().remove(this);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomersEntity)) return false;
        return id != null && id.equals(((CustomersEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
