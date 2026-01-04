package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@Table(name="client")
public class Client extends BaseEntity{
    private String name;
    @Column(name = "owed_money")
    private BigDecimal owedMoney;

    @ManyToOne
    private Company company;

}
