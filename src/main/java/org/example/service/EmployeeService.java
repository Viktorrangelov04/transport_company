package org.example.service;

import org.example.dao.CompanyDao;
import org.example.entity.Company;
import org.example.entity.Employee;

import java.math.BigDecimal;
import java.util.Scanner;

public class EmployeeService {
    boolean back = false;
    public void manageEmployees(Company company, Scanner scanner) {
        while (!back) {
            System.out.println("\nManaging Employees...");
            System.out.println("[1] List Employees");
            System.out.println("[2] Add New Employee");
            System.out.println("[3] Delete Employee");
            System.out.println("[0] Back to Company Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listEmployees(company);
                    break;
                case 2:
                    company = addEmployee(company, scanner);
                    break;
                case 3:
                    company = deleteEmployee(company, scanner);
                    break;
                case 0:
                    back = true;
                    break;
            }
        }
    }

    private void listEmployees(Company company) {
        company.getEmployees().forEach(e ->
                System.out.println(e.getId() + ": " + e.getName() + " (" + e.getSalary() + ")"));
    }

    private Company addEmployee(Company company, Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Salary: ");
        BigDecimal salary = scanner.nextBigDecimal();

        Employee newEmp = new Employee(name, salary, company);
        company.addEmployee(newEmp);

        Company updated = CompanyDao.update(company);
        System.out.println("Employee added!");

        return updated;
    }

    private Company deleteEmployee(Company company, Scanner scanner) {
        System.out.print("Enter Employee ID to delete: ");
        while (!scanner.hasNextLong()) {
            scanner.next();
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Employee toRemove = null;
        for (Employee e : company.getEmployees()) {
            if (e.getId().equals(id)) {
                toRemove = e;
                break;
            }
        }

        if (toRemove != null) {
            company.getEmployees().remove(toRemove);

            Company updated = CompanyDao.update(company);

            System.out.println("Employee deleted successfully.");
            return updated;
        } else {
            System.out.println("Employee not found in this company.");
            return company;
        }
    }
    }
