package org.example.service;

import org.example.dao.CompanyDao;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Vehicle;

import java.util.Scanner;

public class VehicleService {
    boolean back = false;
    public void manageVehicles(Company company, Scanner scanner){
        while (!back) {
            System.out.println("\nManaging Vehicles...");
            System.out.println("[1] List Vehicles");
            System.out.println("[2] Add New Vehicle");
            System.out.println("[3] Delete Vehicle");
            System.out.println("[0] Back to Company Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listVehicles(company);
                    break;
                case 2:
                    company=addVehicle(company, scanner);
                    break;
                case 3:
                    company = deleteVehicle(company, scanner);
                    break;
                case 0:
                    back = true;
                    break;
            }
        }
    }

    private void listVehicles(Company company) {
        for (Vehicle v : company.getVehicles()) {
            String qual = (v.getQualification() != null) ? v.getQualification().getName() : "None";
            System.out.println("ID: " + v.getId() + " | Model: " + v.getName() + " | Required Qual: " + qual);
        }
    }

    private Company addVehicle(Company company, Scanner scanner){
        System.out.println("name:");
        String name = scanner.nextLine();
        Vehicle vehicle = new Vehicle(name, company);
        company.addVehicle(vehicle);

        Company updated = CompanyDao.update(company);

        System.out.println("Vehicle added");
        return updated;
    }

    private Company deleteVehicle(Company company, Scanner scanner){
        System.out.print("Enter Vehicle ID to delete: ");

        while (!scanner.hasNextLong()) {
            scanner.next();
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Vehicle toRemove = null;
        for (Vehicle v : company.getVehicles()) {
            if (v.getId().equals(id)) {
                toRemove = v;
                break;
            }
        }
        if (toRemove != null) {
            company.getVehicles().remove(toRemove);

            Company updated = CompanyDao.update(company);

            System.out.println("Vehicle deleted successfully.");
            return updated;
        } else {
            System.out.println("Vehicle not found in this company.");
            return company;
        }
    }
}
