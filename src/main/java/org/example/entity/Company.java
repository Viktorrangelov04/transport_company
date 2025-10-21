package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name="company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int shippment_count;

    public Company(){};
    public Company(int id, String name, int shippment_count) {
        this.id = id;
        this.name = name;
        this.shippment_count = shippment_count;
    }

    public Integer getId() {
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
    public int getShippment_count() {
        return shippment_count;
    }
    public void setShippment_count(int shippment_count) {
        this.shippment_count = shippment_count;
    }

}
