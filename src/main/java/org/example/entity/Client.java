package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="client")
public class Client extends BaseEntity{
    private String name;
    @Column(name = "owed_money", nullable = false)
    private BigDecimal owedMoney = BigDecimal.ZERO;

    @ManyToOne
    private Company company;



    public void payAll(){
        owedMoney = BigDecimal.ZERO;
    }

    public void paySome(BigDecimal paidAmount){
        owedMoney = owedMoney.subtract(paidAmount);
    }

    public Client(String name, Company company){
        this.name = name;
        this.company = company;
    }

}
