package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Tag;
import fr.uge.gitclout.repository.TagRepository;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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

    public void addTags(@NotNull List<Ref> tags) throws IOException {
        for(var ref : tags) {
            addTag(ref);
        }

    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public List<Tag> addAllTags(@NotNull Git git) throws GitAPIException {
        for (var tags : git.tagList().call()) {
            var tag = new Tag(tags.getName());
            tag = tagRepository.save(tag);
        }
        return findAll();
    }
}
