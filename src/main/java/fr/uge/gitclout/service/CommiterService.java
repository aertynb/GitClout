package fr.uge.gitclout.service;

import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.Git;
import fr.uge.gitclout.repository.CommiterRepository;
import fr.uge.gitclout.entity.Commiter;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class CommiterService {
    private final CommiterRepository commiterRepository;

    public CommiterService(@NotNull CommiterRepository commiterRepository) {
        this.commiterRepository = commiterRepository;
    }

    public Commiter addCommiter(Commiter commiter) {
        return commiterRepository.save(commiter);
    }

    public Commiter addCommiter(@NotNull RevCommit revCommit) {
        var commiter = new Commiter(revCommit.getAuthorIdent().getName(), revCommit.getAuthorIdent().getEmailAddress());
        var opt = commiterRepository.findOne(Example.of(commiter));
        return opt.orElseGet(() -> commiterRepository.save(commiter));
    }

    public List<Commiter> findAll() {
        return commiterRepository.findAll();
    }


    /*public void addAllCommiter(Git git) throws GitAPIException {
        Objects.requireNonNull(git);
        var set = new HashSet<Commiter>();
        for (var commit : git.log().call()) {
            var commiter = new Commiter(commit.getAuthorIdent().getName(), commit.getAuthorIdent().getEmailAddress());
            set.add(commiter);
        }
        commiterRepository.saveAll(set);
    }*/
}
