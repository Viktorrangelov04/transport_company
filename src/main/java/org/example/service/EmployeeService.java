package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.EmployeeDao;
import org.example.dao.QualificationDao;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Qualification;
import org.example.utils.InputReader;
import java.math.BigDecimal;

public class EmployeeService {
    private final InputReader reader;

    public EmployeeService(InputReader reader) {
        this.reader = reader;
    }

    public Company manageEmployees(Company company) {
        boolean back = false;
        while (!back) {
            System.out.println("\nManaging Employees...");
            System.out.println("[1] List Employees");
            System.out.println("[2] Add New Employee");
            System.out.println("[3] Delete Employee");
            System.out.println("[4] Update Employee Salary");
            System.out.println("[5] Update Employee Qualification");
            System.out.println("[0] Back to Company Menu");

            int choice = (int) reader.readLong("Choice: ");

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
                case 5:
                    addQualificationToEmployee(company);
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
        return company;
    }

    public void listEmployees(Company company) {
        if (company.getEmployees().isEmpty()) {
            System.out.println("No employees found in this company.");
            return;
        }
        company.getEmployees().forEach(e ->
                System.out.println(e.getId() + ": " + e.getName() + " | Salary: $" + e.getSalary()));
    }

    public Company addEmployee(Company company) {
        String name = reader.readString("Name: ");
        BigDecimal salary = reader.readBigDecimal("Salary: ");

        Employee newEmp = new Employee(name, salary, company);
        company.addEmployee(newEmp);

        Company updated = CompanyDao.update(company);
        System.out.println("Employee added!");

        return CompanyDao.getById(updated.getId());
    }

    public Company deleteEmployee(Company company) {
        listEmployees(company);
        long id = reader.readLong("Enter Employee ID to delete: ");

        Employee toRemove = company.getEmployees().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            company.getEmployees().remove(toRemove);
            CompanyDao.update(company);
            System.out.println("Employee deleted successfully.");
            return CompanyDao.getById(company.getId());
        } else {
            System.out.println("Employee not found in this company.");
            return company;
        }
    }

    public Company updateSalary(Company company) {
        listEmployees(company);
        long id = reader.readLong("Enter Employee ID to update: ");

        Employee employee = EmployeeDao.getById(id);
        if (employee != null) {
            BigDecimal newSalary = reader.readBigDecimal("Enter new salary: ");
            employee.setSalary(newSalary);

            EmployeeDao.update(employee);

            company.getEmployees().stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .ifPresent(e -> e.setSalary(newSalary));

            System.out.println("Salary updated successfully.");
        } else {
            System.out.println("Error: Employee not found.");
        }
        return company;
    }

    private void addQualificationToEmployee(Company company) {
        listEmployees(company);
        long empId = reader.readLong("Employee ID: ");
        Employee emp = EmployeeDao.getById(empId);

        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        QualificationDao.findAll().forEach(q ->
                System.out.println(q.getId() + ": " + q.getName()));

        String qualName = reader.readString("Qualification name: ");
        Qualification qual = QualificationDao.findByName(qualName);

        if (qual != null) {
            emp.addQualification(qual);
            EmployeeDao.update(emp);
            System.out.println("Qualification added!");
        } else {
            System.out.println("Qualification not found.");
        }
    }
}
