package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.QualificationDao;
import org.example.dao.VehicleDao;
import org.example.entity.Company;
import org.example.entity.Qualification;
import org.example.entity.Vehicle;
import org.example.utils.InputReader;

public class VehicleService {
    private final InputReader reader;

    public VehicleService(InputReader reader) {
        this.reader = reader;
    }

    public Company manageVehicles(Company company) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Managing Vehicles ---");
            System.out.println("[1] List Vehicles");
            System.out.println("[2] Add New Vehicle");
            System.out.println("[3] Delete Vehicle");
            System.out.println("[4] Add/Update Vehicle Qualification");
            System.out.println("[0] Back to Company Menu");

            int choice = (int) reader.readLong("Choice: ");

            switch (choice) {
                case 1: listVehicles(company); break;
                case 2: company = addVehicle(company); break;
                case 3: company = deleteVehicle(company); break;
                case 4: addQualification(company); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option."); break;
            }
        }
        return company;
    }

    public void listVehicles(Company company) {
        if (company.getVehicles().isEmpty()) {
            System.out.println("No vehicles found for this company.");
            return;
        }
        for (Vehicle v : company.getVehicles()) {
            String qual = (v.getQualification() != null) ? v.getQualification().getName() : "None";
            System.out.println("ID: " + v.getId() + " | Model: " + v.getName() + " | Required Qual: " + qual);
        }
    }

    public Company addVehicle(Company company) {
        String name = reader.readString("Enter vehicle model/name: ");
        Vehicle vehicle = new Vehicle(name, company);
        company.addVehicle(vehicle);

        CompanyDao.update(company);
        System.out.println("Vehicle added!");
        return CompanyDao.getById(company.getId());
    }

    public Company deleteVehicle(Company company) {
        listVehicles(company);
        long id = reader.readLong("Enter Vehicle ID to delete: ");

        Vehicle toRemove = company.getVehicles().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            company.getVehicles().remove(toRemove);
            CompanyDao.update(company);
            System.out.println("Vehicle deleted successfully.");
            return CompanyDao.getById(company.getId());
        } else {
            System.out.println("Vehicle not found in this company.");
            return company;
        }
    }

    public void addQualification(Company company) {
        listVehicles(company);
        long vehicleId = reader.readLong("Enter Vehicle ID to update: ");
        Vehicle vehicle = VehicleDao.getById(vehicleId);

        if (vehicle == null || !vehicle.getCompany().getId().equals(company.getId())) {
            System.out.println("Error: Vehicle not found or does not belong to your company.");
            return;
        }

        String qualName = reader.readString("Enter required Qualification name: ");
        Qualification newQual = QualificationDao.findByName(qualName);

        if (newQual != null) {
            vehicle.setQualification(newQual);
            VehicleDao.update(vehicle);
            System.out.println("Qualification updated successfully!");
        } else {
            System.out.println("Error: Qualification '" + qualName + "' not found in database.");
        }
    }
}
