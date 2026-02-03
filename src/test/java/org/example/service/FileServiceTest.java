package org.example.service;

import org.example.entity.Company;
import org.example.entity.Shipment;
import org.example.enums.ShipmentStatus;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileServiceTest {

    private FileService fileService;
    private static final String TEST_FILENAME = "test_shipments.dat";

    @BeforeEach
    void setup() {
        fileService = new FileService();
    }

    @AfterEach
    void cleanup() {
        File file = new File("exports", TEST_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testExportCreatesFile() {
        List<Shipment> shipments = createTestShipments();

        fileService.exportShipments(shipments, TEST_FILENAME);

        File file = new File("exports", TEST_FILENAME);
        assertTrue(file.exists());
    }


    @Test
    void testExportAndImport() {
        List<Shipment> shipments = createTestShipments();
        fileService.exportShipments(shipments, TEST_FILENAME);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        fileService.importAndShowShipments(TEST_FILENAME);

        System.setOut(originalOut);

        String output = outContent.toString();
        assertTrue(output.contains("Varna"), "Output should contain 'Varna'. Actual: " + output);
    }

    @Test
    void testImportNonExistentFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        fileService.importAndShowShipments("nonexistent.dat");

        String output = outContent.toString();
        assertTrue(output.contains("not found") || output.contains("Error"));

        System.setOut(System.out);
    }

    private List<Shipment> createTestShipments() {
        List<Shipment> shipments = new ArrayList<>();

        Shipment s = new Shipment();
        s.setId(1L);
        s.setStartLocation("Sofia");
        s.setDestination("Varna");
        s.setCost(new BigDecimal("100"));
        s.setStartDate(LocalDate.now());
        s.setEndDate(LocalDate.now().plusDays(1));
        s.setStatus(ShipmentStatus.PENDING);

        shipments.add(s);
        return shipments;
    }
}
