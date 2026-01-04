package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name="company")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Company extends BaseEntity{
    private String name;

    @Column(name = "shipment_count")
    private int shipmentCount;

    @ToString.Exclude
    @OneToMany(mappedBy = "company")
    private Set<Employee> employees;

    @ToString.Exclude
    @OneToMany(mappedBy = "company")
    private Set<Client> clients;

    public Company( String name) {
        this.name = name;
    }
}
