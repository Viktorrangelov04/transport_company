package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.EmployeeDto;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeDao {
    public static void create(Employee employee) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction =session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        }
    }

    public static List<EmployeeDto> getEmployeesDto() {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT new org.example.dto.EmployeeDto(e.id, e.name)" +
                    "FROM Employee e", EmployeeDto.class).getResultList();
        }
    }

    public static Employee getById(Long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT e FROM Employee e LEFT JOIN FETCH e.qualifications WHERE e.id = :id",
                            Employee.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    public static List<Employee> findByQualification(String qualName) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT e FROM Employee e " + "JOIN e.qualifications q " + "WHERE q.name = :qualName", Employee.class)
                    .setParameter("qualName", qualName)
                    .list();
        }
    }

    public List<Employee> findAllSortedBySalary() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Employee ORDER BY salary DESC", Employee.class)
                    .list();
        }
    }

    public static void update(Employee employee) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(employee);
            transaction.commit();
        }
    }

    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM Employee WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
}
