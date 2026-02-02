package org.example.dao;

import org.example.entity.Company;
import org.example.entity.Vehicle;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VehicleDaoTest {

    private static Company testCompany;
    private static Vehicle testVehicle;

    @BeforeAll
    static void setup() {
        testCompany = new Company("Vehicle Test Company");
        CompanyDao.create(testCompany);

        testVehicle = new Vehicle("Mercedes Bus", testCompany);
        testCompany.addVehicle(testVehicle);
        CompanyDao.update(testCompany);

        testCompany = CompanyDao.getById(testCompany.getId());
        testVehicle = testCompany.getVehicles().iterator().next();
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
        assertNotNull(testVehicle.getId());
        assertEquals("Mercedes Bus", testVehicle.getName());
    }

    @Test
    @Order(2)
    void testGetById() {
        Vehicle found = VehicleDao.getById(testVehicle.getId());
        assertNotNull(found);
        assertEquals("Mercedes Bus", found.getName());
    }

    @Test
    @Order(3)
    void testGetAll() {
        List<Vehicle> vehicles = VehicleDao.get();
        assertFalse(vehicles.isEmpty());
    }

    @Test
    @Order(4)
    void testUpdate() {
        testVehicle.setName("Updated Bus");
        VehicleDao.update(testVehicle);

        Vehicle found = VehicleDao.getById(testVehicle.getId());
        assertEquals("Updated Bus", found.getName());
    }

    @Test
    @Order(5)
    void testDelete() {
        Vehicle toDelete = new Vehicle("To Delete", testCompany);
        VehicleDao.create(toDelete);
        Long id = toDelete.getId();

        VehicleDao.delete(id);

        Vehicle found = VehicleDao.getById(id);
        assertNull(found);
    }
}