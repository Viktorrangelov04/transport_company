package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.EmployeeDao;
import org.example.dao.ShipmentDao;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Shipment;
import org.example.utils.InputReader;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class MenuService {
    private final InputReader reader;
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
        System.out.println("[5] Reports   | [6] Change Co | [7] Sort/Filter");
        System.out.println("[8] Rename    | [9] Delete    | [0] Exit App");

        int choice = (int) reader.readLong("Choice: ");
        switch (choice) {
            case 1: return employeeService.manageEmployees(activeCompany);
            case 2: return vehicleService.manageVehicles(activeCompany);
            case 3: return clientService.manageClients(activeCompany);
            case 4: return shippingService.manageShipments(activeCompany);
            case 5: reportService.manageReports(activeCompany); return activeCompany;
            case 6: return null;
            case 7: showSortFilterMenu(activeCompany); return activeCompany;
            case 8: return renameCompany(activeCompany);
            case 9: return deleteCompany(activeCompany);
            case 0: System.exit(0);
            default: return activeCompany;
        }
    }

    private void showSortFilterMenu(Company company) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Sort & Filter Menu ---");
            System.out.println("[1] Sort all companies by name");
            System.out.println("[2] Sort all companies by revenue");
            System.out.println("[3] Filter companies by name");
            System.out.println("[4] Sort employees by salary");
            System.out.println("[5] Filter employees by qualification");
            System.out.println("[6] Sort shipments by destination");
            System.out.println("[0] Back to Dashboard");

            int choice = (int) reader.readLong("Choice: ");
            switch (choice) {
                case 1: sortCompaniesByName(); break;
                case 2: sortCompaniesByRevenue(); break;
                case 3: filterCompaniesByName(); break;
                case 4: sortEmployeesBySalary(company); break;
                case 5: filterEmployeesByQualification(company); break;
                case 6: sortShipmentsByDestination(company); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option."); break;
            }
        }
    }

    private void sortCompaniesByName() {
        List<Company> companies = CompanyDao.findAll();

        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        companies.sort(Comparator.comparing(Company::getName, String.CASE_INSENSITIVE_ORDER));

        System.out.println("\n=== Companies Sorted by Name ===");
        for (Company c : companies) {
            System.out.println("ID: " + c.getId() + " | Name: " + c.getName());
        }
    }

    private void sortCompaniesByRevenue() {
        List<Company> companies = CompanyDao.findAll();

        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        // Sort by revenue descending
        companies.sort((c1, c2) -> {
            BigDecimal rev1 = ShipmentDao.getTotalRevenue(c1.getId());
            BigDecimal rev2 = ShipmentDao.getTotalRevenue(c2.getId());
            return rev2.compareTo(rev1);
        });

        System.out.println("\n=== Companies Sorted by Revenue (Highest First) ===");
        for (Company c : companies) {
            BigDecimal revenue = ShipmentDao.getTotalRevenue(c.getId());
            System.out.printf("ID: %d | Name: %s | Revenue: $%.2f%n",
                    c.getId(), c.getName(), revenue);
        }
    }

    private void filterCompaniesByName() {
        String searchTerm = reader.readString("Enter name to search: ");

        List<Company> companies = CompanyDao.findAll();
        List<Company> filtered = companies.stream()
                .filter(c -> c.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("No companies found matching '" + searchTerm + "'");
            return;
        }

        System.out.println("\n=== Companies Matching '" + searchTerm + "' ===");
        for (Company c : filtered) {
            System.out.println("ID: " + c.getId() + " | Name: " + c.getName());
        }
    }

    private void sortEmployeesBySalary(Company company) {
        List<Employee> employees = company.getEmployees().stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .toList();

        if (employees.isEmpty()) {
            System.out.println("No employees in this company.");
            return;
        }

        System.out.println("\n=== Employees Sorted by Salary (Highest First) ===");
        for (Employee e : employees) {
            System.out.printf("ID: %d | Name: %s | Salary: $%.2f%n",
                    e.getId(), e.getName(), e.getSalary());
        }
    }

    private void filterEmployeesByQualification(Company company) {
        String qualName = reader.readString("Enter qualification name: ");

        List<Employee> filtered = company.getEmployees().stream()
                .filter(e -> e.getQualifications().stream()
                        .anyMatch(q -> q.getName().equalsIgnoreCase(qualName)))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("No employees found with qualification '" + qualName + "'");
            return;
        }

        System.out.println("\n=== Employees with '" + qualName + "' Qualification ===");
        for (Employee e : filtered) {
            System.out.printf("ID: %d | Name: %s | Salary: $%.2f%n",
                    e.getId(), e.getName(), e.getSalary());
        }
    }

    private void sortShipmentsByDestination(Company company) {
        List<Shipment> shipments = ShipmentDao.findAllByCompany(company.getId());

        if (shipments.isEmpty()) {
            System.out.println("No shipments found for this company.");
            return;
        }

        shipments.sort(Comparator.comparing(Shipment::getDestination, String.CASE_INSENSITIVE_ORDER));

        System.out.println("\n=== Shipments Sorted by Destination ===");
        for (Shipment s : shipments) {
            System.out.printf("ID: %d | %s -> %s | Cost: $%.2f | Status: %s%n",
                    s.getId(),
                    s.getStartLocation(),
                    s.getDestination(),
                    s.getCost(),
                    s.getStatus());
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