package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.EmployeeDao;
import org.example.entity.Company;
import org.example.entity.Employee;

import java.math.BigDecimal;
import java.util.Scanner;

public class EmployeeService {
    private final Scanner scanner;
    boolean back = false;

    public EmployeeService(Scanner scanner){
        this.scanner = scanner;
    }

    public Company manageEmployees(Company company) {
        while (!back) {
            System.out.println("\nManaging Employees...");
            System.out.println("[1] List Employees");
            System.out.println("[2] Add New Employee");
            System.out.println("[3] Delete Employee");
            System.out.println("[4] Update Employee salary");
            System.out.println("[0] Back to Company Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listEmployees(company);
                    break;
                case 2:
                    company = addEmployee(company);
                    break;
                case 3:
                    company = deleteEmployee(company);
                    break;
                case 4:
                    company = updateSalary(company);
                    break;
                case 0:
                    back = true;
                    break;
            }
        }
        return company;
    }

    public void listEmployees(Company company) {
        company.getEmployees().forEach(e ->
                System.out.println(e.getId() + ": " + e.getName() + " (" + e.getSalary() + ")"));
    }

    public Company addEmployee(Company company) {
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

    public Company deleteEmployee(Company company) {
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

    public Company updateSalary(Company company) {
        System.out.println("Enter Employee ID to update salary: ");
        while (!scanner.hasNextLong()) {
            scanner.next();
        }
        Long id = scanner.nextLong();
        scanner.nextLine();
        Employee employee = EmployeeDao.getById(id);

        System.out.println("Enter new salary: ");
        while(!scanner.hasNextBigDecimal()) {
            scanner.next();
        }
        BigDecimal salary = scanner.nextBigDecimal();
        scanner.nextLine();

        if (employee != null) {
            employee.setSalary(salary);

            EmployeeDao.update(employee);

            company.getEmployees().stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .ifPresent(e -> e.setSalary(salary));
        }

        System.out.println("Employee updated successfully.");
        return company;
    }
    }
