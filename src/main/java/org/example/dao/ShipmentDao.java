package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Shipment;
import org.example.enums.ShipmentStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    //reports
    public static Long getTotalShipmentCount(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(s) FROM Shipment s WHERE s.company.id = :id", Long.class)
                    .setParameter("id", companyId)
                    .uniqueResult();
        }
    }

    public static BigDecimal getTotalRevenue(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            BigDecimal result = session.createQuery("SELECT SUM(s.cost) FROM Shipment s WHERE s.company.id = :id", BigDecimal.class)
                    .setParameter("id", companyId)
                    .uniqueResult();
            return result != null ? result : BigDecimal.ZERO;
        }
    }

    public static BigDecimal getRevenueForPeriod(Long companyId, LocalDate start, LocalDate end) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            BigDecimal result = session.createQuery(
                            "SELECT SUM(s.cost) FROM Shipment s " +
                                    "WHERE s.company.id = :id AND s.startDate >= :start AND s.endDate <= :end", BigDecimal.class)
                    .setParameter("id", companyId)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .uniqueResult();
            return result != null ? result : BigDecimal.ZERO;
        }
    }

    public static List<Object[]> getDriverReport(Long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT s.employee.name, COUNT(s), SUM(s.cost) " +
                                    "FROM Shipment s " +
                                    "WHERE s.company.id = :id " +
                                    "GROUP BY s.employee.id, s.employee.name", Object[].class)
                    .setParameter("id", companyId)
                    .list();
        }
    }

}
