package fr.uge.gitclout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.lib.ObjectId;


@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private Long id;

    @NotNull
    @Column(name = "taf_objectid")
    private ObjectId objId;

    @NotNull(message = "Name of tag cannot be null")
    @Column(name = "tag_name")
    private String name;

    protected Tag() { }

    public Tag(@NotNull String name, @NotNull ObjectId objId) {
        this.name = name;
        this.objId = objId;
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
                '}';
    }
}
