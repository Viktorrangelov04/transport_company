package org.example.service;

import org.example.dao.CompanyDao;
import org.example.entity.Client;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Vehicle;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class MenuService {
    private  Scanner scanner;
    public MenuService(Scanner scanner){
        this.scanner = scanner;
    }

    public Company selectCompanyMenu() {
        List<Company> companies = CompanyDao.findAll();
        if (companies.isEmpty()) {
            System.out.println("No companies found in database!");
            return null;
        }

        System.out.println("\n--- Companies ---");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println("[" + i + "] " + companies.get(i).getName());
        }

        System.out.print("Enter number to select: ");
        int index = scanner.nextInt();
        scanner.nextLine();

        return companies.get(index);
    }

    public void showEmployees(Company company) {
        System.out.println("\nEmployees for " + company.getName() + ":");
        if (company.getEmployees().isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee e : company.getEmployees()) {
                System.out.println("ID: " + e.getId() + " | Name: " + e.getName() + " | Salary: " + e.getSalary());
            }
        }
    }

    public void showVehicles(Company company) {
        System.out.println("\nVehicles for " + company.getName() + ":");
        for (Vehicle v : company.getVehicles()) {
            String qual = (v.getQualification() != null) ? v.getQualification().getName() : "None";
            System.out.println("ID: " + v.getId() + " | Model: " + v.getName() + " | Required Qual: " + qual);
        }
    }

    public void showClients(Company company) {
        System.out.println("\nClients for " + company.getName() + ":");
        if(company.getClients().isEmpty()){
            System.out.println("No Clients found.");
        }else{
            for (Client c : company.getClients()) {
                System.out.println("ID: " + c.getId() + " | Name: " + c.getName());
            }
        }

    }
}
