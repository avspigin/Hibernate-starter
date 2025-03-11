package ru.anton.hibernate.starter.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.anton.hibernate.starter.converter.BirthdayConverter;

public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration.addAttributeConverter(new BirthdayConverter());
        return configuration.buildSessionFactory();
    }
}
