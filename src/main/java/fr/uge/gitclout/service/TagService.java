package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.entity.Tag;
import fr.uge.gitclout.repository.TagRepository;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.lib.Ref;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Tags.
 */
@Service
public class TagService {

    private final TagRepository tagRepository;

    /**
     * Constructs a TagService with the provided TagRepository.
     *
     * @param tagRepository The repository for Tags.
     */
    public TagService(@NotNull TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Adds a Tag to the repository using a Ref and a Repo.
     *
     * @param tag        The Ref representing the Tag.
     * @param repository The Repo associated with the Tag.
     * @return The added Tag.
     */
    public Tag addTag(@NotNull Ref tag, @NotNull Repo repository) {
        return new Tag(tag.getName(), tag.getObjectId(), repository);
    }

    /**
     * Adds multiple Tags to the repository using a list of Refs and a Repo.
     *
     * @param tags       The list of Refs representing the Tags.
     * @param repository The Repo associated with the Tags.
     * @return The list of added Tags.
     */
    public List<Tag> addTags(@NotNull List<Ref> tags, @NotNull Repo repository) {
        var tagList = new ArrayList<Tag>();
        for (var ref : tags) {
            tagList.add(addTag(ref, repository));
        }
        return tagList;
    }

    /**
     * Saves a list of Tags into the repository.
     *
     * @param tags The list of Tags to be saved.
     */
    public void saveAll(@NotNull List<Tag> tags) {
        tagRepository.saveAll(tags);
    }

    /**
     * Retrieves all Tags from the repository.
     *
     * @return A list of all Tags.
     */
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
