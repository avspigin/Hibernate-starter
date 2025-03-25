package ru.anton.hibernate.starter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@ToString(exclude = {"course", "student"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_profile")
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private Instant createdAt;

    public void addScore(Student student, Course course, Integer score) {
        this.student = student;
        this.course = course;
        this.score = score;
        student.getStudentProfiles().add(this);
        course.getStudentProfiles().add(this);
    }
}
