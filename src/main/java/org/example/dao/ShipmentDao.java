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

    public BigDecimal getTotalOwedByClient(Long clientId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            BigDecimal result = session.createQuery("SELECT SUM(s.cost) FROM Shipment s " +
                            "WHERE s.client.id = :clientId AND s.status = :status", BigDecimal.class)
                    .setParameter("clientId", clientId)
                    .setParameter("status", ShipmentStatus.PENDING)
                    .uniqueResult();

            return result != null ? result : BigDecimal.ZERO;
        }
    }

    public List<Shipment> getShipmentsByDestination() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Shipment ORDER BY destination ASC", Shipment.class)
                    .list();
        }
    }
}
