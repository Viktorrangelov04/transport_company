package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Shipment;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ShipmentDao {
    public static void Create(Shipment shipment){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(shipment);
            transaction.commit();
        }
    }
}
