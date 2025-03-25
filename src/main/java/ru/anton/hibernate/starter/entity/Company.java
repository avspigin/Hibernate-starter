package ru.anton.hibernate.starter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "name")  //Исключить для того чтоб не переопределять хэшкод по юзеру, так как оттуда кинет сюда, а отсюда туда. Все зациклится при использовании сет и мэп
@ToString(exclude = "users")
@Builder
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default        //По умолчанию присваивает значение. В данном случае new HashSet<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)   //Мапит на компанию в юзере, каскадное изменение(полное), удаление в базе при удалении из коллекции
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }
}
