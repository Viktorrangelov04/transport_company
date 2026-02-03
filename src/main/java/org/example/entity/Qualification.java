package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "qualification")
public class Qualification extends BaseEntity implements Serializable {
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "qualification")
    private Set<Vehicle> vehicles;
}