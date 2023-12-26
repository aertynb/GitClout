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

    protected Repo() { }

    public Repo(@NotNull String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Commiter> getCommiters() {
        return commiters;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setCommiters(@NotNull List<Commiter> commiters) {
        this.commiters = commiters;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

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
