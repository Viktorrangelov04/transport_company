package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.ShipingType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="shipment")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String startLocation;
    private String endLocation;
    private LocalDate startDate;
    private LocalDate endDate;
    private ShipingType shippingType;
    private BigDecimal weight;
    private BigDecimal cost;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Employee employee;

    public Shipment() {};
    public Shipment(Long id) {
        this.id = id;
    }

}
