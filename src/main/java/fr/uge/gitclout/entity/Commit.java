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

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "commiter_id")
    private Commiter commiter;

    protected Commit() { }

    public Commit(@NotNull String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
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
        return Objects.equals(id, commit.id) && Objects.equals(commiter, commit.commiter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commiter);
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", commiter=" + commiter +
                '}';
    }
}
