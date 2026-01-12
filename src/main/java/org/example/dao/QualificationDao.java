package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Qualification;
import org.hibernate.Session;

import java.util.List;

public class QualificationDao {
    public static Qualification findByName(String name) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Qualification WHERE name = :name", Qualification.class)
                    .setParameter("name", name)
                    .uniqueResult();
        }
    }

    public static List<Qualification> findAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Qualification", Qualification.class).list();
        }
    }
}
