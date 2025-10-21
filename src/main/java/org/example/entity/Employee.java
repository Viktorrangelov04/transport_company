package org.example.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="employee")
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name="company_id")
    private long companyId;
    @ManyToOne
    private Company company;
    @ManyToMany(mappedBy="employee")
    private Set<License> license;

    public Employee(){};
    public Employee(long id, String name, Company company) {
        this.id = id;
        this.name = name;
        this.companyId = company.getId();
    }
}
