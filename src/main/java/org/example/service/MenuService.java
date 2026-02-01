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
    private final Scanner scanner;
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

    public Company changeCompanyName(Company company) {
        System.out.println("Current name: " + company.getName());
        System.out.println("New name:");

        String newName = scanner.nextLine();

        if (newName != null && !newName.trim().isEmpty()) {
            company.setName(newName);
            return CompanyDao.update(company);
        }

        System.out.println("Name change cancelled: Invalid input.");
        return company;
    }


}
