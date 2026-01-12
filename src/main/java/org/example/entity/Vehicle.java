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
    private Qualification qualification;

    public Vehicle() {}
    public Vehicle(String name, Company company) {
        this.name = name;
        this.company = company;
    }
}
