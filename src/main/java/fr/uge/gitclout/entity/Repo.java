package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "repo")
public class Repo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @NotNull
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Commiter> commiters;

    @NotNull
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Commit> commits;

    @NotNull
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tag> tags;

    /**
     * Constructs an empty Repo object.
     */
    public Repo() { }

    /**
     * Constructs a Repo object with a specified name.
     *
     * @param name The name of the repository.
     */
    public Repo(@NotNull String name) {
        this.name = name;
    }

    /**
     * Retrieves the ID associated with this repository.
     *
     * @return The ID of the repository.
     */
    public long getId() {
        return id;
    }

    /**
     * Retrieves the name of this repository.
     *
     * @return The name of the repository.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the list of committers associated with this repository.
     *
     * @return The list of committers.
     */
    public List<Commiter> getCommiters() {
        return commiters;
    }

    /**
     * Retrieves the list of commits associated with this repository.
     *
     * @return The list of commits.
     */
    public List<Commit> getCommits() {
        return commits;
    }

    /**
     * Retrieves the list of tags associated with this repository.
     *
     * @return The list of tags.
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Sets the list of committers associated with this repository.
     *
     * @param commiters The list of committers to set. Must not be null.
     */
    public void setCommiters(@NotNull List<Commiter> commiters) {
        this.commiters = commiters;
    }

    /**
     * Sets the list of tags associated with this repository.
     *
     * @param tags The list of tags to set.
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Sets the list of commits associated with this repository.
     *
     * @param commits The list of commits to set.
     */
    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    /**
     * Returns a string representation of the Repo object, displaying its attributes.
     *
     * @return A string containing the repository's ID, name, committers, commits, and tags.
     */
    @Override
    public String toString() {
        return "Repo{" +
                "Repo_id=" + id +
                ", name='" + name + '\'' +
                ", commiters=" + commiters +
                ", commits=" + commits +
                ", tags=" + tags +
                '}';
    }
}
