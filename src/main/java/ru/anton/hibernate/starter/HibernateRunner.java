package ru.anton.hibernate.starter;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.anton.hibernate.starter.entity.*;
import ru.anton.hibernate.starter.util.HibernateUtil;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {
    public static void main(String[] args) {
        Company company = Company.builder().name("Mail").build();

//        User user = User.builder()
//                .username("User@rootovich.ru")
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("Usersss")
//                        .lastname("Rootovich")
//                        .birthDate(new Birthday(LocalDate.of(1992, 1, 21)))
//                        .build())
//                .role(Role.USER)
//                .company(company)
//                .build();
//        log.info("User object in transient state: {}", user);


    }
}
