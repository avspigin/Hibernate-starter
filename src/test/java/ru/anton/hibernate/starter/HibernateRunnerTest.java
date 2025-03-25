package ru.anton.hibernate.starter;

import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.anton.hibernate.starter.entity.*;
import ru.anton.hibernate.starter.util.HibernateUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class HibernateRunnerTest {

    @Test
    void checkHQL() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String name = "Pavel";
        String company = "Company1";

//        var users = session.createNamedQuery("findByNameAndCompany", User.class)
//                .setParameter("firstname", name)
//                .setParameter("company", company)
//                .list();

        session.createQuery("update User u set u.role = 'ADMIN'")
                .executeUpdate();

//        System.out.println(users);


        transaction.commit();
    }

//    @Test
//    void checkInheritance() {
//        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
//        @Cleanup Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        var company = Company.builder()
//                .name("Yandex")
//                .build();
//        session.persist(company);
//
//        var programmer = Programmer.builder()
//                .username("Vladimir@gmail.com")
//                .language(Language.KOTLIN)
//                .company(company)
//                .build();
//        session.persist(programmer);
//
//        var manager = Manager.builder()
//                .username("Elena@gmail.com")
//                .project("Java Enterprise")
//                .company(company)
//                .build();
//        session.persist(manager);
//
//        session.flush();    //Синхронизируем данные
//        session.clear();     //очищаем кеш
//
//        var programmer1 = session.get(Programmer.class, 1L);
//        var manager1 = session.get(User.class, 2L);
//
//        System.out.println(programmer1 + "\n " + manager1);
//
//        transaction.commit();
//    }

    @Test
    public void checkH2() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        var company = Company.builder()
                .name("Google")
                .build();

        session.persist(company);
        transaction.commit();
    }

    @Test
    public void removeCourse() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Course course = session.get(Course.class, 8);

        session.remove(course);
        transaction.commit();
    }

    @Test
    public void updateCourse() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Course course = session.get(Course.class, 7);
        course.setName("Java Enterprise");
        session.merge(course);
        transaction.commit();
    }

    @Test
    public void addTrainerAndListCourses() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Course course1 = Course.builder()
                .name("Java EE")
                .build();
        Course course2 = Course.builder()
                .name("Python")
                .build();

        Trainer trainer = Trainer.builder()
                .username("TestTrainer")
                .personalInfo(PersonalInfo.builder()
                        .firstname("John")
                        .lastname("Doe")
                        .birthDate(new Birthday(LocalDate.of(1990, 1, 1)))
                        .build())
                .build();
        TrainerCourse trainerCourse1 = new TrainerCourse();
        trainerCourse1.addTrainer(trainer);
        trainerCourse1.addCourse(course1);

        TrainerCourse trainerCourse2 = new TrainerCourse();
        trainerCourse2.addCourse(course2);
        trainerCourse2.addTrainer(trainer);

        session.persist(course1);
        session.persist(course2);
        session.persist(trainer);
        session.persist(trainerCourse1);
        session.persist(trainerCourse2);
        transaction.commit();
    }

    @Test
    public void removeCourseById() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = session.get(Course.class, 6);

        session.remove(course);
        session.getTransaction().commit();
    }

    @Test
    public void addStudentOnCourse() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = session.get(Course.class, 6);
        Student student = Student.builder()
                .username("studentTest4")
                .personalInfo(PersonalInfo.builder()
                        .firstname("NameTest4")
                        .lastname("LastNameTest4")
                        .birthDate(new Birthday(LocalDate.of(2020, 1, 1)))
                        .build())
                .build();
        course.addStudent(student);
        session.persist(course);
        session.getTransaction().commit();
    }

    @Test
    public void whenAllScoreLess6ThenRemoveStudent() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        // Получаем курс
        Course course = session.get(Course.class, 6);

// Создаём копию списка студентов (чтобы избежать ConcurrentModificationException)
        List<Student> studentsToRemove = new ArrayList<>(course.getStudents());

// Проверяем оценки и удаляем студентов
        studentsToRemove.removeIf(student -> {
            List<StudentProfile> studentProfiles = student.getStudentProfiles();
            double sum = studentProfiles.stream().mapToDouble(StudentProfile::getScore).average().orElse(0);

            if (sum < 6) {
                course.getStudents().remove(student); // Удаляем из списка курса
                session.remove(student); // Удаляем из базы
                return true; // Удаляем из списка
            }
            return false;
        });

        session.getTransaction().commit();
    }

    @Test
    public void insertStudentProfile() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();


        Course course = session.get(Course.class, 6);
        Student student = session.get(Student.class, 3L);
        StudentProfile studentProfile = new StudentProfile();
        studentProfile.addScore(student, course, 5);
        studentProfile.setCreatedAt(Instant.now());
        session.persist(studentProfile);

        session.getTransaction().commit();
    }

    @Test
    public void FindAllStudentsByCourse() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Course course = session.get(Course.class, 6);
        List<Student> students = course.getStudents();

        session.getTransaction().commit();
        Assertions.assertEquals(students.getFirst().getCourse(), course);
        Assertions.assertNotNull(students);
    }

    @Test
    public void testInsertStudentAndCourse() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = Course.builder()
                .name("Java EE")
                .build();
        Student student = Student.builder()
                .username("studentTest")
                .personalInfo(PersonalInfo.builder()
                        .firstname("NameTest")
                        .lastname("LastNameTest")
                        .birthDate(new Birthday(LocalDate.of(2020, 1, 1)))
                        .build())
                .build();
        course.addStudent(student);
        session.persist(course);
        session.getTransaction().commit();
    }

    @Test
    public void checkManyToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Chat chat = session.get(Chat.class, 1L);
        User user = session.get(User.class, 7L);
        UserChat userChat = UserChat.builder()
                .user(user)
                .chat(chat)
                .build();

        userChat.setUser(user);
        userChat.setChat(chat);
        userChat.setCreatedBy("Anton");
        userChat.setCreatedAt(Instant.now());

        session.persist(userChat);

        session.getTransaction().commit();
    }

    @Test
    public void checkOneToOne() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        User user = User.builder()
                .username("ivan@test.com")
                .build();

        Profile profile = Profile.builder()
                .language("RU")
                .street("Test Street")
                .build();

        Company company = Company.builder()
                .name("Test Company4")
                .build();
        session.persist(company);
        company.addUser(user);
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

//    @Test
//    public void addNewUserAndCompany() {
//        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
//        @Cleanup var session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        Company company = Company.builder()
//                .name("Test")
//                .build();
//        User user = User.builder()
//                .username("test@test.com")
//                .build();
//
//        company.addUser(user);
//        session.persist(company);
//
//        session.getTransaction().commit();
//    }

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