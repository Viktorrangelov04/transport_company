package org.example.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Column(name = "shippment_count")
    private int shippmentCount;

    @OneToMany(mappedBy = "company")
    private Set<Employee> employees;

    @OneToMany(mappedBy = "company")
    private Set<Client> clients;

    public Company(){};
    public Company(long id, String name, int shippmentCount) {
        this.id = id;
        this.name = name;
        this.shippmentCount = shippmentCount;
    }

    public long getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getShippmentCount() {
        return shippmentCount;
    }
    public void setShippmentCount(int shippmentCount) {
        this.shippmentCount = shippmentCount;
    }

}
