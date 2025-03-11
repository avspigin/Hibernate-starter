package ru.anton.hibernate.starter.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "profile"})  //исключает из тустринг поле компани
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //IDENTITY дает сериализацию БД, SEQUENCE - создаем свой сиквенс
    private Long id;

    private String username;
    @Embedded       //Показывает что он главный, но это необязательно, мы указали на песроналИнфо
    private PersonalInfo personalInfo;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)       //Каскадное изменение
    @JoinColumn(name = "company_id")        //Это необязательно, хибернейт сам поймет
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

}
