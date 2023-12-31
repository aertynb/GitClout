package fr.uge.gitclout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Repo repository;

    @OneToMany(mappedBy = "commiter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contribution> contributions;

    public Repo getRepository() {
        return repository;
    }

    /**
     * Constructs an empty Commiter object.
     */
    public Commiter() { }

    /**
     * Constructs a Commiter object with specified attributes.
     *
     * @param name       The name of the committer. Must not be null.
     * @param email      The email of the committer. Must not be null.
     * @param repository The repository associated with the committer. Must not be null.
     */
    public Commiter(@NotNull String name, @NotNull String email, @NotNull Repo repository) {
        this.name = name;
        this.email = email;
        this.repository = repository;
        contributions = new ArrayList<>();
    }

    /**
     * Returns a string representation of the Commiter object, displaying its attributes.
     *
     * @return A string containing the committer's ID, name, email, and associated repository name.
     */
    @Override
    public String toString() {
        return "Commiter{" +
                "Commiter_id=" + Commiter_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", repository=" + repository.getName() +
                '}';
    }

    /**
     * Retrieves the ID associated with this committer.
     *
     * @return The ID of the committer.
     */
    public Long getCommiter_idId() {
        return Commiter_id;
    }

    /**
     * Retrieves the name of this committer.
     *
     * @return The name of the committer.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the email of this committer.
     *
     * @return The email of the committer.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the repository associated with this committer.
     *
     * @param repository The repository to set. Must not be null.
     */
    public void setRepository(@NotNull Repo repository) {
        this.repository = repository;
    }

    /**
     * Adds a contribution made by this committer.
     *
     * @param contribution The contribution to add. Must not be null.
     */
    public void addContribution(@NotNull Contribution contribution) {
        contributions.add(contribution);
    }

    /**
     * Checks if this Commiter object is equal to another object based on their name and email.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commiter commiter = (Commiter) o;
        return Objects.equals(name, commiter.name) && Objects.equals(email, commiter.email);
    }

    /**
     * Generates a hash code based on the name and email of this Commiter object.
     *
     * @return The hash code value for this Commiter.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
