package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name="commits")
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "commits_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "commiter_id", nullable = false)
    private Commiter commiter;

    public Commit(Commiter commiter) {
        Objects.requireNonNull(commiter);
        this.commiter = commiter;
    }

    public Commit() {

    }

    public Long getId() {
        return id;
    }

    public Commiter getCommiter() {
        return commiter;
    }

    public void setCommiter(Commiter commiter) {
        Objects.requireNonNull(commiter);
        this.commiter = commiter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return Objects.equals(id, commit.id) && Objects.equals(commiter, commit.commiter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commiter);
    }
}
