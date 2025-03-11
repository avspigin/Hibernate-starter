package ru.anton.hibernate.starter;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import ru.anton.hibernate.starter.entity.*;
import ru.anton.hibernate.starter.util.HibernateUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    @Test
    public void testOneToOne() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        User user = User.builder()
                .username("test3@test.com")
                .build();
        Profile profile = Profile.builder()
                .language("RU")
                .street("Test Street")
                .build();
        Company company = session.get(Company.class, 7L);
        user.setCompany(company);
        session.persist(user);
        profile.setUser(user);
        session.persist(profile);

        session.getTransaction().commit();
    }

    @Test
    public void checkOrphanRemoval() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 7L);
        company.getUsers().removeIf(user -> user.getUsername().equals("test@test.com"));

        session.getTransaction().commit();
    }


    @Test
    public void checkOneToMany() {
        //@Cleanup вызывает метод close()
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 2);
        System.out.println(company.getUsers());

        session.getTransaction().commit();
    }

    @Test
    public void addNewUserAndCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Test")
                .build();
        User user = User.builder()
                .username("test@test.com")
                .build();

        company.addUser(user);
        session.persist(company);

        session.getTransaction().commit();
    }

  /*  @Test
    void testHibernateApi() throws SQLException, IllegalAccessException {
        var user = User.builder()
                .username("ivan@gmail.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .birthDate(new Birthday(LocalDate.of(2000, 5, 15)))
                .build();
        var sql = """
                INSERT INTO
                %s
                (%s)
                VALUES
                (%s)
                """;

        //Получаем имя таблицы в строке. через рефлексию получаем класс.получаемАннотацию(Table).мап(с аннотации берем схему + . + имя таблицы
        //  или если вернул нулл, тогда берем имя класса юзера
        var tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(table -> table.schema() + "." + table.name())
                .orElse(user.getClass().getName());

        //получаем все поля класса по рефлексии
        Field[] fields = user.getClass().getDeclaredFields();

        //Здесь тоже получаем строку из массива. Сперва стримим массив, получаем аннотацию Column,
        // мапим - берем с этой аннотации значения name (по примеру @Column(name = "birth_date")
        // если нул, то берем имя поля
        //собираем коллекцию в стринг через запятую.
        var fieldNames = Arrays.stream(fields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(Collectors.joining(", "));

        var columnValues = Arrays.stream(fields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        System.out.println(sql.formatted(tableName, fieldNames, columnValues));
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, fieldNames, columnValues));

        // Получение значения полей
        for (int i = 0; i < fields.length; i++) {
            //получаем доступ к приватным полям
            fields[i].setAccessible(true);
            preparedStatement.setObject(i + 1, fields[i].get(user));
        }

        System.out.println(preparedStatement);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }*/
}