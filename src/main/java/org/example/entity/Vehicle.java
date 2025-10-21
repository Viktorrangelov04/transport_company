package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    private Company company;

    public Vehicle() {}
    public Vehicle(Long id, String name, Company company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }
}
