package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.entity.Tag;
import fr.uge.gitclout.repository.TagRepository;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.lib.Ref;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(@NotNull TagRepository tag) {
        this.tagRepository = tag;
    }

    public Tag addTag(@NotNull Ref tag, @NotNull Repo repository) {
        return new Tag(tag.getName(), tag.getObjectId(), repository);
    }

    public List<Tag> addTags(@NotNull List<Ref> tags, @NotNull Repo repository) {
        var list = new ArrayList<Tag>();
        for(var ref : tags) {
            list.add(addTag(ref, repository));
        }
        return list;
    }

    public void saveAll(@NotNull List<Tag> tags) {
        tagRepository.saveAll(tags);
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
