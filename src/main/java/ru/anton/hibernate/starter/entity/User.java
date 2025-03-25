package ru.anton.hibernate.starter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NamedQuery(name = "findByNameAndCompany", query = """      
                select u from User u
                left join u.company c
                where u.personalInfo.firstname = :firstname
                and c.name = :company
                """)        //Именованный запрос. Его реализация в тесте checkHQL
@Data
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"profile", "userChats"})  //исключает из тустринг поле компани
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
//@Inheritance(strategy = InheritanceType.JOINED)  //При этом использовании - меняем генерацию @GeneratedValue(strategy = GenerationType.SEQUENCE)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //IDENTITY дает сериализацию БД, SEQUENCE - создаем свой сиквенс
    private Long id;

    private String username;
    @Embedded       //Показывает что он главный, но это необязательно, мы указали на песроналИнфо
    private PersonalInfo personalInfo;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)       //1. Означает что не может быть нул, 2. Ленивая загрузка, не загружает связанную сущность сразу при запросе основной сущности. Вместо этого загружается прокси-объект, и данные подгружаются только при первом обращении к свойствам связанной сущности
    @JoinColumn(name = "company_id")        //Это необязательно, хибернейт сам поймет
    private Company company;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)  //Каскадное изменение
//    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();
}
