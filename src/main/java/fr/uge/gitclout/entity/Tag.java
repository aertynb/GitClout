package fr.uge.gitclout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.lib.ObjectId;


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


    protected Tag() { }

    public Tag(@NotNull String name, @NotNull ObjectId objId, @NotNull Repo repository) {
        this.name = name;
        this.objId = objId;
        this.repository = repository;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ObjectId getObjId() {
        return objId;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", objId=" + objId +
                ", name='" + name + '\'' +
                ", repository=" + repository.getName() +
                '}';
    }
}
