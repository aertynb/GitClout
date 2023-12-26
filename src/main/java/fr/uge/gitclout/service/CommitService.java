package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Commit;
import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.repository.CommitRepository;
import fr.uge.gitclout.repository.CommiterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.List;


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

    public Commit addCommit(@NotNull Commiter committer, @NotNull RevCommit revCommit, @NotNull Repo repository) {
        return new Commit(revCommit.getFullMessage(), committer, repository);
    }

    public void saveAll(@NotNull List<Commit> commits) {
        commitRepository.saveAll(commits);
    }

    List<Commiter> getAllCommiter(){
        return commiterRepository.findAll();
    }
}
