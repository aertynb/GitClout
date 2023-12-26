package fr.uge.gitclout.service;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.entity.Contribution;
import fr.uge.gitclout.repository.ContributionRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContributionService {
    private final ContributionRepository contributionRepository;

    public ContributionService(@NotNull ContributionRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
    }

    private Optional<Contribution> checkContribution(Commiter commiter) {
        return contributionRepository.findAll().stream()
                .filter(x -> x.getCommiter().equals(commiter))
                .findFirst();
    }

    public List<Contribution> findAll() {
        return contributionRepository.findAll();
    }

    public void addContribution(@NotNull Commiter commiter, @NotNull int lines) {
        var opt = checkContribution(commiter);
        if (opt.isPresent()) {
            updateContribution(opt.orElseThrow(), lines);
            return;
        }
        var contribution = contributionRepository.save(new Contribution(commiter, lines));
        commiter.addContribution(contribution);
    }

    private void updateContribution(Contribution contribution, int lines) {
        contribution.setAddedLines(lines);
    }
}
