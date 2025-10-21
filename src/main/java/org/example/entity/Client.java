package org.example.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="client")
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "owed_money")
    private BigDecimal owedMoney;

    @ManyToOne
    private Company company;

}
