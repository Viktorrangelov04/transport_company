package org.example.service;

import org.example.dao.CompanyDao;
import org.example.entity.Client;
import org.example.entity.Company;
import org.example.entity.Vehicle;

import java.util.Scanner;

public class ClientService {
    boolean back = false;
    public void manageClients(Company company, Scanner scanner){
        while (!back) {
            System.out.println("\nManaging Clients...");
            System.out.println("[1] List Clients");
            System.out.println("[2] Add New Client");
            System.out.println("[3] Delete Client");
            System.out.println("[0] Back to Company Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listClients(company);
                    break;
                case 2:
                    company = addClient(company, scanner);
                    break;
                case 3:
                    company = deleteClient(company, scanner);
                    break;
                case 0:
                    back = true;
                    break;
            }
        }
    }

    private void listClients(Company company) {
        company.getClients().forEach(c ->
                System.out.println(c.getId() + ": " + c.getName() + " (" + c.getOwedMoney() + ")"));
    }

    private Company addClient(Company company, Scanner scanner){
        System.out.println("name:");
        String name = scanner.nextLine();

        Client client  = new Client(name, company);
        company.addClient(client);

        Company updated = CompanyDao.update(company);
        System.out.println("Client added");

        return updated;
    }
    private Company deleteClient(Company company, Scanner scanner){
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
}
