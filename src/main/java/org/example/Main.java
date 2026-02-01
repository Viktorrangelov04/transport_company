package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.*;
import org.example.service.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.google.protobuf.JavaFeaturesProto.java;

public class Main {
    public static void main(String[] args) throws SQLException {
        //Initializing companies and qualifications and services
        Scanner scanner = new Scanner(System.in);

        MenuService menu = new MenuService(scanner);
        EmployeeService employeeService = new EmployeeService(scanner);
        VehicleService vehicleService = new VehicleService(scanner);
        ClientService clientService = new ClientService(scanner);
        FileService fileService = new FileService();
        ShippingService shippingService = new ShippingService(scanner, employeeService, vehicleService, clientService, fileService);


        List<Qualification> allQualifications = QualificationDao.findAll();
        Map<String, Qualification> qualificationMap = allQualifications.stream()
                .collect(Collectors.toMap(Qualification::getName, q->q));

        Company activeCompany = null;
        boolean running = true;

        while(running){
            if(activeCompany == null){
                System.out.println("Choose an option");
                System.out.println("[1] Select a company");
                System.out.println("[2] Create a new company");
                System.out.println("[0] Exit");
                int startChoice=scanner.nextInt();
                scanner.nextLine();

                switch (startChoice) {
                    case 1:
                        activeCompany = menu.selectCompanyMenu();
                        break;
                    case 2:
                        System.out.println("Company name:");
                        String name = scanner.nextLine();
                        Company company = new Company(name);
                        CompanyDao.create(company);
                        System.out.println("Company created successfully!");
                        activeCompany = company;
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }else{
                System.out.println("Current company: " + activeCompany.getName());
                System.out.println("Choose an option");
                System.out.println("[1] Manage Employees");
                System.out.println("[2] Manage Vehicles");
                System.out.println("[3] Manage Clients");
                System.out.println("[4] Manage Shipments");
                System.out.println("[5] Change company");
                System.out.println("[8] Change company name");
                System.out.println("[9] Delete Company");
                System.out.println("[0] Exit");

                int choice=scanner.nextInt();
                scanner.nextLine();

                switch(choice){
                    case 1: activeCompany=employeeService.manageEmployees(activeCompany); break;
                    case 2: activeCompany=vehicleService.manageVehicles(activeCompany); break;
                    case 3: activeCompany=clientService.manageClients(activeCompany); break;
                    case 4: activeCompany=shippingService.manageShipments(activeCompany); break;
                    case 5: activeCompany=null; break;

                    case 8: activeCompany=menu.changeCompanyName(activeCompany); break;
                    case 9:
                        System.out.println("Are you sure you want to delete? (y/n)");
                        String confirm =scanner.nextLine();
                        if(confirm.equals("y")){
                            CompanyDao.delete(activeCompany.getId());
                            activeCompany=null;
                            System.out.println("Company deleted successfully!");
                        }

                        break;
                    case 0: running = false; break;
                }
            }

        }



//        Employee employee = EmployeeDao.getById(2L);
//        Client client = ClientDao.getById(2L);
//        Vehicle vehicle = VehicleDao.getById(2L);
//
//        Shipment shipment1 = Shipment.createPassangerShipment(company, employee, client, vehicle, "Sofia", "Berlin", LocalDate.of(2026, 1, 1),
//                LocalDate.of(2026, 1, 10), new BigDecimal("1680.00"));
//
//        ShipmentDao.create(shipment1);


        SessionFactoryUtil.getSessionFactory().openSession();
    }
}