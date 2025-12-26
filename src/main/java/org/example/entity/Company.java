package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "shipment_count")
    private int shipmentCount;

    @OneToMany(mappedBy = "company")
    private Set<Employee> employees;

    @OneToMany(mappedBy = "company")
    private Set<Client> clients;

    public Company(){};
    public Company(long id, String name, int shippmentCount) {
        this.id = id;
        this.name = name;
        this.shipmentCount = shippmentCount;
    }
}
