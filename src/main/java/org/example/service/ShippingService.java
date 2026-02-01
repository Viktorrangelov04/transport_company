package org.example.service;

import org.example.dao.ClientDao;
import org.example.dao.EmployeeDao;
import org.example.dao.ShipmentDao;
import org.example.dao.VehicleDao;
import org.example.entity.*;
import org.example.enums.ShipingType;
import org.example.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ShippingService {
    private final Scanner scanner;
    private final EmployeeService employeeService;
    private final VehicleService vehicleService;
    private final ClientService clientService;
    private final FileService fileService;
    boolean back = false;

    public ShippingService(Scanner scanner, EmployeeService es, VehicleService vs, ClientService cs, FileService fs) {
        this.scanner = scanner;
        this.employeeService = es;
        this.vehicleService = vs;
        this.clientService = cs;
        this.fileService = fs;
    }

    public Company manageShipments(Company company){
        while(!back){
            System.out.println("Creating Shipment");
            System.out.println("[1] Create Passenger Shipment");
            System.out.println("[2] Create Carriage Shipment");
            System.out.println("[3] Pay for shipment");

            System.out.println("[6] Export shipment data to file");
            System.out.println("[7] Read shipment data from file");
            System.out.println("[0] Back To Company Menu");

            int choice  = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createPassengerShipping(company);
                    break;
                case 2:
                    createFreightShipping(company);
                    break;
                case 3:
                    payForShipment(company);
                    break;
                case 6:
                    exportCompanyData(company);
                    break;
                case 7:
                    fileService.importAndShowShipments("company_" + company.getId() + "_backup.dat");
                    break;
                case 0:
                    back = true;
                    break;
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

    public void createShipment(Company company, boolean isFreight){
        System.out.println("Creating " + (isFreight ? "Freight" : "Passenger") + " Shipment");

        vehicleService.listVehicles(company);
        Vehicle vehicle = null;
        while (vehicle == null) {
            long vId = readLong("Select Vehicle ID: ");
            vehicle = VehicleDao.getById(vId);
            if (vehicle == null) System.out.println("Vehicle not found.");
        }

        Qualification requiredQual = vehicle.getQualification();

        Employee employee = null;
        while (employee == null) {
            employeeService.listEmployees(company);
            long eId = readLong("Select Driver ID: ");
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
        long clientId = readLong("Enter Client ID: ");

        Client client = ClientDao.getById(clientId);


        String startLocation = readString("Enter starting location: ");
        String destination = readString("Enter ending location: ");

        LocalDate startDate = readDate("Enter start date (YYYY-MM-DD): ");
        LocalDate endDate = readDate("Enter end date (YYYY-MM-DD): ");

        BigDecimal cost = readBigDecimal("Enter cost: ");

        double weight = 0.0;
        if (isFreight) {
            weight = readDouble("Enter cargo weight: ");
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

        if (isFreight) {
            shipment.setType(ShipingType.CARRIAGE);
            shipment.setWeight(weight);
        } else {
            shipment.setType(ShipingType.PEOPLE);
        }
        ShipmentDao.create(shipment);

        System.out.println("All data validated. Saving shipment...");
    }

    public void payForShipment(Company company) {
        clientService.listClients(company);
        System.out.println("Enter client id");

        while (!scanner.hasNextLong()) {
            scanner.next();
        }
        Long id = scanner.nextLong();
        scanner.nextLine();


        Client client = ClientDao.getById(id);

        List<Shipment> unpaidShipments = ShipmentDao.getUnpaidByClient(client.getId());

        if (unpaidShipments.isEmpty()) {
            System.out.println("No pending payments for " + client.getName());
            return;
        }

        System.out.println("Unpaid Shipments for " + client.getName() + "");
        for (Shipment s : unpaidShipments) {
            System.out.println("ID: " + s.getId() + " | Dest: " + s.getDestination() + " | Cost: $" + s.getCost());
        }

        long shipmentId = readLong("Enter Shipment ID to be paid (or 0 to cancel): ");

        if (shipmentId == 0) return;

        Shipment selected = unpaidShipments.stream()
                .filter(s -> s.getId().equals(shipmentId))
                .findFirst()
                .orElse(null);

        if (selected != null) {
            selected.setStatus(ShipmentStatus.PAID);
            ShipmentDao.update(selected);
            System.out.println("Shipment #" + shipmentId + " has been paid.");
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

    private long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid numeric ID.");
            }
        }
    }

    private String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error: Input cannot be empty.");
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid format. Use YYYY-MM-DD.");
            }
        }
    }

    private BigDecimal readBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().replace(",", ".");
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid amount (e.g. 500.00).");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().replace(",", ".");
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid decimal number (e.g. 10.5).");
            }
        }
    }
}
