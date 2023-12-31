package fr.uge.gitclout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.uge.gitclout.utilities.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a contribution made by a commit to a repository.
 */
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

    /**
     * Default constructor initializing addedLines to 0.
     */
    public Contribution() {
        addedLines = 0;
    }

    /**
     * Constructor to create a Contribution instance.
     *
     * @param commiter    The commit's author.
     * @param addedLines  The lines added in the commit.
     * @param tag         The associated tag of the commit.
     * @param language    The language used in the commit.
     */
    public Contribution(@NotNull Commiter commiter, int addedLines, @NotNull Tag tag, @NotNull Language language) {
        this.commiter = commiter;
        this.addedLines = addedLines;
        this.tag = tag;
        this.language = language;
    }

    /**
     * Retrieves the ID of the Contribution.
     *
     * @return The ID of the Contribution.
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the Commiter associated with the Contribution.
     *
     * @return The Commiter associated with the Contribution.
     */
    public Commiter getCommiter() {
        return commiter;
    }

    /**
     * Retrieves the Tag associated with the Contribution.
     *
     * @return The Tag associated with the Contribution.
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Retrieves the number of added lines in the Contribution.
     *
     * @return The number of added lines in the Contribution.
     */
    public int getAddedLines() {
        return addedLines;
    }

    /**
     * Retrieves the language used in the Contribution.
     *
     * @return The language used in the Contribution.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets the Commiter associated with the Contribution.
     *
     * @param commiter The Commiter to set for the Contribution.
     */
    public void setCommiter(@NotNull Commiter commiter) {
        this.commiter = commiter;
    }

    /**
     * Sets the Tag associated with the Contribution.
     *
     * @param tag The Tag to set for the Contribution.
     */
    public void setTag(@NotNull Tag tag) {
        this.tag = tag;
    }

    /**
     * Sets the number of added lines in the Contribution.
     *
     * @param addedLines The number of added lines to set for the Contribution.
     */
    public void setAddedLines(@NotNull int addedLines) {
        this.addedLines += addedLines;
    }

    /**
     * Overrides the toString method to provide a meaningful representation of the Contribution object.
     *
     * @return A string representation of the Contribution object.
     */
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
