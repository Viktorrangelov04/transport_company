package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CompanyDao {
    public static void create(Company company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
        }
    }

    public static List<Company> findAll() {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM Company c", Company.class).getResultList();
        }
    }

    public static Company getById(long id) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Company.class, id);

        }
    }

    public List<Company> filterByName(String namePart) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Company WHERE name LIKE :name", Company.class)
                    .setParameter("name", "%" + namePart + "%")
                    .list();
        }
    }

    public static Company update(Company company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company updatedCompany = session.merge(company);
            transaction.commit();
            return updatedCompany;
        }
    }

    public static void delete(long id) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company1=session.find(Company.class, id);

            session.remove(company1);
            transaction.commit();
        }
    }
}
