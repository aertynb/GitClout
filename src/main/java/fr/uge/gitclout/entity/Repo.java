package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "repo")
public class Repo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @OneToMany
    //@NotNull
    @JoinColumn(name = "commiter_id")
    private List<Commiter> commiters;

    @OneToMany
    //@NotNull
    @JoinColumn(name = "commits_id")
    private List<Commit> commits;

    protected Repo() { }

    public Repo(List<Commiter> commiters, List<Commit> commits) {
        this.commiters = commiters;
        this.commits = commits;
    }

    public long getId() {
        return Id;
    }

    public List<Commiter> getCommiters() {
        return commiters;
    }

    public List<Commit> getCommits() {
        return commits;
    }
}
