package org.example.service;

import org.example.dao.CompanyDao;
import org.example.entity.Client;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Vehicle;
import org.example.utils.InputReader;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class MenuService {
    private final InputReader reader;
    // Inject all services so the MenuService can coordinate them
    private final EmployeeService employeeService;
    private final VehicleService vehicleService;
    private final ClientService clientService;
    private final ShippingService shippingService;
    private final ReportService reportService;

    public MenuService(InputReader reader, EmployeeService es, VehicleService vs,
                       ClientService cs, ShippingService ss, ReportService rs) {
        this.reader = reader;
        this.employeeService = es;
        this.vehicleService = vs;
        this.clientService = cs;
        this.shippingService = ss;
        this.reportService = rs;
    }

    public Company startMenu() {
        System.out.println("--- Welcome to Transport Manager ---");
        System.out.println("[1] Select an existing company");
        System.out.println("[2] Create a new company");
        System.out.println("[0] Exit");

        int choice = (int) reader.readLong("Choice: ");
        switch (choice) {
            case 1: return selectCompany();
            case 2: return createCompany();
            case 0: System.exit(0);
            default: return null;
        }
    }

    private Company selectCompany() {
        List<Company> companies = CompanyDao.findAll();
        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return null;
        }
        for (int i = 0; i < companies.size(); i++) {
            System.out.println("[" + i + "] " + companies.get(i).getName());
        }
        int index = (int) reader.readLong("Select company number: ");
        return (index >= 0 && index < companies.size()) ? companies.get(index) : null;
    }

    private Company createCompany() {
        String name = reader.readString("Enter new company name: ");
        Company company = new Company(name);
        CompanyDao.create(company);
        return company;
    }

    public Company showCompanyDashboard(Company activeCompany) {
        System.out.println("\n--- Dashboard: " + activeCompany.getName() + " ---");
        System.out.println("[1] Employees | [2] Vehicles | [3] Clients | [4] Shipments");
        System.out.println("[5] Reports   | [6] Change Co | [8] Rename   | [9] Delete");
        System.out.println("[0] Exit App");

        int choice = (int) reader.readLong("Choice: ");
        switch (choice) {
            case 1: return employeeService.manageEmployees(activeCompany);
            case 2: return vehicleService.manageVehicles(activeCompany);
            case 3: return clientService.manageClients(activeCompany);
            case 4: return shippingService.manageShipments(activeCompany);
            case 5: reportService.manageReports(activeCompany); return activeCompany;
            case 6: return null; // Resets to start menu
            case 8: return renameCompany(activeCompany);
            case 9: return deleteCompany(activeCompany);
            case 0: System.exit(0);
            default: return activeCompany;
        }
    }

    private Company renameCompany(Company company) {
        String newName = reader.readString("New name: ");
        company.setName(newName);
        return CompanyDao.update(company);
    }

    private Company deleteCompany(Company company) {
        String confirm = reader.readString("Are you sure? Type 'y' to confirm: ");
        if (confirm.equalsIgnoreCase("y")) {
            CompanyDao.delete(company.getId());
            return null;
        }
        return company;
    }
}
