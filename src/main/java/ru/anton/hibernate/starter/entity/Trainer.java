package ru.anton.hibernate.starter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "username")
@ToString(exclude = "trainerCourses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private PersonalInfo personalInfo;

    @Builder.Default
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainerCourse> trainerCourses = new ArrayList<>();
}
