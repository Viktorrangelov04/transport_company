package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.ShipmentDao;
import org.example.entity.Client;
import org.example.entity.Company;
import org.example.entity.Shipment;
import org.example.entity.Vehicle;
import org.example.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ClientService {
    private final Scanner scanner;
    boolean back = false;
    public ClientService(Scanner scanner){
        this.scanner = scanner;
    }
    public Company manageClients(Company company){
        while (!back) {
            System.out.println("\nManaging Clients...");
            System.out.println("[1] List Clients");
            System.out.println("[2] Add New Client");
            System.out.println("[3] Delete Client");
            System.out.println("[4] Check owed money");
            System.out.println("[0] Back to Company Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listClients(company);
                    break;
                case 2:
                    company = addClient(company);
                    break;
                case 3:
                    company = deleteClient(company);
                    break;
                case 4:
                    getOwedMoney(company);
                    break;
                case 0:
                    back = true;
                    break;
            }
        }
        return company;
    }

    public void listClients(Company company) {
        company.getClients().forEach(c ->
                System.out.println(c.getId() + ": " + c.getName()));
    }

    public Company addClient(Company company){
        System.out.println("name:");
        String name = scanner.nextLine();

        Client client  = new Client(name, company);
        company.addClient(client);

        Company updated = CompanyDao.update(company);
        System.out.println("Client added");

        return updated;
    }
    public Company deleteClient(Company company){
        System.out.print("Enter Client ID to delete: ");

        while (!scanner.hasNextLong()) {
            scanner.next();
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        Client toRemove = null;
        for (Client c : company.getClients()) {
            if (c.getId().equals(id)) {
                toRemove = c;
                break;
            }
        }
        if (toRemove != null) {
            company.getClients().remove(toRemove);

            Company updated = CompanyDao.update(company);

            System.out.println("Client deleted successfully.");
            return updated;
        } else {
            System.out.println("Client not found in this company.");
            return company;
        }
    }

    public void getOwedMoney(Company company){
        listClients(company);
        System.out.println("Enter Client ID to get owed money: ");
        while (!scanner.hasNextLong()) {
            scanner.next();
        }
        Long id = scanner.nextLong();
        scanner.nextLine();

        BigDecimal totalOwed = ShipmentDao.getTotalOwedByClient(id);

        System.out.println("Total Balance Owed: $" + totalOwed);
    }


}
