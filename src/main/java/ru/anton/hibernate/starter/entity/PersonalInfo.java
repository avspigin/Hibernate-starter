package ru.anton.hibernate.starter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anton.hibernate.starter.converter.BirthdayConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable //Указывает что этот класс связан с главным, в общем что это персональные данные
public class PersonalInfo {

    private String firstName;
    private String lastName;
    @Convert(converter = BirthdayConverter.class)       //Указывается наш класс конвертора в sql и обратно, чтобы можно было использовать любой тип поля
    @Column(name = "birth_date")
    private Birthday birthDate;
}
