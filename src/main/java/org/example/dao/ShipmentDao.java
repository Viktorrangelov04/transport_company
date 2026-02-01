package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Shipment;
import org.example.enums.ShipmentStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class ShipmentDao {
    public static void create(Shipment shipment){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(shipment);
            transaction.commit();
        }
    }

    public static Shipment update(Shipment shipment){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(shipment);
            transaction.commit();
            return shipment;
        }
    }

    public static BigDecimal getTotalOwedByClient(Long clientId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            BigDecimal result = session.createQuery("SELECT SUM(s.cost) FROM Shipment s " +
                            "WHERE s.client.id = :clientId AND s.status = :status", BigDecimal.class)
                    .setParameter("clientId", clientId)
                    .setParameter("status", ShipmentStatus.PENDING)
                    .uniqueResult();

            return result != null ? result : BigDecimal.ZERO;
        }
    }

    public static List<Shipment> findAllByCompany(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT s FROM Shipment s " +
                                    "LEFT JOIN FETCH s.employee " +
                                    "LEFT JOIN FETCH s.client " +
                                    "LEFT JOIN FETCH s.vehicle " +
                                    "WHERE s.company.id = :companyId", Shipment.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    public static List<Shipment> getUnpaidByClient(Long clientId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Shipment s WHERE s.client.id = :clientId AND s.status = :status",
                            Shipment.class)
                    .setParameter("clientId", clientId)
                    .setParameter("status", ShipmentStatus.PENDING)
                    .list();
        }
    }

    public List<Shipment> getShipmentsByDestination() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Shipment ORDER BY destination ASC", Shipment.class)
                    .list();
        }
    }
}
