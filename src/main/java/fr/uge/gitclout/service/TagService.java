package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Tag;
import fr.uge.gitclout.repository.TagRepository;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.lib.Ref;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(@NotNull TagRepository tag) {
        this.tagRepository = tag;
    }

    public void addTag(@NotNull Ref tag) {
        tagRepository.save(new Tag(tag.getName()));
    }

    public void addTags(@NotNull List<Ref> tags) {
        for(var ref : tags) {
            addTag(ref);
        }
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
