package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLInsert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="commiter")
public class Commiter {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "commiter_id", nullable = false)
    private Long Commiter_id;

    @NotNull(message = "Name of commiter cannot be null")
    private String name;

    @NotNull
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    @Fetch(FetchMode.JOIN)
    private Repo repository;

    public Repo getRepository() {
        return repository;
    }

    public Long getCommiter_id() {
        return Commiter_id;
    }

    protected Commiter() { }

    public Commiter(@NotNull String name, @NotNull String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Commiter{" +
                "Commiter_id=" + Commiter_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", repository=" + repository +
                '}';
    }

    public Long getId() {
        return Commiter_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setRepository(@NotNull Repo repository) {
        this.repository = repository;
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
