package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.ShippingType;
import org.example.enums.ShipmentStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="shipment")
@NoArgsConstructor
@Getter
@Setter
public class Shipment extends BaseEntity implements Serializable {
    @ManyToOne
    private Company company;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Vehicle vehicle;

    @Column(name="start_location")
    private String startLocation;
    private String destination;
    @Column(name="start_date")
    private LocalDate startDate;
    @Column(name="end_date")
    private LocalDate endDate;
    private Double weight;
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private ShippingType type;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status = ShipmentStatus.PENDING;

}
