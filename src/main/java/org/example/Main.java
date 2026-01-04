package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.ClientDao;
import org.example.dao.CompanyDao;
import org.example.dao.EmployeeDao;
import org.example.dao.VehicleDao;
import org.example.entity.Client;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Vehicle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.google.protobuf.JavaFeaturesProto.java;

public class Main {
    public static void main(String[] args) throws SQLException {
//        Employee employee = new Employee();
//        employee.setName("Pesho");

//        EmployeeDao.create(employee);
//        EmployeeDao.update(1, employee);
//        EmployeeDao.delete(1);
//        EmployeeDao.getEmployeesDto()
//                .forEach(System.out::println);
//        Client client = new Client();
//        client.setName("Viktor");
//
//        ClientDao.delete(1);
//        ClientDao.get().forEach(System.out::println);


        SessionFactoryUtil.getSessionFactory().openSession();
    }
}