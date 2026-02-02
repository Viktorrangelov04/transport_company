package org.example.service;

import org.example.dao.ClientDao;
import org.example.dao.EmployeeDao;
import org.example.dao.ShipmentDao;
import org.example.dao.VehicleDao;
import org.example.entity.*;
import org.example.enums.ShipingType;
import org.example.enums.ShipmentStatus;
import org.example.utils.InputReader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ShippingService {
    private final InputReader inputReader;
    private final EmployeeService employeeService;
    private final VehicleService vehicleService;
    private final ClientService clientService;
    private final FileService fileService;

    public ShippingService(InputReader inputReader, EmployeeService es, VehicleService vs, ClientService cs, FileService fs) {
        this.inputReader = inputReader;
        this.employeeService = es;
        this.vehicleService = vs;
        this.clientService = cs;
        this.fileService = fs;
    }

    public Company manageShipments(Company company) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Shipment Management ---");
            System.out.println("[1] Create Passenger Shipment");
            System.out.println("[2] Create Carriage Shipment");
            System.out.println("[3] Pay for shipment");
            System.out.println("[4] Export shipment data to file");
            System.out.println("[5] Read shipment data from file");
            System.out.println("[0] Back To Company Menu");

            int choice = (int) inputReader.readLong("Choice: ");
            switch (choice) {
                case 1: createPassengerShipping(company); break;
                case 2: createFreightShipping(company); break;
                case 3: payForShipment(company); break;
                case 4: exportCompanyData(company); break;
                case 5: fileService.importAndShowShipments("company_" + company.getId() + "_backup.dat"); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option."); break;
            }
        }
        return company;
    }

    public void createPassengerShipping(Company company) {
        createShipment(company, false);
    }

    public void createFreightShipping(Company company) {
        createShipment(company, true);
    }

    private void createShipment(Company company, boolean isFreight) {
        System.out.println("\nCreating " + (isFreight ? "Freight" : "Passenger") + " Shipment");

        vehicleService.listVehicles(company);
        Vehicle vehicle = null;
        while (vehicle == null) {
            long vId = inputReader.readLong("Select Vehicle ID: ");
            vehicle = VehicleDao.getById(vId);
            if (vehicle == null) System.out.println("Vehicle not found.");
        }

        Qualification requiredQual = vehicle.getQualification();

        Employee employee = null;
        while (employee == null) {
            employeeService.listEmployees(company);
            long eId = inputReader.readLong("Select driver ID: ");
            employee = EmployeeDao.getById(eId);

            if (employee == null) {
                System.out.println("Employee not found.");
                continue;
            }

            if (requiredQual != null && !employee.getQualifications().contains(requiredQual)) {
                System.out.println("Error: " + employee.getName() + " lacks '" + requiredQual.getName() + "'.");
                employee = null;
                System.out.println("Please select a different driver.");
            }
        }

        clientService.listClients(company);
        Client client = null;
        while (client == null) {
            long clientId = inputReader.readLong("Enter Client ID: ");
            client = ClientDao.getById(clientId);
            if (client == null) System.out.println("Client not found.");
        }

        String startLocation = inputReader.readString("Enter starting location: ");
        String destination = inputReader.readString("Enter ending location: ");
        LocalDate startDate = inputReader.readDate("Enter start date (YYYY-MM-DD): ");
        LocalDate endDate = inputReader.readDate("Enter end date (YYYY-MM-DD): ");
        BigDecimal cost = inputReader.readBigDecimal("Enter cost: ");

        double weight = 0.0;
        if (isFreight) {
            weight = inputReader.readDouble("Enter cargo weight: ");
        }

        Shipment shipment = new Shipment();
        shipment.setCompany(company);
        shipment.setEmployee(employee);
        shipment.setClient(client);
        shipment.setVehicle(vehicle);
        shipment.setStartLocation(startLocation);
        shipment.setDestination(destination);
        shipment.setStartDate(startDate);
        shipment.setEndDate(endDate);
        shipment.setCost(cost);
        shipment.setStatus(ShipmentStatus.PENDING);

        if (isFreight) {
            shipment.setType(ShipingType.CARRIAGE);
            shipment.setWeight(weight);
        } else {
            shipment.setType(ShipingType.PEOPLE);
        }

        ShipmentDao.create(shipment);
        System.out.println("Shipment successfully created and saved!");
    }

    public void payForShipment(Company company) {
        clientService.listClients(company);
        long id = inputReader.readLong("Enter client ID: ");
        Client client = ClientDao.getById(id);

        if (client == null) {
            System.out.println("Client not found.");
            return;
        }

        List<Shipment> unpaidShipments = ShipmentDao.getUnpaidByClient(client.getId());

        if (unpaidShipments.isEmpty()) {
            System.out.println("No pending payments for " + client.getName());
            return;
        }

        System.out.println("\nUnpaid Shipments for " + client.getName());
        for (Shipment s : unpaidShipments) {
            System.out.println("ID: " + s.getId() + " | Dest: " + s.getDestination() + " | Cost: $" + s.getCost());
        }

        long shipmentId = inputReader.readLong("Enter Shipment ID to be paid (or 0 to cancel): ");
        if (shipmentId == 0) return;

        Shipment selected = unpaidShipments.stream()
                .filter(s -> s.getId().equals(shipmentId))
                .findFirst()
                .orElse(null);

        if (selected != null) {
            selected.setStatus(ShipmentStatus.PAID);
            ShipmentDao.update(selected);
            System.out.println("Shipment #" + shipmentId + " has been marked as PAID.");
        } else {
            System.out.println("Invalid ID. Payment cancelled.");
        }
    }

    public void exportCompanyData(Company activeCompany) {
        List<Shipment> shipments = ShipmentDao.findAllByCompany(activeCompany.getId());
        if (shipments.isEmpty()) {
            System.out.println("No shipments found to export.");
            return;
        }
        fileService.exportShipments(shipments, "company_" + activeCompany.getId() + "_backup.dat");
    }
}
