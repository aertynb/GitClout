package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Commit;
import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.repository.CommitRepository;
import fr.uge.gitclout.repository.CommiterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.eclipse.jgit.api.Git;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;


@Service
public class CommitService {

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private CommiterRepository commiterRepository;

    public List<Commit> findAll() {
        return commitRepository.findAll();
    }

    public Commit addCommit(Commit commit) {
        return commitRepository.save(commit);
    }

    public Commit createCommitWithCommiter(Long idCommiter, Commit commit) {
        Commiter commiter = commiterRepository.findById(idCommiter).orElseThrow(() -> new EntityNotFoundException("Commiter not found"));
        commit.setCommiter(commiter);
        return commitRepository.save(commit);
    }

    List<Commiter> getAllCommiter(){
        return commiterRepository.findAll();
    }

    public void addAllCommit(Git git) throws GitAPIException {
        Objects.requireNonNull(git);
        var set = new HashSet<Commit>();
        for(var commit : git.log().call()) {
            var newCommit = new Commit();
            set.add(newCommit);
        }
        commitRepository.saveAll(set);
    }
}
