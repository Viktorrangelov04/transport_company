package org.example.dao;

import org.example.entity.Company;
import org.example.entity.Employee;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeDaoTest {

    private static Company testCompany;
    private static Employee testEmployee;

    @BeforeAll
    static void setup() {
        testCompany = new Company("Employee Test Company");
        CompanyDao.create(testCompany);

        testEmployee = new Employee("John Driver", new BigDecimal("1500"), testCompany);
        testCompany.addEmployee(testEmployee);
        CompanyDao.update(testCompany);

        testCompany = CompanyDao.getById(testCompany.getId());
        testEmployee = testCompany.getEmployees().iterator().next();
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
        assertNotNull(testEmployee.getId());
        assertEquals("John Driver", testEmployee.getName());
    }

    @Test
    @Order(2)
    void testGetById() {
        Employee found = EmployeeDao.getById(testEmployee.getId());
        assertNotNull(found);
        assertEquals("John Driver", found.getName());
        assertEquals(new BigDecimal("1500"), found.getSalary());
    }

    @Test
    @Order(3)
    void testUpdate() {
        testEmployee.setSalary(new BigDecimal("2000"));
        EmployeeDao.update(testEmployee);

        Employee found = EmployeeDao.getById(testEmployee.getId());
        assertEquals(new BigDecimal("2000"), found.getSalary());
    }

    @Test
    @Order(4)
    void testGetEmployeesDto() {
        var dtos = EmployeeDao.getEmployeesDto();
        assertFalse(dtos.isEmpty());
    }

    @Test
    @Order(5)
    void testDelete() {
        Employee toDelete = new Employee("To Delete", new BigDecimal("1000"), testCompany);
        EmployeeDao.create(toDelete);
        Long id = toDelete.getId();

        EmployeeDao.delete(id);

        Employee found = EmployeeDao.getById(id);
        assertNull(found);
    }
}
