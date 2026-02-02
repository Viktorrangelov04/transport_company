package org.example.dao;

import org.example.entity.Company;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompanyDaoTest {

    private static Company testCompany;

    @BeforeAll
    static void setup() {
        testCompany = new Company("Test Company");
        CompanyDao.create(testCompany);
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
        assertNotNull(testCompany.getId());
    }

    @Test
    @Order(2)
    void testGetById() {
        Company found = CompanyDao.getById(testCompany.getId());
        assertNotNull(found);
        assertEquals("Test Company", found.getName());
    }

    @Test
    @Order(3)
    void testFindAll() {
        List<Company> companies = CompanyDao.findAll();
        assertFalse(companies.isEmpty());
        assertTrue(companies.stream().anyMatch(c -> c.getId().equals(testCompany.getId())));
    }

    @Test
    @Order(4)
    void testUpdate() {
        testCompany.setName("Updated Company");
        CompanyDao.update(testCompany);

        Company found = CompanyDao.getById(testCompany.getId());
        assertEquals("Updated Company", found.getName());
    }

    @Test
    @Order(5)
    void testDelete() {
        Company toDelete = new Company("To Delete");
        CompanyDao.create(toDelete);
        Long id = toDelete.getId();

        CompanyDao.delete(id);

        Company found = CompanyDao.getById(id);
        assertNull(found);
    }
}
