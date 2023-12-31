package fr.uge.gitclout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.lib.ObjectId;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private ObjectId objId;

    @NotNull(message = "Name of tag cannot be null")
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Repo repository;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contribution> contributions;

    /**
     * Constructs an empty Tag.
     */
    public Tag() { }

    /**
     * Creates an instance of Tag with the given name, object ID, and repository.
     *
     * @param name       The name of the Tag. Must not be null.
     * @param objId      The object ID associated with the Tag. Must not be null.
     * @param repository The repository to which the Tag is associated. Must not be null.
     */
    public Tag(@NotNull String name, @NotNull ObjectId objId, @NotNull Repo repository) {
        this.name = name;
        this.objId = objId;
        this.repository = repository;
        contributions = new ArrayList<>();
    }

    /**
     * Retrieves the ID associated with this object.
     *
     * @return The ID of the object.
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the name associated with this Tag.
     *
     * @return The name of the Tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the object ID associated with this Tag.
     *
     * @return The object ID of the Tag.
     */
    public ObjectId getObjId() {
        return objId;
    }

    /**
     * Retrieves the contributions associated with this Tag.
     *
     * @return The list of contributions linked with the Tag.
     */
    public List<Contribution> getContributions() {
        return contributions;
    }

    /**
     * Adds a contribution to this Tag.
     *
     * @param contribution The contribution to add. Must not be null.
     */
    public void addContributions(@NotNull Contribution contribution) {
        contributions.add(contribution);
    }


    /**
     * Returns a string representation of the Tag object, displaying its attributes.
     *
     * @return A string containing the Tag's attributes: ID, object ID, name, repository, and contributions.
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", objId=" + objId +
                ", name='" + name + '\'' +
                ", repository=" + repository +
                ", contributions=" + contributions +
                '}';
    }
}
