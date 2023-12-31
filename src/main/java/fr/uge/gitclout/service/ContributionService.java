package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.entity.Contribution;
import fr.uge.gitclout.entity.Tag;
import fr.uge.gitclout.repository.ContributionRepository;
import fr.uge.gitclout.utilities.Language;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Contributions.
 */
@Service
public class ContributionService {
    private final ContributionRepository contributionRepository;

    /**
     * Constructs a ContributionService with the provided ContributionRepository.
     *
     * @param contributionRepository The repository for Contributions.
     */
    public ContributionService(@NotNull ContributionRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
    }

    /**
     * Retrieves all Contributions from the repository.
     *
     * @return A list of all Contributions.
     */
    public List<Contribution> findAll() {
        return contributionRepository.findAll();
    }

    /**
     * Adds a Contribution to the repository and associates it with a Commiter, Tag, and Language.
     *
     * @param commiter The Commiter associated with the Contribution.
     * @param lines    The number of lines contributed.
     * @param tag      The Tag associated with the Contribution.
     * @param language The Language used for the Contribution.
     * @return The added Contribution.
     */
    public Contribution addContribution(@NotNull Commiter commiter, @NotNull int lines, @NotNull Tag tag, @NotNull Language language) {
        var contribution = new Contribution(commiter, lines, tag, language);
        commiter.addContribution(contribution);
        tag.addContributions(contribution);
        return contribution;
    }

    /**
     * Saves a list of Contributions into the repository.
     *
     * @param contributions The list of Contributions to be saved.
     */
    public void saveAll(@NotNull List<Contribution> contributions) {
        contributionRepository.saveAll(contributions);
    }
}
