package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.EmployeeDto;
import org.example.entity.Company;
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

    public static Employee getById(long id){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Employee.class, id);
        }
    }

    public static void update(long id, Employee employee) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Employee employee1=session.find(Employee.class, id);
            employee1.setName(employee.getName());
            session.persist(employee1);
            transaction.commit();
        }
    }

    public static void delete(long id) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Employee employee1=session.find(Employee.class, id);

            session.remove(employee1);
            transaction.commit();
        }
    }
}
