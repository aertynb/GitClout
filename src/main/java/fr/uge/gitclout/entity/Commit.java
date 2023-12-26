package fr.uge.gitclout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name="commits")
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Commit_id;

    private String message;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn
    private Commiter commiter;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Repo repository;

    protected Commit() { }

    public Commit(@NotNull String message, @NotNull Commiter commiter, @NotNull Repo repository) {
        this.message = message;
        this.commiter = commiter;
        this.repository = repository;
    }

    public Long getCommit_id() {
        return Commit_id;
    }

    public Commiter getCommiter() {
        return commiter;
    }

    public void setCommiter(@NotNull Commiter commiter) {
        this.commiter = commiter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return Objects.equals(Commit_id, commit.Commit_id) && Objects.equals(commiter, commit.commiter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Commit_id, commiter);
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + Commit_id +
                ", message='" + message + '\'' +
                ", commiter=" + commiter +
                '}';
    }
}
