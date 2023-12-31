package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Commit;
import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.repository.CommitRepository;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitService {

    private final CommitRepository commitRepository;

    /**
     * Constructs a CommitService with the provided CommitRepository.
     *
     * @param commitRepository The repository for Commits.
     */
    public CommitService(@NotNull CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
    }

    /**
     * Retrieves all Commits from the repository.
     *
     * @return A list of all Commits.
     */
    public List<Commit> findAll() {
        return commitRepository.findAll();
    }

    /**
     * Adds a Commit to the repository.
     *
     * @param committer  The Commiter associated with the Commit.
     * @param revCommit  The RevCommit containing Commit information.
     * @param repository The Repo associated with the Commit.
     * @return The added Commit.
     */
    public Commit addCommit(@NotNull Commiter committer, @NotNull RevCommit revCommit, @NotNull Repo repository) {
        return commitRepository.save(new Commit(revCommit.getFullMessage(), committer, repository));
    }

    /**
     * Saves a list of Commits into the repository.
     *
     * @param commits The list of Commits to be saved.
     */
    public void saveAll(@NotNull List<Commit> commits) {
        commitRepository.saveAll(commits);
    }
}
