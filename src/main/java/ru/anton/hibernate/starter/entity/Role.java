package ru.anton.hibernate.starter.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    ADMIN,
    USER;

    public Optional<Role> find(String role) {
        return Arrays.stream(Role.values())
                .filter((it) -> it.name().equals(role))
                .findFirst();
    }
}
