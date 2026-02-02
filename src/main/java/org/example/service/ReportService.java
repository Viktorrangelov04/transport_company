package org.example.service;

import org.example.dao.ShipmentDao;
import org.example.entity.Company;
import org.example.utils.InputReader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReportService {
    private final InputReader reader;

    public ReportService(InputReader reader) {
        this.reader = reader;
    }

    public void manageReports(Company company) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Reports Menu ---");
            System.out.println("[1] Total shipments");
            System.out.println("[2] Total earnings");
            System.out.println("[3] Earnings for a period");
            System.out.println("[4] Driver report");
            System.out.println("[0] Back to company menu");

            int choice = (int) reader.readLong("Choice: ");
            switch (choice) {
                case 1: showTotalShipments(company); break;
                case 2: showTotalRevenue(company); break;
                case 3: showEarningsForPeriod(company); break;
                case 4: showDriverReport(company); break;
                case 0: back = true; break;
                default: System.out.println("Invalid choice."); break;
            }
        }
    }

    public void showTotalShipments(Company company) {
        Long shipmentCount = ShipmentDao.getTotalShipmentCount(company.getId());
        System.out.println("\n=== Shipment Report ===");
        System.out.println("Total shipments: " + shipmentCount);
        System.out.println("=======================");
    }

    public void showTotalRevenue(Company company) {
        BigDecimal revenue = ShipmentDao.getTotalRevenue(company.getId());
        System.out.println("\n=== Earning Report ===");
        System.out.printf("Total revenue: $%.2f%n", revenue);
        System.out.println("======================");
    }

    private void showEarningsForPeriod(Company company) {
        System.out.println("\n--- Select Period ---");
        LocalDate startDate = reader.readDate("Start date (YYYY-MM-DD): ");
        LocalDate endDate = reader.readDate("End date (YYYY-MM-DD): ");

        BigDecimal revenue = ShipmentDao.getRevenueForPeriod(company.getId(), startDate, endDate);

        System.out.println("\n=== Period Revenue Report ===");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.printf("Total revenue: $%.2f%n", revenue);
        System.out.println("=============================");
    }

    private void showDriverReport(Company company) {
        List<Object[]> reportData = ShipmentDao.getDriverReport(company.getId());

        System.out.println("\n===== DRIVER PERFORMANCE REPORT =====");
        System.out.printf("%-20s | %-12s | %-15s%n", "Driver Name", "Shipments", "Total Revenue");
        System.out.println("-----------------------------------------------------------");

        if (reportData.isEmpty()) {
            System.out.println("No shipment data available for drivers.");
        } else {
            for (Object[] row : reportData) {
                String driverName = (String) row[0];
                Long shipmentCount = (Long) row[1];
                BigDecimal totalRevenue = (BigDecimal) row[2];

                System.out.printf("%-20s | %-12d | $%-15.2f%n",
                        driverName, shipmentCount, totalRevenue);
            }
        }
        System.out.println("-----------------------------------------------------------");
    }
}
