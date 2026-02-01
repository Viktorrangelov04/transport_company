package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="company")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Company extends BaseEntity implements Serializable {
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Employee> employees = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    private Set<Client> clients = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Vehicle> vehicles = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    private Set<Shipment> shipments = new HashSet<>();


    public Company( String name) {
        this.name = name;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setCompany(this);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setCompany(this);
    }

    public void addClient(Client client) {
        clients.add(client);
        client.setCompany(this);
    }
}
