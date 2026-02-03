package org.example.service;

import org.example.entity.Shipment;

import java.io.*;
import java.util.List;

public class FileService {
    public void exportShipments(List<Shipment> shipments, String filename) {
        try {
            File directory = new File("exports");

            File file = new File(directory, filename);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(shipments);
                System.out.println("Data successfully saved to: " + file.getPath());
            }

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void importAndShowShipments(String filename) {
        File file = new File("exports", filename);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Shipment> shipments = (List<Shipment>) ois.readObject();

            System.out.println("=== Data from File ===");
            for (Shipment s : shipments) {
                System.out.println("Shipment ID: " + s.getId() + " | Dest: " + s.getDestination() + " | Cost: " + s.getCost());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

}
