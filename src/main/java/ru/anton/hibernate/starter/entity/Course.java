    package ru.anton.hibernate.starter.entity;

    import jakarta.persistence.*;
    import lombok.*;

    import java.util.ArrayList;
    import java.util.List;

    @Data
    @EqualsAndHashCode(of = "name")
    @ToString(exclude = {"studentProfiles", "students", "trainerCourses"})
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Entity
    public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String name;

        @Builder.Default
        @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Student> students = new ArrayList<>();

        @Builder.Default
        @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<StudentProfile> studentProfiles = new ArrayList<>();

        @Builder.Default
        @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<TrainerCourse> trainerCourses = new ArrayList<>();

        public void addStudent(Student student) {
            students.add(student);
            student.setCourse(this);
        }
    }
