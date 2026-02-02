package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.ShipmentDao;
import org.example.entity.Client;
import org.example.entity.Company;
import org.example.entity.Shipment;
import org.example.entity.Vehicle;
import org.example.enums.ShipmentStatus;
import org.example.utils.InputReader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ClientService {
    private final InputReader inputReader;

    public ClientService(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    public Company manageClients(Company company) {
        boolean back = false;
        while (!back) {
            System.out.println("\nManaging Clients...");
            System.out.println("[1] List Clients");
            System.out.println("[2] Add New Client");
            System.out.println("[3] Delete Client");
            System.out.println("[4] Check Owed Money");
            System.out.println("[0] Back to Company Menu");


            long choice = inputReader.readLong("Choice: ");

            switch ((int) choice) {
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
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
        return company;
    }

    public void listClients(Company company) {
        company.getClients().forEach(c ->
                System.out.println(c.getId() + ": " + c.getName()));
    }

    public Company addClient(Company company) {
        String name = inputReader.readString("Enter client name: ");

        Client client = new Client(name, company);
        company.addClient(client);

        Company updated = CompanyDao.update(company);
        System.out.println("Client added successfully.");
        return updated;
    }

    public Company deleteClient(Company company) {
        listClients(company);
        long id = inputReader.readLong("Enter client ID to delete: ");

        Client toRemove = company.getClients().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            company.getClients().remove(toRemove);
            CompanyDao.update(company);
            System.out.println("Client deleted successfully.");
            return CompanyDao.getById(company.getId());
        } else {
            System.out.println("Client not found in this company.");
            return company;
        }
    }

    public void getOwedMoney(Company company) {
        listClients(company);
        long id = inputReader.readLong("Enter client ID to check balance: ");

        BigDecimal totalOwed = ShipmentDao.getTotalOwedByClient(id);
        System.out.println("Total owed money:" + totalOwed);
    }
}
