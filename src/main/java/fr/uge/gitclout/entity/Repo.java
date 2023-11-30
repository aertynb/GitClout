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

    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "commiter_id")
    private List<Commiter> commiters;

    @OneToMany(fetch = FetchType.LAZY)
    //@NotNull
    @JoinColumn(name = "commits_id")
    private List<Commit> commits;

    protected Repo() { }

    public Repo(@NotNull String name) {
        this.name = name;
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

    public void setCommiters(@NotNull List<Commiter> commiters) {
        this.commiters = commiters;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", commiters=" + commiters +
                ", commits=" + commits +
                '}';
    }
}
