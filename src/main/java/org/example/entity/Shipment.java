package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.ShipingType;
import org.example.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="shipment")
@NoArgsConstructor
@Getter
@Setter
public class Shipment extends BaseEntity{
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
    private ShipingType type;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status = ShipmentStatus.PENDING;

    public Shipment(ShipingType type) {
        this.type=type;
    }

    public static Shipment createPassangerShipment(Company company, Employee employee, Client client,Vehicle vehicle, String startLocation, String destination,
                                                  LocalDate startDate, LocalDate endDate, BigDecimal cost){
        Qualification requiredQual = vehicle.getQualification();
        if (requiredQual != null) {
            if (!employee.getQualifications().contains(requiredQual)) {
                throw new IllegalArgumentException("Driver " + employee.getName() +
                        " lacks the required qualification: " + requiredQual.getName());
            }
        }

        Shipment s = new Shipment(ShipingType.PEOPLE);
        s.setCompany(company);
        s.setEmployee(employee);
        s.setClient(client);
        s.setVehicle(vehicle);
        s.setStartLocation(startLocation);
        s.setDestination(destination);
        s.setStartDate(startDate);
        s.setEndDate(endDate);
        s.setCost(cost);
        s.weight = null;
        return s;
    }

    public static Shipment createFreightShipment(Company company, Employee employee, Client client,Vehicle vehicle, String startLocation, String destination,
                                                 LocalDate startDate, LocalDate endDate, BigDecimal cost, double weight) {
        Qualification requiredQual = vehicle.getQualification();
        if (requiredQual != null) {
            if (!employee.getQualifications().contains(requiredQual)) {
                throw new IllegalArgumentException("Driver " + employee.getName() +
                        " lacks the required qualification: " + requiredQual.getName());
            }
        }

        Shipment s = new Shipment(ShipingType.CARRIAGE);
        s.setCompany(company);
        s.setEmployee(employee);
        s.setClient(client);
        s.setVehicle(vehicle);
        s.setStartLocation(startLocation);
        s.setDestination(destination);
        s.setStartDate(startDate);
        s.setEndDate(endDate);
        s.setCost(cost);
        s.setWeight(weight);
        return s;
    }

}
