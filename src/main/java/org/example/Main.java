package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.*;
import org.example.service.MenuService;

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
        //Initializing companies and qualifications
        Scanner scanner = new Scanner(System.in);
        MenuService menu = new MenuService(scanner);

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
                int startChoise=scanner.nextInt();
                scanner.nextLine();

                if(startChoise==1){
                    activeCompany=menu.selectCompanyMenu();
                }else{
                    running =false;
                }
            }else{
                System.out.println("Choose an option");
                System.out.println("[1] View employees");
                System.out.println("[2] View Vehicles");
                System.out.println("[3] View Clients");
                System.out.println("[4] Register Shipment");
                System.out.println("[5] Change company");
                System.out.println("[0] Exit");

                int choice=scanner.nextInt();
                scanner.nextLine();

                switch(choice){
                    case 1: menu.showEmployees(activeCompany); break;
                    case 2: menu.showVehicles(activeCompany); break;
                    case 3: menu.showClients(activeCompany); break;
                    case 5: activeCompany=null; break;
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