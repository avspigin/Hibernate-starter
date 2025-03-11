package ru.anton.hibernate.starter;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.anton.hibernate.starter.entity.*;
import ru.anton.hibernate.starter.util.HibernateUtil;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {
    public static void main(String[] args) {
        Company company = Company.builder().name("Mail").build();

        User user = User.builder()
                .username("User@rootovich.ru")
                .personalInfo(PersonalInfo.builder()
                        .firstName("Usersss")
                        .lastName("Rootovich")
                        .birthDate(new Birthday(LocalDate.of(1992, 1, 21)))
                        .build())
                .role(Role.USER)
                .company(company)
                .build();
        log.info("User object in transient state: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

//                session1.persist(user);
//                User user1 = session1.get(User.class, 3L);
                session1.persist(user);

                System.out.println(user);
                session1.getTransaction().commit();
            } catch (Exception e) {
                log.error("Exception occurred", e);
                throw e;
            }
        }
    }
}
