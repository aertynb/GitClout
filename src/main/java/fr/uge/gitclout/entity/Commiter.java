package fr.uge.gitclout.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
public class Commiter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Name of commiter cannot be null")
    private String name;

    @NotNull
    private String email;

    protected Commiter() {}

    public Commiter(String name, String email) {
        Objects.requireNonNull(name);
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("Commiter[id='%d', name='%s', Email='%s']", id, name, email);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
