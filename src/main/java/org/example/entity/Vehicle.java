package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "vehicle")
public class Vehicle extends BaseEntity{
    private String name;

    @ManyToOne
    private Company company;

    @ManyToOne
    @JoinColumn(name = "qualification_id")
    private Qualification requiredQualification;

    public Vehicle() {}
    public Vehicle(Long id, String name, Company company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }
}
