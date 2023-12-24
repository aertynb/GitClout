package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Objects;

@Entity
@Table(name="commits")
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Commit_id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Fetch(FetchMode.JOIN)
    private Commiter commiter;

    protected Commit() { }

    public Commit(@NotNull String message, @NotNull Commiter commiter) {
        this.message = message;
        this.commiter = commiter;
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
