package ru.anton.hibernate.starter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainer_course")
public class TrainerCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public void addTrainer(Trainer trainer) {
        this.trainer = trainer;
        trainer.getTrainerCourses().add(this);
    }

    public void addCourse(Course course) {
        this.course = course;
        course.getTrainerCourses().add(this);
    }
}
