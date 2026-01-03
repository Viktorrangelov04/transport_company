package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.LicenseType;

import java.util.Set;

@Entity
@Table(name="employee")
public class Employee extends BaseEntity{
    @Column(name = "name")
    private String name;

    @ElementCollection(targetClass = LicenseType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "driver_licenses")
    private Set<LicenseType> categories;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public Employee(){};
    public Employee(long id, String name, Company company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }
}
