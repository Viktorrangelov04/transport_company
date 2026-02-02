package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.ShipmentDao;
import org.example.entity.*;
import org.example.enums.ShipmentStatus;
import org.example.utils.InputReader;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {

    private static Company testCompany;
    private ReportService reportService;
    private ByteArrayOutputStream outContent;

    @BeforeAll
    static void setupData() {
        testCompany = new Company("Report Test Company");
        CompanyDao.create(testCompany);

        Employee emp = new Employee("Test Driver", new BigDecimal("1500"), testCompany);
        Vehicle veh = new Vehicle("Test Truck", testCompany);
        Client client = new Client("Test Client", testCompany);

        testCompany.addEmployee(emp);
        testCompany.addVehicle(veh);
        testCompany.addClient(client);
        CompanyDao.update(testCompany);

        testCompany = CompanyDao.getById(testCompany.getId());

        Shipment shipment = new Shipment();
        shipment.setCompany(testCompany);
        shipment.setEmployee(testCompany.getEmployees().iterator().next());
        shipment.setVehicle(testCompany.getVehicles().iterator().next());
        shipment.setClient(testCompany.getClients().iterator().next());
        shipment.setStartLocation("Sofia");
        shipment.setDestination("Plovdiv");
        shipment.setStartDate(LocalDate.now());
        shipment.setEndDate(LocalDate.now().plusDays(1));
        shipment.setCost(new BigDecimal("250"));
        shipment.setStatus(ShipmentStatus.PAID);

        ShipmentDao.create(shipment);
    }

    @AfterAll
    static void cleanup() {
        if (testCompany != null) {
            CompanyDao.delete(testCompany.getId());
        }
    }

    @BeforeEach
    void setup() {
        InputReader reader = new InputReader(new Scanner(System.in));
        reportService = new ReportService(reader);

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void resetOutput() {
        System.setOut(System.out);
    }

    @Test
    void testShowTotalShipments() {
        reportService.showTotalShipments(testCompany);

        String output = outContent.toString();
        assertTrue(output.contains("Total shipments: 1"));
    }

    @Test
    void testShowTotalRevenue() {
        reportService.showTotalRevenue(testCompany);

        String output = outContent.toString();
        assertTrue(output.contains("250"));
    }
}