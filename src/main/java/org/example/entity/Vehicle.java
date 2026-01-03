package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicle")
public class Vehicle extends BaseEntity{
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
