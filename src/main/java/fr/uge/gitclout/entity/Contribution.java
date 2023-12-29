package fr.uge.gitclout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.uge.gitclout.utilities.Language;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private Tag tag;

    private int addedLines;

    private Language language;

    public Contribution() {
        addedLines = 0;
    }

    public Contribution(@NotNull Commiter commiter, int addedLines, @NotNull Tag tag, @NotNull Language language) {
        this.commiter = commiter;
        this.addedLines = addedLines;
        this.tag = tag;
        this.language = language;
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
                ", tag=" + tag.getName() +
                ", addedLines=" + addedLines +
                ", language=" + language +
                '}';
    }
}
