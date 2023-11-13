package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="commiter")
public class Commiter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "commiter_id")
    private Long id;

    @NotNull(message = "Name of commiter cannot be null")
    private String name;

    @NotNull
    private String email;

    @OneToMany(mappedBy = "commiter", cascade = CascadeType.ALL)
    private List<Commit> commits = new ArrayList<>();

    protected Commiter() { }

    public Commiter(String name, String email) {
        Objects.requireNonNull(name);
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("Commiter[id='%d', name='%s', Email='%s' Commits='%s']", id, name, email, commits);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commiter commiter = (Commiter) o;
        return Objects.equals(name, commiter.name) && Objects.equals(email, commiter.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
