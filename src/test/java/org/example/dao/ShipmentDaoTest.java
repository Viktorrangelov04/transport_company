package org.example.dao;

import org.example.entity.*;
import org.example.enums.ShipmentStatus;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShipmentDaoTest {

    private static Company testCompany;
    private static Employee testEmployee;
    private static Vehicle testVehicle;
    private static Client testClient;
    private static Shipment testShipment;

    @BeforeAll
    static void setup() {
        testCompany = new Company("Shipment Test Company");
        CompanyDao.create(testCompany);

        testEmployee = new Employee("Driver", new BigDecimal("1500"), testCompany);
        testVehicle = new Vehicle("Truck", testCompany);
        testClient = new Client("Client", testCompany);

        testCompany.addEmployee(testEmployee);
        testCompany.addVehicle(testVehicle);
        testCompany.addClient(testClient);
        CompanyDao.update(testCompany);

        testCompany = CompanyDao.getById(testCompany.getId());
        testEmployee = testCompany.getEmployees().iterator().next();
        testVehicle = testCompany.getVehicles().iterator().next();
        testClient = testCompany.getClients().iterator().next();

        testShipment = new Shipment();
        testShipment.setCompany(testCompany);
        testShipment.setEmployee(testEmployee);
        testShipment.setVehicle(testVehicle);
        testShipment.setClient(testClient);
        testShipment.setStartLocation("Sofia");
        testShipment.setDestination("Varna");
        testShipment.setStartDate(LocalDate.now());
        testShipment.setEndDate(LocalDate.now().plusDays(1));
        testShipment.setCost(new BigDecimal("500"));
        testShipment.setStatus(ShipmentStatus.PENDING);

        ShipmentDao.create(testShipment);
    }

    @AfterAll
    static void cleanup() {
        if (testCompany != null && testCompany.getId() != null) {
            CompanyDao.delete(testCompany.getId());
        }
    }

    @Test
    @Order(1)
    void testCreate() {
        assertNotNull(testShipment.getId());
    }

    @Test
    @Order(2)
    void testFindAllByCompany() {
        List<Shipment> shipments = ShipmentDao.findAllByCompany(testCompany.getId());
        assertFalse(shipments.isEmpty());
        assertEquals("Varna", shipments.get(0).getDestination());
    }

    @Test
    @Order(3)
    void testGetTotalShipmentCount() {
        Long count = ShipmentDao.getTotalShipmentCount(testCompany.getId());
        assertEquals(1L, count);
    }

    @Test
    @Order(4)
    void testGetTotalRevenue() {
        BigDecimal revenue = ShipmentDao.getTotalRevenue(testCompany.getId());
        assertEquals(0, new BigDecimal("500").compareTo(revenue));
    }

    @Test
    @Order(5)
    void testGetRevenueForPeriod() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(2);

        BigDecimal revenue = ShipmentDao.getRevenueForPeriod(testCompany.getId(), start, end);
        assertEquals(0, new BigDecimal("500").compareTo(revenue));
    }

    @Test
    @Order(6)
    void testGetUnpaidByClient() {
        List<Shipment> unpaid = ShipmentDao.getUnpaidByClient(testClient.getId());
        assertFalse(unpaid.isEmpty());
    }

    @Test
    @Order(7)
    void testGetTotalOwedByClient() {
        BigDecimal owed = ShipmentDao.getTotalOwedByClient(testClient.getId());
        assertEquals(0, new BigDecimal("500").compareTo(owed));
    }

    @Test
    @Order(8)
    void testUpdate() {
        testShipment.setStatus(ShipmentStatus.PAID);
        ShipmentDao.update(testShipment);

        List<Shipment> unpaid = ShipmentDao.getUnpaidByClient(testClient.getId());
        assertTrue(unpaid.isEmpty());
    }

    @Test
    @Order(9)
    void testGetDriverReport() {
        List<Object[]> report = ShipmentDao.getDriverReport(testCompany.getId());
        assertFalse(report.isEmpty());
    }
}