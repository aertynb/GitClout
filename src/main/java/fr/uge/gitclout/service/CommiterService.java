package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Repo;
import jakarta.validation.constraints.NotNull;
import fr.uge.gitclout.repository.CommiterRepository;
import fr.uge.gitclout.entity.Commiter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class CommiterService {

    private final CommiterRepository commiterRepository;

    /**
     * Constructs a CommiterService with the provided CommiterRepository.
     *
     * @param commiterRepository The repository for Commiters.
     */
    public CommiterService(@NotNull CommiterRepository commiterRepository) {
        this.commiterRepository = commiterRepository;
    }

    /**
     * Adds a Commiter to the repository.
     *
     * @param commiter The Commiter to be added.
     * @return The added Commiter.
     */
    public Commiter addCommiter(Commiter commiter) {
        return commiterRepository.save(commiter);
    }

    /**
     * Adds a Commiter using information from a RevCommit and a Repo.
     *
     * @param revCommit The RevCommit containing Commiter information.
     * @param repo      The Repo associated with the Commiter.
     * @return The added or existing Commiter.
     */
    public Commiter addCommiter(@NotNull RevCommit revCommit, @NotNull Repo repo) {
        var commiter = new Commiter(revCommit.getAuthorIdent().getName(), revCommit.getAuthorIdent().getEmailAddress(), repo);
        return commiterRepository.findOne(Example.of(commiter)).orElseGet(() -> commiterRepository.save(commiter));
    }

    /**
     * Retrieves all Commiters from the repository.
     *
     * @return A list of all Commiters.
     */
    public List<Commiter> findAll() {
        return commiterRepository.findAll();
    }

    /**
     * Saves a set of Commiters into the repository.
     *
     * @param commiters The set of Commiters to be saved.
     */
    public void saveAll(@NotNull HashSet<Commiter> commiters) {
        commiterRepository.saveAll(commiters);
    }
}
