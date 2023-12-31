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

    /**
     * Constructs an empty Commit object.
     */
    public Commit() { }

    /**
     * Constructs a Commit object with specified attributes.
     *
     * @param message   The commit message. Must not be null.
     * @param commiter  The committer associated with the commit. Must not be null.
     * @param repository The repository associated with the commit. Must not be null.
     */
    public Commit(@NotNull String message, @NotNull Commiter commiter, @NotNull Repo repository) {
        this.message = message;
        this.commiter = commiter;
        this.repository = repository;
    }

    /**
     * Retrieves the committer associated with this commit.
     *
     * @return The committer of the commit.
     */
    public Commiter getCommiter() {
        return commiter;
    }

    /**
     * Sets the committer associated with this commit.
     *
     * @param commiter The committer to set. Must not be null.
     */
    public void setCommiter(@NotNull Commiter commiter) {
        this.commiter = commiter;
    }

    /**
     * Checks if this Commit object is equal to another object based on their IDs and committers.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return Objects.equals(Commit_id, commit.Commit_id)
                && Objects.equals(message, commit.message)
                && Objects.equals(commiter, commit.commiter);
    }

    /**
     * Generates a hash code based on the ID and committer of this Commit object.
     *
     * @return The hash code value for this Commit.
     */
    @Override
    public int hashCode() {
        return Objects.hash(Commit_id, commiter);
    }

    /**
     * Returns a string representation of the Commit object, displaying its attributes.
     *
     * @return A string containing the commit's ID, message, and committer.
     */
    @Override
    public String toString() {
        return "Commit{" +
                "id=" + Commit_id +
                ", message='" + message + '\'' +
                ", commiter=" + commiter +
                '}';
    }

    /**
     * Retrieves the repository associated with this commit.
     *
     * @return The repository of the commit.
     */
    public Repo getRepository() {
        return repository;
    }

    /**
     * Sets the repository associated with this commit.
     *
     * @param repository The repository to set.
     */
    public void setRepository(Repo repository) {
        this.repository = repository;
    }

}
