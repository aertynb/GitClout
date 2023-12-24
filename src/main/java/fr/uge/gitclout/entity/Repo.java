package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "repo")
public class Repo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Repo_id;

    private String name;

    @NotNull
    @OneToMany(cascade = {CascadeType.ALL})
    @Fetch(FetchMode.JOIN)
    private List<Commiter> commiters;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Commit_id")
    private List<Commit> commits;

    protected Repo() { }

    public Repo(@NotNull String name) {
        this.name = name;
    }

    public long getRepo_id() {
        return Repo_id;
    }

    public String getName() { return name; }

    public List<Commiter> getCommiters() {
        return commiters;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommiters(@NotNull List<Commiter> commiters) {
        this.commiters = commiters;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "Id=" + Repo_id +
                ", name='" + name + '\'' +
                ", commiters=" + commiters +
                ", commits=" + commits +
                '}';
    }
}
