package fr.uge.gitclout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.uge.gitclout.utilities.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
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
     * Constructs an empty Contribution object with zero added lines.
     */
    public Contribution() {
        addedLines = 0;
    }

    /**
     * Constructs a Contribution object with specified attributes.
     *
     * @param commiter   The committer associated with the contribution. Must not be null.
     * @param addedLines The number of lines added in the contribution.
     * @param tag        The tag associated with the contribution. Must not be null.
     * @param language   The language used in the contribution.
     */
    public Contribution(@NotNull Commiter commiter, int addedLines, @NotNull Tag tag, @NotNull Language language) {
        this.commiter = commiter;
        this.addedLines = addedLines;
        this.tag = tag;
        this.language = language;
    }

    /**
     * Retrieves the ID associated with this contribution.
     *
     * @return The ID of the contribution.
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the committer associated with this contribution.
     *
     * @return The committer of the contribution.
     */
    public Commiter getCommiter() {
        return commiter;
    }

    /**
     * Retrieves the tag associated with this contribution.
     *
     * @return The tag of the contribution.
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Retrieves the number of added lines in this contribution.
     *
     * @return The number of added lines in the contribution.
     */
    public int getAddedLines() {
        return addedLines;
    }

    /**
     * Retrieves the language used in this contribution.
     *
     * @return The language of the contribution.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets the committer associated with this contribution.
     *
     * @param commiter The committer to set. Must not be null.
     */
    public void setCommiter(@NotNull Commiter commiter) {
        this.commiter = commiter;
    }

    /**
     * Sets the tag associated with this contribution.
     *
     * @param tag The tag to set. Must not be null.
     */
    public void setTag(@NotNull Tag tag) {
        this.tag = tag;
    }

    /**
     * Increments the number of added lines in this contribution by the specified value.
     *
     * @param addedLines The number of lines to increment the added lines by. Must not be null.
     */
    public void setAddedLines(@NotNull int addedLines) {
        this.addedLines += addedLines;
    }

    /**
     * Returns a string representation of the Contribution object, displaying its attributes.
     *
     * @return A string containing the contribution's ID, committer, tag, added lines, and language.
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
