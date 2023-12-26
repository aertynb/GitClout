package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Repo;
import jakarta.validation.constraints.NotNull;
import fr.uge.gitclout.repository.CommiterRepository;
import fr.uge.gitclout.entity.Commiter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommiterService {
    private final CommiterRepository commiterRepository;

    public CommiterService(@NotNull CommiterRepository commiterRepository) {
        this.commiterRepository = commiterRepository;
    }

    public Commiter addCommiter(Commiter commiter) {
        return commiterRepository.save(commiter);
    }

    public Commiter addCommiter(@NotNull RevCommit revCommit, @NotNull Repo repo) {
        var commiter = new Commiter(revCommit.getAuthorIdent().getName(), revCommit.getAuthorIdent().getEmailAddress(), repo);
        return commiterRepository.findOne(Example.of(commiter)).orElseGet(() -> commiterRepository.save(commiter));
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
