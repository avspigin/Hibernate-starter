package ru.anton.hibernate.starter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"course", "studentProfiles"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @Embedded
    private PersonalInfo personalInfo;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    private Course course;

    @Builder.Default
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentProfile> studentProfiles = new ArrayList<>();

}
