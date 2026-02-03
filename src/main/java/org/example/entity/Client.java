package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="client")
public class Client extends BaseEntity implements Serializable {
    private String name;

    @ManyToOne
    private Company company;

    public Client(String name, Company company){
        this.name = name;
        this.company = company;
    }

}
