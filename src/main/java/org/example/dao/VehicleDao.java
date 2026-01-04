package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class VehicleDao {
    public static void create(Vehicle vehicle) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(vehicle);
            transaction.commit();
        }
    }
    public static List<Vehicle> get() {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            return session.createQuery("SELECT v from Vehicle v", Vehicle.class).getResultList();
        }
    }

    public static Vehicle get(Long id) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Vehicle.class, id);
        }
    }

    public static void  update(long id, Vehicle vehicle) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Vehicle vehicle1=session.find(Vehicle.class, id);
            vehicle1.setName(vehicle.getName());
            session.persist(vehicle1);
            transaction.commit();
        }
    }
    public static void delete(long id) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Vehicle vehicle1=session.find(Vehicle.class, id);
            session.remove(vehicle1);
            transaction.commit();
        }
    }

}
