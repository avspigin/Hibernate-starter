package ru.anton.hibernate.starter.entity;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

//Этот класс для полей, которые можно передать наследнику
@MappedSuperclass
public interface BaseEntity <T extends Serializable> {
    T getId();
    void setId(T id);
}
