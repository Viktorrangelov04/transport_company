package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.QualificationDao;
import org.example.dao.VehicleDao;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Qualification;
import org.example.entity.Vehicle;

import java.util.Scanner;

public class VehicleService {
    private final Scanner scanner;
    boolean back = false;

    public VehicleService(Scanner scanner){
        this.scanner = scanner;
    }

    public Company manageVehicles(Company company){
        while (!back) {
            System.out.println("\nManaging Vehicles");
            System.out.println("[1] List Vehicles");
            System.out.println("[2] Add New Vehicle");
            System.out.println("[3] Delete Vehicle");
            System.out.println("[4] Add Vehicle Qualification");
            System.out.println("[0] Back to Company Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listVehicles(company);
                    break;
                case 2:
                    company=addVehicle(company);
                    break;
                case 3:
                    company = deleteVehicle(company);
                    break;
                case 4:
                    addQualification(company);
                    break;
                case 0:
                    back = true;
                    break;
            }
        }
        return company;
    }

    public void listVehicles(Company company) {
        for (Vehicle v : company.getVehicles()) {
            String qual = (v.getQualification() != null) ? v.getQualification().getName() : "None";
            System.out.println("ID: " + v.getId() + " | Model: " + v.getName() + " | Required Qual: " + qual);
        }
    }

    public Company addVehicle(Company company){
        System.out.println("name:");
        String name = scanner.nextLine();
        Vehicle vehicle = new Vehicle(name, company);
        company.addVehicle(vehicle);

        Company updated = CompanyDao.update(company);

        System.out.println("Vehicle added");
        return updated;
    }

    public Company deleteVehicle(Company company){
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

    public void addQualification(Company company){
        System.out.print("Enter Vehicle ID to update: ");
        long vehicleId = Long.parseLong(scanner.nextLine());
        Vehicle vehicle = VehicleDao.getById(vehicleId);

        if (vehicle == null || !vehicle.getCompany().getId().equals(company.getId())) {
            System.out.println("Error: Vehicle not found or does not belong to your company.");
            return;
        }

        System.out.println("New Qualification name:");
        String qual = scanner.nextLine();
        Qualification newQual = QualificationDao.findByName(qual);

        if (newQual != null) {
            vehicle.setQualification(newQual);

            VehicleDao.update(vehicle);
            System.out.println("Qualification updated!");
        } else {
            System.out.println("Error: Qualification '" + qual + "' not found in database.");
        }
    }
}
