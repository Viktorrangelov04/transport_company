package org.example.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "license")
public class License {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToMany(mappedBy = "license")
    private Set<Employee> employees;
}
