package fr.uge.gitclout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Repo repository;

    public Repo getRepository() {
        return repository;
    }

    public Long getCommiter_id() {
        return Commiter_id;
    }

    protected Commiter() { }

    public Commiter(@NotNull String name, @NotNull String email, @NotNull Repo repository) {
        this.name = name;
        this.email = email;
        this.repository = repository;
    }

    @Override
    public String toString() {
        return "Commiter{" +
                "Commiter_id=" + Commiter_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", repository=" + repository.getName() +
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
