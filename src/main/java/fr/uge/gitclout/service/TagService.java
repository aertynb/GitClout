package fr.uge.gitclout.service;

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

    public Tag addTag(@NotNull Ref tag) {
        return tagRepository.save(new Tag(tag.getName(), tag.getObjectId()));
    }

    public List<Tag> addTags(@NotNull List<Ref> tags) {
        var list = new ArrayList<Tag>();
        for(var ref : tags) {
            list.add(addTag(ref));
        }
        return list;
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
