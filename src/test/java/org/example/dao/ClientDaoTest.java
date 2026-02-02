package org.example.dao;

import org.example.entity.Client;
import org.example.entity.Company;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientDaoTest {

    private static Company testCompany;
    private static Client testClient;

    @BeforeAll
    static void setup() {
        testCompany = new Company("Client Test Company");
        CompanyDao.create(testCompany);

        testClient = new Client("Test Client", testCompany);
        testCompany.addClient(testClient);
        CompanyDao.update(testCompany);

        testCompany = CompanyDao.getById(testCompany.getId());
        testClient = testCompany.getClients().iterator().next();
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
        assertNotNull(testClient.getId());
        assertEquals("Test Client", testClient.getName());
    }

    @Test
    @Order(2)
    void testGetById() {
        Client found = ClientDao.getById(testClient.getId());
        assertNotNull(found);
        assertEquals("Test Client", found.getName());
    }

    @Test
    @Order(3)
    void testGetAll() {
        List<Client> clients = ClientDao.get();
        assertFalse(clients.isEmpty());
    }

    @Test
    @Order(4)
    void testUpdate() {
        testClient.setName("Updated Client");
        ClientDao.update(testClient.getId(), testClient);

        Client found = ClientDao.getById(testClient.getId());
        assertEquals("Updated Client", found.getName());
    }

    @Test
    @Order(5)
    void testDelete() {
        Client toDelete = new Client("To Delete", testCompany);
        ClientDao.create(toDelete);
        Long id = toDelete.getId();

        ClientDao.delete(id);

        Client found = ClientDao.getById(id);
        assertNull(found);
    }
}