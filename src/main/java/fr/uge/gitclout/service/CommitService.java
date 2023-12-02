package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Commit;
import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.repository.CommitRepository;
import fr.uge.gitclout.repository.CommiterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.stereotype.Service;
import org.eclipse.jgit.api.Git;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


@Service
public class CommitService {

    private CommitRepository commitRepository;

    private CommiterRepository commiterRepository;

    public CommitService(@NotNull CommitRepository commitRepository, @NotNull CommiterRepository commiterRepository) {
        this.commitRepository = commitRepository;
        this.commiterRepository = commiterRepository;
    }

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

    public Commit addCommit(@NotNull Commiter committer, @NotNull RevCommit revCommit) {
        var commit = commitRepository.save(new Commit(revCommit.getFullMessage()));
        commit.setCommiter(committer);
        return commit;
    }

    List<Commiter> getAllCommiter(){
        return commiterRepository.findAll();
    }

    /*public void addAllCommit(Git git) throws GitAPIException, IOException {
        Objects.requireNonNull(git);
        var set = new HashSet<Commit>();
        for(var commit : git.log().call()) {
            var newCommit = new Commit();
            set.add(newCommit);
        }
        commitRepository.saveAll(set);
    }*/
}
