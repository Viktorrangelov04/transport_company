package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="employee")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Employee extends BaseEntity implements Serializable {
    private String name;
    private BigDecimal salary;

    @ManyToMany
    @JoinTable(
            name = "employee_qualifications",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "qualification_id")
    )
    private Set<Qualification> qualifications = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public Employee(String name, BigDecimal salary, Company company) {
        this.name = name;
        this.salary = salary;
        this.company = company;
    }

    public void addQualification(Qualification qualification) {
        qualifications.add(qualification);
    }
}
