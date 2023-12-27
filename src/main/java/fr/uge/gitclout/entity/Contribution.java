package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Entity
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Commiter commiter;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private Tag tag;

    private int addedLines;

    public Contribution() {
        addedLines = 0;
    }

    public Contribution(@NotNull Commiter commiter, int addedLines, @NotNull Tag tag) {
        this.commiter = commiter;
        this.addedLines = addedLines;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public Commiter getCommiter() {
        return commiter;
    }

    public Tag getTag() {
        return tag;
    }

    public int getAddedLines() {
        return addedLines;
    }

    public void setCommiter(@NotNull Commiter commiter) {
        this.commiter = commiter;
    }

    public void setTag(@NotNull Tag tag) {
        this.tag = tag;
    }

    public void setAddedLines(@NotNull int addedLines) {
        this.addedLines += addedLines;
    }

    @Override
    public String toString() {
        return "Contribution{" +
                "id=" + id +
                ", commiter=" + commiter.getName() +
                ", tag=" + "null" +
                ", addedLines=" + addedLines +
                '}';
    }
}
