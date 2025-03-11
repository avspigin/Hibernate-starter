package ru.anton.hibernate.starter.converter;

import jakarta.persistence.AttributeConverter;
import ru.anton.hibernate.starter.entity.Birthday;

import java.sql.Date;
import java.util.Optional;

public class BirthdayConverter  implements AttributeConverter<Birthday, Date> {

    @Override
    public Date convertToDatabaseColumn(Birthday birthday) {
        return Optional.ofNullable(birthday)
                .map(Birthday::birthDate)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public Birthday convertToEntityAttribute(Date date) {
        return Optional.ofNullable(date)
                .map(Date::toLocalDate)
                .map(Birthday::new)
                .orElse(null);
    }
}
