package fr.uge.gitclout.service;

import org.eclipse.jgit.api.Git;
import fr.uge.gitclout.repository.CommiterRepository;
import fr.uge.gitclout.entity.Commiter;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommiterService {
    private final CommiterRepository commiterRepository;

    @Autowired
    public CommiterService(CommiterRepository commiterRepository) {
        this.commiterRepository = commiterRepository;
    }

    public Commiter addCommiter(Commiter commiter) {
        return commiterRepository.save(commiter);
    }

    public List<Commiter> findAll() {
        return commiterRepository.findAll();
    }

    public void addAllCommiter(Git git) throws GitAPIException {
        for (var commit : git.log().call()) {
            var commiter = new Commiter(commit.getAuthorIdent().getName(), commit.getAuthorIdent().getEmailAddress());
            commiterRepository.save(commiter);
        }
    }
}
