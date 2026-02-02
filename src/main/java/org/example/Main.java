package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.*;
import org.example.service.*;
import org.example.utils.InputReader;

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
        Scanner scanner = new Scanner(System.in);
        InputReader inputReader = new InputReader(scanner);
        FileService fileService = new FileService();

        EmployeeService employeeService = new EmployeeService(inputReader);
        VehicleService vehicleService = new VehicleService(inputReader);
        ClientService clientService = new ClientService(inputReader);
        ShippingService shippingService = new ShippingService(inputReader, employeeService, vehicleService, clientService, fileService);
        ReportService reportService = new ReportService(inputReader);

        MenuService menu = new MenuService(inputReader, employeeService, vehicleService, clientService, shippingService, reportService);

        Company activeCompany = null;
        boolean running = true;

        while (running) {
            if (activeCompany == null) {
                activeCompany = menu.startMenu();
            } else {
                activeCompany = menu.showCompanyDashboard(activeCompany);
            }
        }
    }
}